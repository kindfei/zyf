#!/bin/sh

TMP_CP="conf"

for a in lib/*; do
	TMP_CP="$TMP_CP":"$a";
done

for a in jar/*; do
	TMP_CP="$TMP_CP":"$a";
done

java -cp $TMP_CP test.fx.TestCluster shutdown

