#include <jni.h>
#include <windows.h>
#include <stdio.h>

#define JRE_KEY "Software\\JavaSoft\\Java Runtime Environment"
#define MAXPATHLEN MAX_PATH

typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, void **env, void *args);
typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);

#define DFP_GET_VERSION 0x00074080
#define DFP_SEND_DRIVE_COMMAND 0x0007c084
#define DFP_RECEIVE_DRIVE_DATA 0x0007c088

typedef struct _GETVERSIONOUTPARAMS {
	BYTE bVersion;  // Binary driver version.
	BYTE bRevision;  // Binary driver revision.
	BYTE bReserved;  // Not used.
	BYTE bIDEDeviceMap; // Bit map of IDE devices.
	DWORD fCapabilities; // Bit mask of driver capabilities.
	DWORD dwReserved[4]; // For future use.
} GETVERSIONOUTPARAMS, *PGETVERSIONOUTPARAMS, *LPGETVERSIONOUTPARAMS;

typedef struct _IDEREGS { 
	BYTE bFeaturesReg;  // Used for specifying SMART "commands". 
	BYTE bSectorCountReg; // IDE sector count register 
	BYTE bSectorNumberReg; // IDE sector number register 
	BYTE bCylLowReg;   // IDE low order cylinder value 
	BYTE bCylHighReg;  // IDE high order cylinder value 
	BYTE bDriveHeadReg;  // IDE drive/head register 
	BYTE bCommandReg;  // Actual IDE command. 
	BYTE bReserved;   // reserved for future use.  Must be zero. 
} IDEREGS, *PIDEREGS, *LPIDEREGS; 

typedef struct _SENDCMDINPARAMS { 
	DWORD cBufferSize;  // Buffer size in bytes 
	IDEREGS irDriveRegs;  // Structure with drive register values. 
	BYTE bDriveNumber;  // Physical drive number to send 
	    // command to (0,1,2,3). 
	BYTE bReserved[3];  // Reserved for future expansion. 
	DWORD dwReserved[4];  // For future use. 
	//BYTE  bBuffer[1];   // Input buffer. 
} SENDCMDINPARAMS, *PSENDCMDINPARAMS, *LPSENDCMDINPARAMS; 

typedef struct _DRIVERSTATUS { 
	BYTE bDriverError;  // Error code from driver, 
	    // or 0 if no error. 
	BYTE bIDEStatus;   // Contents of IDE Error register. 
	    // Only valid when bDriverError 
	    // is SMART_IDE_ERROR. 
	BYTE bReserved[2];  // Reserved for future expansion. 
	DWORD dwReserved[2];  // Reserved for future expansion. 
} DRIVERSTATUS, *PDRIVERSTATUS, *LPDRIVERSTATUS; 

typedef struct _SENDCMDOUTPARAMS { 
	DWORD    cBufferSize;  // Size of bBuffer in bytes 
	DRIVERSTATUS DriverStatus;  // Driver status structure. 
	BYTE   bBuffer[512];   // Buffer of arbitrary length 
	      // in which to store the data read from the drive. 
} SENDCMDOUTPARAMS, *PSENDCMDOUTPARAMS, *LPSENDCMDOUTPARAMS; 

typedef struct _IDSECTOR { 
	USHORT wGenConfig; 
	USHORT wNumCyls; 
	USHORT wReserved; 
	USHORT wNumHeads; 
	USHORT wBytesPerTrack; 
	USHORT wBytesPerSector; 
	USHORT wSectorsPerTrack; 
	USHORT wVendorUnique[3]; 
	CHAR sSerialNumber[20]; 
	USHORT wBufferType; 
	USHORT wBufferSize; 
	USHORT wECCSize; 
	CHAR sFirmwareRev[8]; 
	CHAR sModelNumber[40]; 
	USHORT wMoreVendorUnique; 
	USHORT wDoubleWordIO; 
	USHORT wCapabilities; 
	USHORT wReserved1; 
	USHORT wPIOTiming; 
	USHORT wDMATiming; 
	USHORT wBS; 
	USHORT wNumCurrentCyls; 
	USHORT wNumCurrentHeads; 
	USHORT wNumCurrentSectorsPerTrack; 
	ULONG ulCurrentSectorCapacity; 
	USHORT wMultSectorStuff; 
	ULONG ulTotalAddressableSectors; 
	USHORT wSingleWordDMA; 
	USHORT wMultiWordDMA; 
	BYTE bReserved[128]; 
} IDSECTOR, *PIDSECTOR; 

