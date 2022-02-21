CC = gcc -W -pthread 

all: stereo producer
	@ echo "Compiled successfully!"

stereo: stereo.c
	@ $(CC) $< -o $@

producer: producer.c
	@ $(CC) $< -o $@

clean:
	@ rm stereo -f
	@ rm producer -f
	@ rm p_dir -f
	@ rm p_esq -f