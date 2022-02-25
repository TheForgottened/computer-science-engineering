#include <stdio.h>
#include <winsock.h>

#define GET_TIME "TIME"
#define MAX_MSG_SIZE 100

void main(int argc, char **argv)
{
	SOCKET s;
	WSADATA wsaData;
	int iResult, nbytes, len;
	struct sockaddr_in serv_addr, cli_addr;
	char request[MAX_MSG_SIZE], response[MAX_MSG_SIZE];
	SYSTEMTIME systemTime;

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

	if((s = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) == SOCKET_ERROR){
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

	while(1){

		printf("Waiting for a time request...\n");

		len = sizeof(cli_addr);
		if((nbytes = recvfrom(s, request, MAX_MSG_SIZE, 0,
					(struct sockaddr *)&cli_addr, &len)) == SOCKET_ERROR){

			printf("Unable to receive request (error: %d)\n", WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);

		}

		request[nbytes] = 0;
		if(strcmp(GET_TIME, request)!=0){
			printf("Unexpected request \"%s\" will be ignored\n", request);
			continue;
		}

		GetLocalTime(&systemTime);
		sprintf(response, "%d:%d:%d", systemTime.wHour,
							systemTime.wMinute, systemTime.wSecond);

		if(sendto(s, response, strlen(response), 0, (struct sockaddr *)&cli_addr, 
							sizeof(cli_addr)) == SOCKET_ERROR){
			printf("Unable to send reponse (error: %d)\n", WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);
		}

		printf("\"%s\" sent to %s:%d\n", response, inet_ntoa(cli_addr.sin_addr),
						ntohs(cli_addr.sin_port));
	}

}

