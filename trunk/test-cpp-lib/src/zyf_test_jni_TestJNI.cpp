#include <stdio.h>
#include "zyf_test_jni_TestJNI.h"

JNIEXPORT jstring Java_zyf_test_jni_TestJNI_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	printf("Hello ");
	return js;
}
