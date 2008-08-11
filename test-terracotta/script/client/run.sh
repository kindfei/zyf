#!/bin/sh

export TC_OPTS="${TC_OPTS} -Dtc.install-root=${TC_HOME}"
# export TC_OPTS="${TC_OPTS} -Dtc.config=${TC_HOME}\tc-config.xml"
export TC_OPTS="${TC_OPTS} -Dtc.config=10.4.6.218:9510,192.168.0.10:9510"
export TC_OPTS="${TC_OPTS} -Xbootclasspath/p:${TC_HOME}\dso-boot-hotspot_win32_160_04.jar"
export TC_OPTS="${TC_OPTS} -DnodeId=TestCluster-1"

java ${TC_OPTS} test.TestCluster

