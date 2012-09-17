echo off

set /p NEW_USERNAME=Enter new username: 
set /p NEW_PASSWORD=Enter new password: 

set DOMAIN_HOME=%~dp0
set SERVER_NAME=AdminServer

set BOOT_FILE=%DOMAIN_HOME%servers\%SERVER_NAME%\security\boot.properties

set TMP_FILE=%DOMAIN_HOME%tmp.txt

rem set env
call %DOMAIN_HOME%bin\setDomainEnv.cmd

rem generate DefaultAuthenticatorInit.ldift
java weblogic.security.utils.AdminAccount %NEW_USERNAME% %NEW_PASSWORD% %DOMAIN_HOME%\security

rem generate encrypted username
java -Dweblogic.RootDirectory=%DOMAIN_HOME% weblogic.security.Encrypt %NEW_USERNAME% > %TMP_FILE%
set /p ENCRYPTED_USERNAME= < %TMP_FILE%

rem generate encrypted password
java -Dweblogic.RootDirectory=%DOMAIN_HOME% weblogic.security.Encrypt %NEW_PASSWORD% > %TMP_FILE%
set /p ENCRYPTED_PASSWORD= < %TMP_FILE%

rem delete tmp file
del /Q %TMP_FILE%

rem create boot.properties
echo username=%ENCRYPTED_USERNAME% > %BOOT_FILE%
echo password=%ENCRYPTED_PASSWORD% >> %BOOT_FILE%

rem clear domain
rmdir /Q /S %DOMAIN_HOME%\servers\domain_bak >nul 2>nul
rmdir /Q /S %DOMAIN_HOME%\servers\%SERVER_NAME%\cache >nul 2>nul
rmdir /Q /S %DOMAIN_HOME%\servers\%SERVER_NAME%\data >nul 2>nul
rmdir /Q /S %DOMAIN_HOME%\servers\%SERVER_NAME%\logs >nul 2>nul
rmdir /Q /S %DOMAIN_HOME%\servers\%SERVER_NAME%\tmp >nul 2>nul

@echo The password has been reseted.

pause