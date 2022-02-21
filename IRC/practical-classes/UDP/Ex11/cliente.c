/*=========================== Cliente basico UDP ===============================
Este cliente destina-se a enviar mensagens passadas na linha de comando, sob
a forma de um argumento, para um servidor especifico cuja locacao e' dada
pelas seguintes constantes: SERV_HOST_ADDR (endereco IP) e SERV_UDP_PORT (porto)

O protocolo usado e' o UDP.
==============================================================================*/

#include <winsock.h>
#include <stdio.h>
#include <strings.h>

#define BUFFERSIZE     4096
#define TIMEOUT 10000

void Abort(char *msg);

/*________________________________ main _______________________________________*/

int main( int argc , char *argv[] )
{

	SOCKET sockfd;
	int msg_len, cli_len, iResult, nBytes, SERV_UDP_PORT, serv_check_len, tam_addr;
	struct sockaddr_in serv_addr, cli_addr, serv_check, addr;
	char buffer[BUFFERSIZE];
	WSADATA wsaData;
	char msg[4096], SERV_HOST_ADDR[4096];
	struct timeval timeout = {TIMEOUT, 0};

	/*========================= TESTA A SINTAXE =========================*/

	if(argc != 7){
		fprintf(stderr,"Sintaxe: -msg <mensagem> -ip <ip destino> -port <porta destino>\n",argv[0]);
		exit(EXIT_FAILURE);
	}

	/*========================= GUARDAR ARGUMENTOS =========================*/

	int i = 0;

	for (i = 0; i < 7; i++)
		if (strcmp(argv[i], "-msg") == 0)
			strcpy(msg, argv[++i]);
		else if (strcmp(argv[i], "-ip") == 0)
			strcpy(SERV_HOST_ADDR, argv[++i]);
		else if (strcmp(argv[i], "-port") == 0)
			SERV_UDP_PORT = atoi(argv[++i]);

	/*=============== INICIA OS WINSOCKS ==============*/

	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*=============== CRIA SOCKET PARA ENVIO/RECEPCAO DE DATAGRAMAS ==============*/

	sockfd = socket( PF_INET , SOCK_DGRAM , 0 );
	if(sockfd == INVALID_SOCKET)
		Abort("Impossibilidade de criar socket");

	/*================= PREENCHE ENDERECO DO SERVIDOR ====================*/

	memset( (char*)&serv_addr , 0, sizeof(serv_addr) ); /*Coloca a zero todos os bytes*/
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = inet_addr(SERV_HOST_ADDR); /*IP no formato "dotted decimal" => 32 bits*/
	serv_addr.sin_port = htons(SERV_UDP_PORT); /*Host TO Netowork Short*/

	/*====================== ENVIA MENSAGEM AO SERVIDOR ==================*/

	msg_len = strlen(msg);

	if(sendto( sockfd , msg , msg_len , 0 , (struct sockaddr*)&serv_addr , sizeof(serv_addr) ) == SOCKET_ERROR)
		Abort("SO nao conseguiu aceitar o datagram");

	printf("<CLI> Mensagem enviado ao servidor...\n");

	cli_len = sizeof(cli_addr);

	if(getsockname(sockfd, (struct sockaddr*) &cli_addr, &cli_len) != SOCKET_ERROR)
		printf("Porta local atribuida automaticamente: %i\n", ntohs(cli_addr.sin_port));

	tam_addr = sizeof(addr);
	nBytes = recvfrom(sockfd , (char *)&cli_addr , sizeof(cli_addr) , 0 , (struct sockaddr*) &addr, &tam_addr);

	if(nBytes == SOCKET_ERROR) {
		if (WSAGetLastError() == WSAETIMEDOUT)
			Abort("Timeout!");
		else
			Abort("Erro na recepcao da confirmacao de rececao datagrams");
	}

	if(nBytes != sizeof(cli_addr)) 
    Abort("Mensagem recebida do tipo inesperado"); 

 

	if(strcmp(SERV_HOST_ADDR, inet_ntoa(addr.sin_addr)) == 0 && SERV_UDP_PORT == ntohs(addr.sin_port)) { 
		// mensagem veio do servidor 

		nBytes=sendto(sockfd, (char *) &cli_addr, sizeof(cli_addr), 0 , (struct sockaddr *) &cli_addr, sizeof(cli_addr));  

		if(nBytes==SOCKET_ERROR) 
			Abort("Erro ao enviar o endereço ao par remoto"); 

		printf("Sou o cliente 2\n"); 
		printf("O meu par remoto -> IP: %s, Porto: %d\n",inet_ntoa(cli_addr.sin_addr),ntohs(cli_addr.sin_port));    
	} else { 
		printf("Sou o cliente 1\n"); 
		printf("Par remoto -> IP: %s, Porto: %d \n",   inet_ntoa(addr.sin_addr), ntohs(addr.sin_port)); 
	} 

		buffer[nBytes]='\0';

		printf("<CLI1>Mensagem enviada e confirmada. A mensagem foi {%s}\n", buffer); 

		if (serv_check.sin_port == serv_addr.sin_port && strcmp(inet_ntoa(serv_check.sin_addr), inet_ntoa(serv_addr.sin_addr)) == 0)
			printf("A confirmacao veio do servidor!\n");
		else
			printf("A confirmacao veio de um impostor\n");

		/*========================= FECHA O SOCKET ===========================*/

		closesocket(sockfd);

		exit(EXIT_SUCCESS);
	}

/*________________________________ Abort________________________________________
  Mostra uma mensagem de erro e o c�digo associado ao ultimo erro com Winsocks. 
  Termina a aplicacao com "exit status" a 1 (constante EXIT_FAILURE)
________________________________________________________________________________*/

void Abort(char *msg)
{

	fprintf(stderr,"<CLI1>Erro fatal: <%s> (%d)\n",msg, WSAGetLastError());
	exit(EXIT_FAILURE);

}
