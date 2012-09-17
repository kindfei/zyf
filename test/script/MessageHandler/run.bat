@echo off

set APP_HOME=%~dp0

set CLASS_PATH=%APP_HOME%conf

setLocal EnableDelayedExpansion

for %%i in ("%APP_HOME%jar\*.jar") do (
	set CLASS_PATH=!CLASS_PATH!;%%i
)

for %%i in ("%APP_HOME%lib\*.jar") do (
	set CLASS_PATH=!CLASS_PATH!;%%i
)

java -cp %CLASS_PATH% -DAPP_HOME=%APP_HOME% fei.tools.mh.MessageHandler

pause