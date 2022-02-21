/*======================= Servidor interactivo TCP ============================
Este servidor destina-se mostrar mensagens recebidas via TCP, no porto
definido pela constante SERV_TCP_PORT.
Trata-se de um servidor que envia confirmacao (o comprimento, em bytes, da
mensagem recebida).

Em relacao ao exemplo servidorTCPv1.c, optou-se por uma abordagem mais
rigorosa em termos dos acessos para leitura e escrita no socket TCP.
Para o efeito, sao usadas as fun��es auxiliares WriteN (+/- opcional) 
e ReadLine (obrigatoria quando se assume que a mensagem termina em '\n', como
acontece quando se recorre ao clienteTCP.c). Quando se pretende ler um numero 
fixo de bytes, deve usar-se uma funcao do tipo ReadN do codigo ctcp10.c
===============================================================================
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock.h>

#define SERV_TCP_PORT  6000
#define BUFFERSIZE     4096

void Abort(char *msg, SOCKET s);
void AtendeCliente(SOCKET sock);
int writeN(SOCKET sock,char * buffer,int nbytes);
int readLine(SOCKET sock,char * buffer,int nbytes);

/*________________________________ main ________________________________________
*/
int main(int argc,char *argv[]){

	SOCKET sock = INVALID_SOCKET, newSock = INVALID_SOCKET;
	int iResult;
	int cliaddr_len;	
	struct sockaddr_in cli_addr,serv_addr;
	WSADATA wsaData;

	/*=============== INICIA OS WINSOCKS ==============*/
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*================== ABRE SOCKET PARA ESCUTA DE CLIENTES ================*/
	if((sock=socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket", sock);

	/*=================== PREENCHE ENDERECO DE ESCUTA =======================*/
	memset((char*)&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family=AF_INET;
	serv_addr.sin_addr.s_addr=htonl(INADDR_ANY);  /*Recebe de qq interface*/
	serv_addr.sin_port=htons(SERV_TCP_PORT);  /*Escuta no porto Well-Known*/

	/*====================== REGISTA-SE PARA ESCUTA =========================*/
	if(bind(sock,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta", sock);

	/*============ AVISA QUE ESTA PRONTO A ACEITAR PEDIDOS ==================*/
	if(listen(sock,5) == SOCKET_ERROR) 
		Abort("Impossibilidade de escutar pedidos", sock);

	/*================ PASSA A ATENDER CLIENTES INTERACTIVAMENTE =============*/
	cliaddr_len=sizeof(cli_addr);
	while(1){
		/*====================== ATENDE PEDIDO ========================*/
		if((newSock=accept(sock,(struct sockaddr *)&cli_addr,&cliaddr_len)) == SOCKET_ERROR)
			fprintf(stderr,"<SERV> Impossibilidade de aceitar cliente...\n");
		else{
			AtendeCliente(newSock);
			closesocket(newSock);
		}
	}
}

/*___________________________ AtendeCliente ____________________________________
Atende cliente. 
______________________________________________________________________________*/

void AtendeCliente(SOCKET sock){
	static char buffer[BUFFERSIZE];
	static unsigned int cont=0;
	int nbytes, nBytesSent;
		
	/*==================== PROCESSA PEDIDO ==========================*/
	switch((nbytes=readLine(sock, buffer, BUFFERSIZE))){

		case SOCKET_ERROR:
			fprintf(stderr,"\n<SER> Erro na recepcao de dados...\n");
			return;

		case  0:
			fprintf(stderr,"\n<SER> O cliente nao enviou dados...\n");
			return;

		default:
			buffer[nbytes]='\0';
			printf("\n<SER> Mensagem n. %d recebida {%s}\n", ++cont, buffer);				
				
			/*============ ENVIA CONFIRMACAO =============*/
			printf("<SER> Confirma recepcao de mensagem.\n");
			sprintf_s(buffer, BUFFERSIZE, "%d\n", nbytes);
			nbytes=strlen(buffer);

			if((nBytesSent=writeN(sock, buffer, nbytes)) == SOCKET_ERROR)
				fprintf(stderr,"<SER> Impossibilidade de Confirmar.\n");
			
			else 
				printf("<SER> Mensagem confirmada.\n");
	}
	
}

/*________________________________ Abort________________________________________
Mostra a mensagem de erro associada ao ultimo erro no SO e abando com 
"exit status" a 1
_______________________________________________________________________________
*/
void Abort(char *msg, SOCKET s)
{
	fprintf(stderr,"\a<SER >Erro fatal: <%d>\n", WSAGetLastError());
	
	if(s != INVALID_SOCKET)
		closesocket(s);

	exit(EXIT_FAILURE);
}

/*______________________________ writeN _______________________________________
Escreve n bytes no socket em causa. Devolve o numero de bytes escritos ou
SOCKET_ERROR caso ocorra um erro ou 0 caso a ligacao seja encerrada.
______________________________________________________________________________*/

int writeN(SOCKET sock,char * buffer,int nbytes){

	int nLeft,nWritten;

	nLeft=nbytes;

	while(nLeft>0){

		nWritten=send(sock,buffer,nLeft, 0);

		if(nWritten == 0 || nWritten == SOCKET_ERROR)
			return(nWritten); /*Erro ou End Of File (0) */

		nLeft-=nWritten;
		buffer+=nWritten;

	}

	return(nWritten);
}

/*______________________________ readLine _______________________________________
Le uma linha de texto (conjunto de caracteres terminados em '\n') 
no socket em causa.

Regressa quando encontra o caractere '\n', quando o buffer fica cheio,
quando a ligacao TCP e' encerrada ou quando quando ocorre um erro.

Devolve o numero de bytes lidos ou SOCKET_ERROR caso ocorra um erro.
______________________________________________________________________________*/

int readLine(SOCKET sock,char * buffer,int nbytes){

	int nread,i;
	char c;

	i = 0;

	while(i < nbytes-1){ /* -1 para deixar espaco ao '\0' */
		
		nread = recv(sock, &c, sizeof(c), 0);
	
		if(nread == SOCKET_ERROR)
			return nread; /*Erro*/

		if(nread == 0)
			break; /*End Of File*/

		if(c == '\r')
			continue; /*Ignora o '\r' numa sequencia "\r\n"*/
		
		if(c == '\n')
			break; /*Final da linha*/

		buffer[i++] = c;
	}
	
	buffer[i] = '\0';
	
	return i;
}
