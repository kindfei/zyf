#!/bin/sh

################################## SET ENV ##################################
export THREAD_NAMES=\
"[TIBCO EMS Session Dispatcher (292470)]",\
"[tpTaskExecutor-2]"

export KEYWORDS=\
"^.*(Processing swift trade using GFM client|Processing using GFM client).*$",\
"^.*Enter TradeProcessor.Dispatch trades to different TradeCoordinators basing on their type..*$",\
"^.*com.ssmb.gpb.service.handler.notifier.(DomesticTradeNotifier|GPTradeNotifier|RadarTradeNotifier|GPAssetTransferTradeNotifier) kicking off....*$",\
"^.*com.ssmb.gpb.service.handler.notifier.(DomesticTradeNotifier|GPTradeNotifier|RadarTradeNotifier|GPAssetTransferTradeNotifier) end.*$"

export RESULT_FORMAT=\
"total=0-3",\
"GFM=0-1",\
"book trade=1-3",\
"send msg=2-3"

################################## Run ##################################
JAVACMD=/opt/jrockit/6.0_14R27.6.5/bin/java

APP_HOME=`dirname $0`
APP_HOME=`(cd "$APP_HOME"; pwd)`

cp="$APP_HOME"/conf

for j in $(ls "$APP_HOME"/*.jar)
do
    cp=$cp:$j
done

nohup $JAVACMD -cp "$cp" -DAPP_HOME="$APP_HOME" enfi.tool.perfanal.LogAnalyzer $1 > "$APP_HOME"/nohup.out 2>&1 &

