@echo off

set APP_HOME=%~dp0

set CLASS_PATH=%APP_HOME%conf

for %%i in ("%APP_HOME%jar\*.jar") do call %APP_HOME%setenv.cmd %%i
for %%i in ("%APP_HOME%lib\*.jar") do call %APP_HOME%setenv.cmd %%i

java -cp %CLASS_PATH% -DAPP_HOME=%APP_HOME% enfi.tool.mh.MessageHandler

pause