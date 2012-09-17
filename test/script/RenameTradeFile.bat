@echo off

cd %~dp0

setlocal EnableDelayedExpansion

echo 1. Parse trade file name
echo 2. Generate .LOCK file
set /p OPER_NUM=Select your operation: 
	
echo 0. Without sub folder
echo 1. With sub folder
set /p WITH_SUBDIR=Select your operation:

if "!OPER_NUM!" == "1" (
	echo 0. Without seqNum
	echo 1. With seqNum
	set /p WITH_SEQNUM=Select your operation:
	goto :parseFileName
) else (
	goto :genLockFile
)

set count=0

:parseFileName
if "!WITH_SUBDIR!" == "1" (
	for /r /d %%s in (*) do (
		cd %%s
		call :parse
		cd ..
	)
)

:parse
for %%i in (*) do (
	set fn=%%i
	if "!fn:~-15,1!" == "." (
		if "!WITH_SEQNUM!" == "1" (
			set destName=!count!!fn:~0,-15!
		) else (
			set destName=!fn:~0,-15!
		)
		ren %%i !destName!
		echo Rename %%i to !destName!
		echo. >!destName!.LOCK
		echo Create !destName!.LOCK
		set /a count+=1
	)
)
goto :end

:genLockFile
if "!WITH_SUBDIR!" == "1" (
	for /r /d %%s in (*) do (
		cd %%s
		call :generate
		cd ..
	)
)

:generate
for %%i in (*) do (
	set fn=%%i
	if not "!fn:~-5!" == ".LOCK" (
		echo. >%%i.LOCK
		echo Create %%i.LOCK
	)
)
goto :end

:end

pause
