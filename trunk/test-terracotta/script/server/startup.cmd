@echo off

set JAVA_OPTS=

set JAVA_OPTS=%JAVA_OPTS% -Dtc.config=cluster-tc-config.xml
set JAVA_OPTS=%JAVA_OPTS% -Dtc.server.name=server1

set JAVA_OPTS=%JAVA_OPTS% -Dl2.l1reconnect.enabled=true
set JAVA_OPTS=%JAVA_OPTS% -Dl2.l1reconnect.timeout.millis=5000
set JAVA_OPTS=%JAVA_OPTS% -Dl2.nha.tcgroupcomm.reconnect.enabled=true
set JAVA_OPTS=%JAVA_OPTS% -Dl2.nha.tcgroupcomm.reconnect.timeout=5000

bin/start-tc-server.bat
