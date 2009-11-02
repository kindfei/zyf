int test1(){
  return 88661;
}

int test2(char * in){
  printf("%s\n",in);
  return 88662;
}

int test3(char * out){
  *out++ = 'w';
  *out++ = 'a';
  *out++ = 'h';
  *out++ = 'a';
  *out++ = 'h';
  *out++= 'a';
  *out = '\0';
  return 88663;
}

int test4(int * i){
  *i = 4;
  return 88664;
}
