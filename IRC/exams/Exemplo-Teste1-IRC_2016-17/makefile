CC = gcc $< -o $@ -lws2_32

all: cliente servidor
	@ echo Trabalho concluido xuxu
cliente: cliente.c
	@ $(CC)
	@ echo Cliente compilado!

servidor: servidor.c
	@ $(CC)
	@ echo Servidor compilado!

clean: 
	@ del servidor.exe
	@ del cliente.exe
	@ echo Diretorio limpinho!