/*+++ 
Global vars 
---*/ 
GETVERSIONOUTPARAMS vers;
SENDCMDINPARAMS in;
SENDCMDOUTPARAMS out;
HANDLE h;
DWORD i;
BYTE j;

void hdid9x(jbyte *key) { 
	ZeroMemory(&vers,sizeof(vers)); 

	//We start in 95/98/Me 
	h=CreateFile("\\\\.\\Smartvsd",0,0,0,CREATE_NEW,0,0); 
	if (!h){ 
		return; 
	} 

	if (!DeviceIoControl(h,DFP_GET_VERSION,0,0,&vers,sizeof(vers),&i,0)){ 
		CloseHandle(h); 
		return; 
	} 

	//If IDE identify command not supported, fails 
	if (!(vers.fCapabilities&1)){
		CloseHandle(h); 
		return; 
	} 
 
	//Identify the IDE drives 
	PIDSECTOR phdinfo; 
	char s[41]; 

	ZeroMemory(&in,sizeof(in)); 
	ZeroMemory(&out,sizeof(out)); 
	
	if (j&1){ 
		in.irDriveRegs.bDriveHeadReg=0xb0; 
	}else{ 
		in.irDriveRegs.bDriveHeadReg=0xa0; 
	} 
	
	if (vers.fCapabilities&(16>>j)){ 
		//We don't detect a ATAPI device. 
		return; 
	}else{ 
		in.irDriveRegs.bCommandReg=0xec; 
	} 
	
	in.bDriveNumber=j; 
	in.irDriveRegs.bSectorCountReg=1; 
	in.irDriveRegs.bSectorNumberReg=1; 
	in.cBufferSize=512; 
	
	if (!DeviceIoControl(h,DFP_RECEIVE_DRIVE_DATA,&in,sizeof(in),&out,sizeof(out),&i,0)){
		CloseHandle(h); 
		return; 
	} 
	
	phdinfo=(PIDSECTOR)out.bBuffer; 
	
	memcpy(s,phdinfo->sSerialNumber,20); 
	s[20]=0; 
	
	int i;
	jbyte *key2 = key;
	byte k = 0xF1;
	for (i=0;i<8;i++) {
		*key2++ = s[i] ^ k;
	}
	
	//Close handle before quit 
	CloseHandle(h);
} 

void hdidnt(jbyte *key) { 
	char hd[80]; 
	PIDSECTOR phdinfo; 
	char s[41]; 

	ZeroMemory(&vers,sizeof(vers)); 
	
	//We start in NT/Win2000 
	sprintf(hd,"\\\\.\\PhysicalDrive%d",j); 
	h=CreateFile(hd,GENERIC_READ|GENERIC_WRITE,FILE_SHARE_READ|FILE_SHARE_WRITE,0,OPEN_EXISTING,0,0); 
	
	if (!h){ 
		return;
	} 
	
	if (!DeviceIoControl(h,DFP_GET_VERSION,0,0,&vers,sizeof(vers),&i,0)){ 
		CloseHandle(h); 
		return;
	} 
	
	//If IDE identify command not supported, fails 
	if (!(vers.fCapabilities&1)){ 
		CloseHandle(h); 
		return; 
	} 
	
	//Identify the IDE drives 
	ZeroMemory(&in,sizeof(in)); 
	ZeroMemory(&out,sizeof(out)); 
	
	if (j&1){ 
		in.irDriveRegs.bDriveHeadReg=0xb0; 
	}else{ 
		in.irDriveRegs.bDriveHeadReg=0xa0; 
	} 
	
	if (vers.fCapabilities&(16>>j)){ 
		//We don't detect a ATAPI device. 
		return; 
	}else{ 
		in.irDriveRegs.bCommandReg=0xec; 
	} 
	
	in.bDriveNumber=j; 
	in.irDriveRegs.bSectorCountReg=1; 
	in.irDriveRegs.bSectorNumberReg=1; 
	in.cBufferSize=512; 
	
	if (!DeviceIoControl(h,DFP_RECEIVE_DRIVE_DATA,&in,sizeof(in),&out,sizeof(out),&i,0)){
		CloseHandle(h); 
		return; 
	} 
	
	phdinfo=(PIDSECTOR)out.bBuffer; 

	memcpy(s,phdinfo->sSerialNumber,20); 
	s[20]=0; 
	
	int i;
	jbyte *key2 = key;
	byte k = 0xF1;
	for (i=0;i<8;i++) {
		*key2++ = s[i] ^ k;
	}

	CloseHandle(h);
}

