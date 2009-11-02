#include <stdio.h>
#include <stdlib.h>

int test(char* in, char * out, int* i_p) {
	printf("char*in=%s", in);
	*out++ = 'w';
	*out++ = 'a';
	*out++ = 'h';
	*out++ = 'a';
	*out++ = 'h';
	*out++ = 'a';
	*out = '\0';
	*i_p = 10;
	return 0;
}
