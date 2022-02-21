CC = gcc
FLAGS = -lws2_32
PROGRAMS = $(patsubst %.c, %, $(wildcard ./*.c))

jogos: $(PROGRAMS)

%: %.c
	@ $(CC) $< -o $@ $(FLAGS)
	@ echo "Programa $@ compilado."

clean: 
	@echo rm *.exe -f