void getHDSerialNum(jbyte *key){
	OSVERSIONINFO VersionInfo;
	ZeroMemory(&VersionInfo,sizeof(VersionInfo)); 
	VersionInfo.dwOSVersionInfoSize=sizeof(VersionInfo); 
	GetVersionEx(&VersionInfo); 

	switch (VersionInfo.dwPlatformId){ 
		case VER_PLATFORM_WIN32s: 
			return; 
		case VER_PLATFORM_WIN32_WINDOWS: 
			hdid9x(key);
			return;
		case VER_PLATFORM_WIN32_NT: 
			hdidnt(key);
			return;
	}
} 

jboolean GetStringFromRegistry(HKEY key, const char *name, char *buf, jint bufsize) {
	DWORD type, size;

	if (RegQueryValueEx(key, name, 0, &type, 0, &size) == 0 && type == REG_SZ && (size < (unsigned int)bufsize)) {
		if (RegQueryValueEx(key, name, 0, 0, buf, &size) == 0) {
			return JNI_TRUE;
		}
	}
	return JNI_FALSE;
}

jboolean GetJREPath(char *jrepath, char *jvmpath) {
	HKEY key, subkey;
	char version[MAXPATHLEN];
	jint size = MAXPATHLEN;
	
	if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, JRE_KEY, 0, KEY_READ, &key) != 0) {
		fprintf(stderr, "Error opening registry key '" JRE_KEY "'\n");
		return JNI_FALSE;
	}

	if (!GetStringFromRegistry(key, "CurrentVersion", version, sizeof(version))) {
		fprintf(stderr, "Failed reading value of registry key:\n\t" JRE_KEY "\\CurrentVersion\n");
		RegCloseKey(key);
		return JNI_FALSE;
	}
	
	if (RegOpenKeyEx(key, version, 0, KEY_READ, &subkey) != 0) {
		fprintf(stderr, "Error opening registry key '" JRE_KEY "\\%s'\n", version);
		RegCloseKey(key);
		return JNI_FALSE;
	}
	
	if (!GetStringFromRegistry(subkey, "JavaHome", jrepath, size)) {
		fprintf(stderr, "Failed reading value of registry key:\n\t" JRE_KEY "\\%s\\JavaHome\n", version);
		RegCloseKey(key);
		RegCloseKey(subkey);
		return JNI_FALSE;
	}
	
	if (!GetStringFromRegistry(subkey, "RuntimeLib", jvmpath, size)) {
		fprintf(stderr, "Failed reading value of registry key:\n\t" JRE_KEY "\\%s\\RuntimeLib\n", version);
		RegCloseKey(key);
		RegCloseKey(subkey);
		return JNI_FALSE;
	}
	
	RegCloseKey(key);
	RegCloseKey(subkey);
	
	return JNI_TRUE;
}

