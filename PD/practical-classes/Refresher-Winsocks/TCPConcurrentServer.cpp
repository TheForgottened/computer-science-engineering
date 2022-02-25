#include <stdio.h>
#include <winsock.h>

#define GET_TIME "TIME"
#define MAX_MSG_SIZE 100

void processClient(LPVOID param);

void main(int argc, char **argv)
{
	SOCKET s, cliSocket;
	WSADATA wsaData;
	int iResult, len;
	struct sockaddr_in serv_addr, cli_addr;
	SECURITY_ATTRIBUTES sa;
	DWORD thread_id;

	if(argc != 2){
		printf("Usage: %s  <time_server_port>\n", argv[0]);
		getchar();
		exit(1);
	}

	// Initialize Winsock
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	if((s = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) == SOCKET_ERROR){
		printf("Unable to create socket (error: %d)!\n", WSAGetLastError());
		getchar();
		exit(1);
	}

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	serv_addr.sin_port = htons(atoi(argv[1]));

	if(bind(s, (struct sockaddr *)&serv_addr, sizeof(serv_addr))==SOCKET_ERROR){
		printf("Unable to bind socket to port %s (error: %d)\n", argv[1], WSAGetLastError());
		closesocket(s);
		getchar();
		exit(1);
	}

	listen(s, 5);

	while(1){

		printf("Waiting for a time request...\n");

		len = sizeof(cli_addr);

		if((cliSocket = accept(s, (struct sockaddr *)&cli_addr, &len))==SOCKET_ERROR){
			printf("Unable to accept new connection (error: %d)\n", WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);
		}

		printf("Connection from %s:%d accepted\n", inet_ntoa(cli_addr.sin_addr),
					ntohs(cli_addr.sin_port));

		
		sa.nLength=sizeof(sa);
		sa.lpSecurityDescriptor=NULL;

		if(CreateThread(&sa,0 ,(LPTHREAD_START_ROUTINE)processClient, (LPVOID)cliSocket, (DWORD)0, &thread_id)==NULL){
			printf("Cannot start new slave thread (error: %d)\n", GetLastError());			
			closesocket(cliSocket);
		}

		printf("New slave thread started (id: %d)\n", thread_id);			
	}
}

void processClient(LPVOID param)
{
	SOCKET s;
	int nbytes, len;
	struct sockaddr_in cli_addr;
	char request[MAX_MSG_SIZE], response[MAX_MSG_SIZE];
	SYSTEMTIME systemTime;

	s = (SOCKET)param;

	if((nbytes = recv(s, request, MAX_MSG_SIZE, 0)) == SOCKET_ERROR){

		printf("Unable to receive request (error: %d)\n", WSAGetLastError());
		closesocket(s);
		return;
	}

	request[nbytes] = 0;
	if(strstr(request, GET_TIME)==NULL){
		printf("Unexpected request \"%s\" will be ignored\n", request);
		closesocket(s);
		return;
	}

	GetLocalTime(&systemTime);
	sprintf(response, "%d:%d:%d\r\n", systemTime.wHour,
						systemTime.wMinute, systemTime.wSecond);

	if(send(s, response, strlen(response), 0) == SOCKET_ERROR){
		printf("Unable to send reponse (error: %d)\n", WSAGetLastError());
		closesocket(s);
		return;
	}

	len = sizeof(cli_addr);
	getpeername(s, (struct sockaddr *)&cli_addr, &len);
	printf("\"%s\" sent to %s:%d\n", response, inet_ntoa(cli_addr.sin_addr),
					ntohs(cli_addr.sin_port));
	
	closesocket(s);
}
