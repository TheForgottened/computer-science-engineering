/*============================== Servidor basico UDP ==========================
  Este servidor UDP destina-se a mostrar os conteudos dos datagramas recebidos.
  O porto de escuta encontra-se definido pela constante SERV_UDP_PORT.
  Assume-se que as mensagens recebida sao cadeias de caracteres (ou seja, 
  "strings").
===============================================================================*/

#include <stdio.h>
#include <winsock.h>

#define SERV_UDP_PORT 6000
#define BUFFERSIZE     4096

void Abort(char *msg);

/*________________________________ main ________________________________________
*/

int main( int argc , char *argv[] )
{
	SOCKET sockfd;
	int iResult, nbytes, cli1_len, cli2_len;
	struct sockaddr_in serv_addr, cli1_addr, cli2_addr;
	char buffer[BUFFERSIZE];
	WSADATA wsaData;

	/*=============== INICIA OS WINSOCKS ==============*/

	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*============ CRIA O SOCKET PARA RECEPCAO/ENVIO DE DATAGRAMAS UDP ============*/

	if((sockfd = socket( PF_INET , SOCK_DGRAM , 0)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket");

	/*=============== ASSOCIA O SOCKET AO  ENDERECO DE ESCUTA ===============*/

	/*Define que pretende receber datagramas vindos de qualquer interface de 
	rede, no porto pretendido*/

	memset( (char*)&serv_addr , 0, sizeof(serv_addr) );
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /*Host TO Network Long*/
	serv_addr.sin_port = htons(SERV_UDP_PORT);  /*Host TO Network Short*/

	/*Associa o socket ao porto pretendido*/

	if(bind( sockfd , (struct sockaddr *)&serv_addr , sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta");


	/*================ PASSA A ATENDER CLIENTES INTERACTIVAMENTE =============*/

	cli1_len = sizeof(cli1_addr);
	cli2_len = sizeof(cli2_addr);
	
	while(1){

		fprintf(stderr,"<SER1> Esperando datagram...\n");

		nbytes=recvfrom(sockfd , buffer , sizeof(buffer) , 0 , (struct sockaddr*)&cli1_addr , &cli1_len);

		if(nbytes == SOCKET_ERROR) 
			Abort("Erro na recepcao de datagrams");

		nbytes=recvfrom(sockfd , buffer , sizeof(buffer) , 0 , (struct sockaddr*)&cli2_addr , &cli2_len);

		if(nbytes == SOCKET_ERROR) 
			Abort("Erro na recepcao de datagrams");

		buffer[nbytes]='\0'; /*Termina a cadeia de caracteres recebidos com '\0'*/

		if(sendto( sockfd , (char *)&cli1_addr , sizeof(cli1_addr) , 0 , (struct sockaddr*)&cli2_addr , sizeof(cli2_addr) ) == SOCKET_ERROR)
			Abort("SO nao conseguiu confirmar a rececao do datagram");

		printf("\n<SER1> Par formado...\n\n",buffer);
		
	}
	
}

/*________________________________ Abort________________________________________
  Mostra uma mensagem de erro e o c�digo associado ao ultimo erro com Winsocks. 
  Termina a aplicacao com "exit status" a 1 (constante EXIT_FAILURE)
________________________________________________________________________________*/

void Abort(char *msg)
{

	fprintf(stderr,"<SERV>Erro fatal: <%s> (%d)\n",msg, WSAGetLastError());
	exit(EXIT_FAILURE);

}