#!/bin/sh

export JAVA_HOME="/opt/java"

export JAVA_OPTS="${JAVA_OPTS} -Dtc.config=cluster-tc-config.xml"
export JAVA_OPTS="${JAVA_OPTS} -Dtc.server.name=server1"

export JAVA_OPTS="${JAVA_OPTS} -Dl2.l1reconnect.enabled=true"
export JAVA_OPTS="${JAVA_OPTS} -Dl2.l1reconnect.timeout.millis=5000"
export JAVA_OPTS="${JAVA_OPTS} -Dl2.nha.tcgroupcomm.reconnect.enabled=true"
export JAVA_OPTS="${JAVA_OPTS} -Dl2.nha.tcgroupcomm.reconnect.timeout=5000"

nohup bin/start-tc-server.sh >> nohup.out 2>&1 &
