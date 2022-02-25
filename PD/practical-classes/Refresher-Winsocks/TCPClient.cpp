#include <stdio.h>
#include <winsock.h>

#define GET_TIME "TIME\r\n"
#define MAX_MSG_SIZE 100

#define TIMEOUT 60 //seconds

void main(int argc, char **argv)
{
	SOCKET s;
	WSADATA wsaData;

	int iResult, nbytes;
	struct sockaddr_in serv_addr;
	struct hostent *serverName;
	char response[MAX_MSG_SIZE];
	SYSTEMTIME systemTime;
	long timeout;

	if(argc != 3){
		printf("Usage: %s <time_server_addr> <time_server_port\n", argv[0]);
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

	timeout = TIMEOUT*1000;
	
	if(setsockopt(s, SOL_SOCKET, SO_RCVTIMEO, (char *)&timeout, 
						sizeof(timeout))==SOCKET_ERROR){
		printf("Unable to set timeout (error: %d)!\n", WSAGetLastError());
	}

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = inet_addr(argv[1]);
	serv_addr.sin_port = htons(atoi(argv[2]));

	if(serv_addr.sin_addr.s_addr == INADDR_NONE &&
							strcmp(argv[1], "255.255.255.255")!=0){
		if((serverName = gethostbyname(argv[1])) == NULL){
			printf("Unknown %s host (error: %d)\n", argv[1], WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);
		}

		memcpy(&serv_addr.sin_addr, serverName->h_addr_list[0], serverName->h_length);
		printf("Host %s has ip address %s \n", argv[1], inet_ntoa(serv_addr.sin_addr));
	}

	if(connect(s, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == SOCKET_ERROR){
			printf("Unable to connect to server (error: %d)\n", WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);
	}

	if(send(s, GET_TIME, strlen(GET_TIME), 0) == SOCKET_ERROR){
			printf("Unable to send time request (error: %d)\n", WSAGetLastError());
			closesocket(s);
			getchar();
			exit(1);
	}
	
	if((nbytes = recv(s, response, MAX_MSG_SIZE, 0)) == SOCKET_ERROR){
			printf("Unable to receive response (error: %d)\n", WSAGetLastError());
			if(WSAGetLastError()==WSAETIMEDOUT){
				printf("Timeout has occured\n");
			}
			closesocket(s);
			getchar();
			exit(1);
	}

	response[nbytes] = 0; // '\0'
	closesocket(s);

	printf("Server time is: %s\n", response);

	GetLocalTime(&systemTime);
	printf("Local time is: %d:%d:%d\n", systemTime.wHour, \
		systemTime.wMinute, systemTime.wSecond);

	getchar();
	exit(0);
}

