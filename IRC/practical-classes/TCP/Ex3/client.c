/*=========================== Cliente basico TCP ===============================
Este cliente destina-se a enviar mensagens passadas na linha de comando, sob
a forma de um argumento, para um servidor especifico cujo socket e' fornecido atrav�s
da linha de comando. Tambem e' aguarda confirmacao (trata-se do comprimento da mensagem).

O protocolo usado e' o TCP.

Em relaxao ao exemplo clienteTCPv1a.c, optou-se por uma abordagem mais
rigorosa em termos dos acessos para leitura e escrita no socket TCP.
Para o efeito, sao usadas as fun�oes auxiliares WriteN e ReadLine.
==============================================================================*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <winsock.h>

#define BUFFERSIZE     4096

void Abort(char *msg, SOCKET s);
int writeN(SOCKET sock,char * buffer,int nbytes);
int readLine(SOCKET sock,char * buffer,int nbytes);

/*________________________________ main ________________________________________
*/

int main(int argc,char *argv[]){

	int sock = INVALID_SOCKET;
	int msg_len, nbytes, iResult;
	struct sockaddr_in serv_addr;
	char buffer[BUFFERSIZE];
	struct hostent *info;
	WSADATA wsaData;

	if(argc!=4){ /*Testa sintaxe*/
		fprintf(stderr,"<CLI> Sintaxe: %s \"frase_a_enviar\" ip_destino porto_destino\n",argv[0]);
		exit(EXIT_FAILURE);
	}

	/*=============== INICIA OS WINSOCKS ==============*/
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*=============== ABRE SOCKET PARA CONTACTAR O SERVIDOR ==============*/
	if((sock=socket(PF_INET,SOCK_STREAM,IPPROTO_TCP)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket", sock);

	/*================= PREENCHE ENDERECO DO SERVIDOR ====================*/
	memset((char*)&serv_addr, 0, sizeof(serv_addr));	/*a zero todos os bytes*/
	serv_addr.sin_family=AF_INET;				/*Address Family - Internet*/
	serv_addr.sin_addr.s_addr=inet_addr(argv[2]);   
	serv_addr.sin_port=htons(atoi(argv[3]));  

	if (serv_addr.sin_addr.s_addr == INADDR_NONE) {
		info = gethostbyname(argv[2]);

		if (info == NULL)
			Abort("Servidor desconhecido", sock);

		memcpy(&(serv_addr.sin_addr.s_addr), info->h_addr, info->h_length);

		printf("Endereco IP de %s: %s\n", argv[2], inet_ntoa(serv_addr.sin_addr));
	}


	/*========================== ESTABELECE LIGACAO ======================*/
	if(connect(sock,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de estabelecer ligacao", sock);

	/*====================== ENVIA MENSAGEM AO SERVIDOR ==================*/
	msg_len=strlen(argv[1]);

	if((nbytes=writeN(sock, argv[1], msg_len)) == SOCKET_ERROR)
		Abort("Impossibilidade de transmitir mensagem...", WSAGetLastError());
	
	else 
		fprintf(stderr, "<CLI> Mensagem \"%s\" enviada\n", argv[1]);
	
	/*Conclui a mensagem com um indicador final de linha de texto.  E' necessario para que a funcao readLine no receptor retorne.
	  Em alternativa, pode ser composta uma mensagem completa  (e.g., "sprintf(msg, "%s\n", argv[0])") e realizada
	  apenas uma operacao de envio.*/
	msg_len = strlen("\n");
	if(writeN(sock,"\n", msg_len) != msg_len) /*termina mensagem com '\n'*/
		Abort("Impossibilidade de transmitir \"\\n\"...", sock);

	/*========================== ESPERA CONFIRMACAO =======================*/
	nbytes=readLine(sock, buffer, sizeof(buffer));

	if(nbytes == SOCKET_ERROR) 
		Abort("Impossibilidade de receber confirmacao", sock);

	printf("<CLI> Confirmacao recebida {%s}.\n",buffer);

	/*=========================== FECHA SOCKET ============================*/
	closesocket(sock);

	exit(EXIT_SUCCESS);
}


/*________________________________ Abort________________________________________
Mostra a mensagem de erro associada ao ultimo erro no SO e abando com 
"exit status" a 1
_______________________________________________________________________________
*/
void Abort(char *msg, SOCKET s)
{
	fprintf(stderr,"<CLI> Erro fatal: <%d>\n",WSAGetLastError());

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
