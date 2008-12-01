set HOME_DIR=E:\eclipse\workspace-zyf\test

rmic -classpath %HOME_DIR%\bin -d %HOME_DIR%\src -keep test.basic.rmi.hello.HelloImpl

del %HOME_DIR%\src\test\basic\rmi\hello\HelloImpl_Stub.class

pause