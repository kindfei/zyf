@echo off

set JARS=%CD%\conf
for %%j in (%CD%\lib\*.jar) do call .\SetEnv.bat %%j

set %JAVA_HOME%=C:\Program Files\Java\jdk1.5.0_14

"%JAVA_HOME%\bin\java.exe" -cp %JARS% zyf.cr.CallRegisterParser

pause