jboolean InitializeJVM(JavaVM **pvm, JNIEnv **penv) {
	char jrepath[MAXPATHLEN] = "jre";
	char jvmpath[MAXPATHLEN] = "jre\\bin\\client\\jvm.dll";
	
	GetJREPath(jrepath, jvmpath);

	printf("JRE path is %s\n", jrepath);
	printf("JVM path is %s\n", jvmpath);

	(void)strcat(jrepath, "\\bin\\" "msvcr71.dll"); /* Add crt dll */
	
	printf("CRT path is %s\n", jrepath);
	
	if (_access(jrepath, 0) == 0) {
		if (LoadLibrary(jrepath) == 0) {
			fprintf(stderr, "Error loading: %s", jrepath);
			return JNI_FALSE;
		}
	} else {
		printf("msvcr71.dll is not exist\n");
	}

	HINSTANCE handle;
	if ((handle = LoadLibrary(jvmpath)) == 0) {
		fprintf(stderr, "Error loading: %s", jvmpath);
		return JNI_FALSE;
	}
	
	JavaVMOption options[2];
	options[0].optionString = "-Djava.class.path=lib/SessionManager.jar;lib/swt.jar;lib/registry.jar;lib/dom4j-1.6.1.jar;lib/log4j-1.2.11.jar;lib/commons-logging.jar";
	options[1].optionString = "-Djava.library.path=lib";
	
	JavaVMInitArgs args;
	jint r;
	
	memset(&args, 0, sizeof(args));
	args.version  = JNI_VERSION_1_2;
	args.nOptions = 2;
	args.options  = options;
	args.ignoreUnrecognized = JNI_FALSE;
	
	CreateJavaVM_t CreateJavaVM = (void *)GetProcAddress(handle, "JNI_CreateJavaVM");
	GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs = (void *)GetProcAddress(handle, "JNI_GetDefaultJavaVMInitArgs");
	if (CreateJavaVM == 0 || GetDefaultJavaVMInitArgs == 0) {
		fprintf(stderr, "Error: can't find JNI interfaces in: %s", (char *)jvmpath);
		return JNI_FALSE;
	}
	r = CreateJavaVM(pvm, (void **)penv, &args);
	free(options);
	
	return r == JNI_OK;
}

jclass GetClass(JNIEnv *env, char *name) {
	FILE *in;
	long length;
	char *code;
	jclass clz = 0;

	if ((in = fopen("lib/startup.code", "rb")) == NULL) {
		printf("Cannot Open Class File\n");
		clz = (*env)->FindClass(env, name);
		return clz;
	}

	printf("Open Class File Successful\n");

	fseek(in, 0L, SEEK_END);
	length = ftell(in);
	fseek(in, 0, SEEK_SET);

	code = malloc(length);
	fread((void*)code, length, 1, in);
	fclose(in);

	char key = 0xF1;
	char *src = malloc(length);
	char *code2;
	char *src2;
	byte b;
	int i;
	
	code2 = code;
	src2 = src;
	for (i=0; i<length; i++) {
		b = *code2++;
		*src2++ = b ^ key;
	}

	clz = (*env)->DefineClass(env, name, 0, src, length);
	free(code);

	return clz;
}

int main() {
	JNIEnv *env;
	JavaVM *vm;
	jclass mainClass = 0;
	jmethodID mainID = 0;
	
	if (InitializeJVM(&vm, &env)) {
		
		mainClass = GetClass(env, "zyf/sm/Startup");
		
		if(mainClass != NULL) {
			jarray cls = (*env)->FindClass(env, "java/lang/Object");
			jobjectArray mainArgs = (*env)->NewObjectArray(env, 1, cls, 0);
			
			jbyte s[8] = {0x77,0x21,0x3F,0x6E,0x3E,0x3F,0x4F,0x52};
			getHDSerialNum(s);
			jbyteArray ary = (*env)->NewByteArray(env, 8);
			(*env)->SetByteArrayRegion(env, ary, 0, 8, s);
			
			(*env)->SetObjectArrayElement(env, mainArgs, 0, ary);
			(*env)->DeleteLocalRef(env, ary);
			
			mainID = (*env)->GetStaticMethodID(env, mainClass, "cMain", "([Ljava/lang/Object;)V");
			(*env)->CallStaticVoidMethod(env, mainClass, mainID, mainArgs);
		} else {
			printf("mainClass is null\n");
		}
		
		(*vm)->DestroyJavaVM(vm);
		
		return 0;
	} else {
		printf("JNI_CreateJavaVM error\n");
		return -1;
	}
}
