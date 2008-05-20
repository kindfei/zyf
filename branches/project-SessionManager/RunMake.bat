set MINGWHOME=E:\Tools\MinGW-3.4.2
set PATH=%MINGWHOME%\bin;%PATH%;

cd jni

mingw32-make.exe -f makefile SessionManager.exe

copy /Y SessionManager.exe ..\SessionManager.exe

del SessionManager.exe
del SessionManager.o
del resource.res
del jvm.def
del libjvm.a

pause