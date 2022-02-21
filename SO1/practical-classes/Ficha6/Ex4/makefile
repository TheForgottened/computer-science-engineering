CC = gcc
FLAGS = -W -pthread

all: servidor cliente

servidor: servidor.c servidor.h
	@ $(CC) $< -o $@ $(FLAGS)

cliente: cliente.c
	@ $(CC) $< -o $@ $(FLAGS)

clean:
	@ rm servidor cliente -f
	@ rm serv_pipe -f
	@ rm *_w -f
	@ rm *_r -f