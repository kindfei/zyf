#!/bin/sh

if [ $# -ne 4 ]
then
    echo "Wrong number of parameters, please try again."
    exit 1
fi

TARGET_HOST=$1
LOGIN_USER=$2
LOGIN_PASS=$3
BUILD_CMD=$4

WORK_HOME=`dirname $0`
WORK_HOME=`(cd "$WORK_HOME"; pwd)`

SOURCE_FILE="\
$WORK_HOME/modules/citi-gpb-gtu/citi-gpb-gtu-ejb/target/citi-gpb-gtu-ejb.ear\
"

TMP_DIR="/tmp"
TARGET_DIR="/opt/PF_WL10.3_Apps/pfConnectivity/10.3/weblogic/ear_deployments"
DEPLOY_CMD="/opt/PF_WL10.3_Apps/scripts/emtDeployApp.ksh"
APP_NAME="pfConnectivity"

svn update $WORK_HOME

cd $WORK_HOME
stexenv . << EOF
./$BUILD_CMD
exit
EOF

cat > $WORK_HOME/deploytmp.sh << EOF
#!/usr/bin/expect
set timeout 300
set prompt "(%|#|\\\\$|>) $";
catch {set prompt \$env(EXPECT_PROMPT)}
`
IFS=',' read -ra FILES <<< "$SOURCE_FILE"
for i in "${FILES[@]}"; do
    echo spawn scp $i $LOGIN_USER@$TARGET_HOST:$TMP_DIR
    echo expect \"Password:\"
    echo send \"$LOGIN_PASS\\\r\"
    echo expect \"100%\"
done
`
spawn ssh $LOGIN_USER@$TARGET_HOST
expect "Password:"
send "$LOGIN_PASS\r"
expect -re \$prompt
send "scsu epbadm\r"
expect "Password:"
send "$LOGIN_PASS\r"
expect "Reason:"
send "deploy\r"
`
IFS=',' read -ra FILES <<< "$SOURCE_FILE"
for i in "${FILES[@]}"; do
    FILE_NAME=${i##*/}
    EXTENSION=${FILE_NAME##*.}
    PACKAGE_NAME=${FILE_NAME%.*}
    echo send \"cp $TMP_DIR/$FILE_NAME $TARGET_DIR\\\r\"
    echo expect -re \\\$prompt
    echo send \"$DEPLOY_CMD $APP_NAME $EXTENSION $PACKAGE_NAME.properties\\\r\"
    echo expect \"Completed the deployment of Application\"
done
`
send "exit\r"
send "exit\r"
exit
EOF

chmod 755 $WORK_HOME/deploytmp.sh
$WORK_HOME/deploytmp.sh
rm $WORK_HOME/deploytmp.sh
