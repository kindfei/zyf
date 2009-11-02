#include <stdio.h>
#include <stdlib.h>

int main(void) {
	puts("Hello World");

	char * in, out;
	int * i_p;

	tt(in, out, i_p);

	printf("%s=%s\n", "out", out);
	printf(i_p);

	return EXIT_SUCCESS;
}

int tt(char* in, char* out, int* i_p) {
	printf("char*in=%s\n", in);
	*out = 'a';
	*i_p = 10;

	return 0;
}

