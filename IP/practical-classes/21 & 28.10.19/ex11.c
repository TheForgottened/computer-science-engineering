#include <stdio.h>
void main(void)
{
	printf("i) %i \n", 4 > 8 || 3 <= 5 && 7 != 9);
	printf("ii) %i \n", 4 / 3 + 2 * 18 / 4);
	printf("iii) %i \n", (2 == 1) || (2 == 2));
	printf("iv) %i \n", 2 == (1 || 2) == 2);
	printf("v) %i \n", 2 == 1 || 2 == 2);
}