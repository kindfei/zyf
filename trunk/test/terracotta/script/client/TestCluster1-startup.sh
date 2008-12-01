#!/bin/sh

TMP_CP="conf"

for a in lib/*; do
	TMP_CP="$TMP_CP":"$a";
done

for a in jar/*; do
	TMP_CP="$TMP_CP":"$a";
done

export TC_OPTS="$TC_OPTS -Dtc.install-root=/opt/terracotta-2.6.2"
export TC_OPTS="$TC_OPTS -Dtc.config=conf/cluster-tc-config.xml"
# export TC_OPTS="$TC_OPTS -Dtc.config=10.4.6.217:9510,10.4.6.218:9510"
export TC_OPTS="$TC_OPTS -Xbootclasspath/p:lib/dso-boot-hotspot_linux_160_06.jar"
export TC_OPTS="$TC_OPTS -DnodeId=TestCluster1"

nohup java -cp $TMP_CP $TC_OPTS test.fx.TestCluster startup >> nohup.out 2>&1 &

