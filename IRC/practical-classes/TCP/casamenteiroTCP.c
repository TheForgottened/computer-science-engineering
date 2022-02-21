/*_______________________________casamenteiroTCP.C___________________________________*/
/*============================= Servidor concorrente TCP  =========================
Este servidor destina-se a colocar em contacto pares de clientes.
Forma os pares de forma sucessiva.
Em cada par, sempre que recebe um byte num dos sockets, reencaminha-o para o outro.
===================================================================================
*/

#include <stdio.h>
#include <stdlib.h>
#include <winsock.h>

#define TIMEOUT		3   //segundos
#define BUFFERSIZE	4096
#define MSG_BOAS_VINDAS "Servidor Casamenteiro\r\nAguarde...\r\n"
#define MSG_ARRANQUE_CONVERSA "Pode iniciar conversa com par...\r\n"

struct ParSockets{
	SOCKET s1;
	SOCKET s2;
};

void AtendeCliente(LPVOID param);
int exchange(SOCKET s1, SOCKET s2);
void Abort(char *msg, SOCKET s);

/*________________________________ main ________________________________________
*/
int main(int argc,char *argv[]){

	SOCKET sock = INVALID_SOCKET, newSock = INVALID_SOCKET;
	int iResult;
	int cliaddr_len;
	struct sockaddr_in cli_addr, serv_addr;
	WSADATA wsaData;
	SECURITY_ATTRIBUTES sa;
	DWORD thread_id;
	SOCKET parSockets[2];
	int contador;
	struct ParSockets *parametrosThreadAtendeCliente;

	if(argc!=2){
		fprintf(stderr, "Usage: %s <porto de escuta>\n",argv[0]); 
		exit(EXIT_SUCCESS);
	}

	/*=============== INICIA OS WINSOCKS ==============*/
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*================== ABRE SOCKET PARA ESCUTA DE CLIENTES ================*/
	if((sock=socket(PF_INET,SOCK_STREAM,IPPROTO_TCP)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket", sock);

	/*=================== PREENCHE ENDERECO DE ESCUTA =======================*/
	memset((char*)&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family=AF_INET;
	serv_addr.sin_addr.s_addr=htonl(INADDR_ANY);  /*Recebe de qq interface*/
	serv_addr.sin_port=htons(atoi(argv[1]));  /*Escuta no porto Well-Known*/

	/*====================== REGISTA-SE PARA ESCUTA =========================*/
	if(bind(sock,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta", sock);

	/*============ AVISA QUE ESTA PRONTO A ACEITAR PEDIDOS ==================*/
	if(listen(sock,5) == SOCKET_ERROR)
		Abort("Impossibilidade de escutar pedidos", sock);

	printf("<SER> Servidor casamenteiro pronto no porto de escuta: %s\n", argv[1]);

	/*========== PASSA A ATENDER CLIENTES DE FORMA CONCORRENTE  =============*/
	cliaddr_len=sizeof(cli_addr);
	contador = 0;
	while(1){
		/*====================== ATENDE PEDIDO ========================*/
		if((newSock=accept(sock,(struct sockaddr *)&cli_addr,&cliaddr_len)) == SOCKET_ERROR){

			if(WSAGetLastError() == WSAEINTR)
				continue;

			fprintf(stderr,"<SERV> Impossibilidade de aceitar cliente...\n");

		}else{

			printf("<SER> Novo cliente conectado: <%s:%d>.\n", inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));

			send(newSock, MSG_BOAS_VINDAS, strlen(MSG_BOAS_VINDAS), 0);

			parSockets[contador++] = newSock;

			if(contador == 2){

				contador = 0;

				parametrosThreadAtendeCliente = (struct ParSockets *)malloc(sizeof(struct ParSockets));

				if(parametrosThreadAtendeCliente == NULL){

					printf("<SER> Nao foi possivel reservar espaco para passar parametros\n!");			
					closesocket(parSockets[0]); closesocket(parSockets[1]);
				
				}else{

					parametrosThreadAtendeCliente->s1 = parSockets[0];
					parametrosThreadAtendeCliente->s2 = parSockets[1];

					sa.nLength=sizeof(sa);
					sa.lpSecurityDescriptor=NULL;

					if(CreateThread(&sa,0 ,(LPTHREAD_START_ROUTINE)AtendeCliente, (LPVOID)parametrosThreadAtendeCliente, (DWORD)0, &thread_id)==NULL){
						printf("<SER> Nao foi possivel iniciar uma nova thread (error: %d)!\n", GetLastError());			
						printf("<SER> O par actual nao sera' atendido!\n");
						closesocket(parSockets[0]); closesocket(parSockets[1]);
					}

					printf("<SER> Um novo par acaba de ser formado.\n");

				}
			}
		}
	}
}

/*___________________________ AtendeCliente ____________________________________
Atende cliente.
______________________________________________________________________________*/

void AtendeCliente(LPVOID param){
	SOCKET sockCli1, sockCli2;
	fd_set fdread, fdtemp;
	struct ParSockets *p;
	int i;
	//struct timeval timeout = {TIMEOUT, 0};	

	//Obtem parametros (sockets para os elementos do par)
	p = (struct ParSockets *)param;
	sockCli1 = p->s1;
	sockCli2 = p->s2;
	free(p);

	send(sockCli1, MSG_ARRANQUE_CONVERSA, strlen(MSG_ARRANQUE_CONVERSA), 0);
	send(sockCli2, MSG_ARRANQUE_CONVERSA, strlen(MSG_ARRANQUE_CONVERSA), 0);

	FD_ZERO(&fdread);
	FD_SET(sockCli1, &fdread);
	FD_SET(sockCli2, &fdread);

	while(1){
		/*==================== PROCESSA PEDIDO ==========================*/

		fdtemp=fdread;
		//timeout.tv_sec=TIMEOUT;
		//timeout.tv_usec=0;

		switch(select(32, &fdtemp, NULL, NULL, NULL)){ // Sem timeout
		//switch(select(32, &fdtemp, NULL, NULL, &timeout)){
			case SOCKET_ERROR:
				if(WSAGetLastError()==WSAEINTR)
					break;

				fprintf(stderr,"<SER_%d> Erro na rotina select (%d) ...\n", GetCurrentThreadId(), WSAGetLastError());
				closesocket(sockCli1); closesocket(sockCli2);
				return;

			case  0:printf(".");
				break;

			default:
				if(FD_ISSET(sockCli1, &fdtemp)){
					if(exchange(sockCli1, sockCli2) <= 0){
						closesocket(sockCli1); closesocket(sockCli2);
						return;
					}
				}

				if(FD_ISSET(sockCli2, &fdtemp)){
					if(exchange(sockCli2, sockCli1) <= 0){
						closesocket(sockCli2); closesocket(sockCli1);
						return;
					}
				}

				break;
		} //switch
	} //while
}

/*_____________________________ exchange _______________________________________
Recebe um caractere do primeiro socket e escreve-o no segundo

Devolve:
	SOCKET_ERROR : se houve erro
			   0 : EOF
	        >= 0 : se leu algum byte
______________________________________________________________________________*/
int exchange(SOCKET s1, SOCKET s2)
{
	int result;
	char c;

	if((result=recv(s1, &c, sizeof(char), 0))==sizeof(char))
		result=send(s2, &c, sizeof(char), 0);

	if(result==0)
		fprintf(stderr, "<SER_%d> Connection closed by foreign host\n", GetCurrentThreadId());

	if(result == SOCKET_ERROR){
		fprintf(stderr, "<SER_%d> Erro no acesso para I/O a um dos sockets (%d)\n", GetCurrentThreadId(), WSAGetLastError());	
	}

	return result;
}

/*________________________________ Abort________________________________________
Mostra a mensagem de erro associada ao ultimo erro dos Winsock e abandona com 
"exit status" a 1
_______________________________________________________________________________
*/
void Abort(char *msg, SOCKET s)
{
	fprintf(stderr,"\a<SER_%d> Erro fatal: <%d>\n", WSAGetLastError(), GetCurrentThreadId());
	
	if(s != INVALID_SOCKET)
		closesocket(s);

	exit(EXIT_FAILURE);
}
