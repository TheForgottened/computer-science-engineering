#include <stdio.h>
#include <string.h>

void main() {
	char str[1000];
}

void InTheBanks(char string[], int size, char letter) {
	int i, j;

	for (i = 0; i < size; i++) {
		if (string[i] == letter) {
			size++;
			while (i < size)
				string[size - 1] = string[i];
		}
			
	}

}