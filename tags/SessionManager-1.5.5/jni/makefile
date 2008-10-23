MINGWHOME = E:\Tools\MinGW-3.4.2
JAVAHOME = C:\Java\JDK1.6.0_04

#INCS = -I"$(MINGWHOME)\include" -I"$(JAVAHOME)\include" -I"$(JAVAHOME)\include\win32"
#LIBS = "$(JAVAHOME)\lib\jvm.lib" -L"libjvm.a" -L"$(MINGWHOME)\lib" -L"$(JAVAHOME)\jre\bin" -L"$(JAVAHOME)\jre\bin\client"

INCS = -I"$(JAVAHOME)\include" -I"$(JAVAHOME)\include\win32"
LIBS = 

SessionManager.exe: SessionManager.o resource.res
	gcc SessionManager.o resource.res -o "SessionManager.exe" $(LIBS) -mwindows

SessionManager.o: SessionManager.c
	gcc -c SessionManager.c -o SessionManager.o $(INCS)

resource.res: resource.rc
	windres -i resource.rc --input-format=rc -o resource.res -O coff

reimp:
	reimp $(JAVAHOME)\lib\jvm.lib
