#!/bin/sh

JAVACMD=/opt/jrockit/6.0_14R27.6.5/bin/java

APP_HOME=`dirname $0`
APP_HOME=`(cd "$APP_HOME"; pwd)`

cp="$APP_HOME"/conf

for j in $(ls "$APP_HOME"/jar/*.jar)
do
    cp=$cp:$j
done

for j in $(ls "$APP_HOME"/lib/*.jar)
do
    cp=$cp:$j
done

nohup $JAVACMD -cp "$cp" -DAPP_HOME="$APP_HOME" fei.smtpserver.Bootstrap > "$APP_HOME"/nohup.out 2>&1 &

