#!/bin/sh

if [ $# -eq 1 ]; then
	BUILD_CMD=$1
elif [ $# -eq 3 ]; then
	TARGET_HOST=$1
	LOGIN_USER=$2
	LOGIN_PASS=$3
elif [ $# -eq 4 ]; then
	TARGET_HOST=$1
	LOGIN_USER=$2
	LOGIN_PASS=$3
	BUILD_CMD=$4
else
    echo "Wrong number of parameters, please try again."
    exit 1
fi

WORK_HOME=`dirname $0`
WORK_HOME=`(cd "$WORK_HOME"; pwd)`

SOURCE_FILE="\
$WORK_HOME/modules/citi-gpb-gtu/citi-gpb-gtu-ejb/build/dist/citi-gpb-gtu-ejb.ear\
"

TMP_DIR="/tmp"
TARGET_DIR="/opt/pfApps/pfConnectivity/10.3.x/weblogic/ear_deployments"
DEPLOY_CMD="/opt/pfApps/scripts/emtDeployApp.ksh"
APP_NAME="pfConnectivity"

if [ -n "$BUILD_CMD" ]; then
svn update $WORK_HOME

cd $WORK_HOME
stexenv . << EOF
./$BUILD_CMD
exit
EOF
fi

if [ -n "$TARGET_HOST" -a -n "$LOGIN_USER" -a -n "$LOGIN_PASS" ]; then
expect -c "
set timeout 300
set prompt \"(%|#|\\\\$|>) $\";
catch {set prompt \$env(EXPECT_PROMPT)}
`
IFS=',' read -ra FILES <<< \"$SOURCE_FILE\"
for i in \"${FILES[@]}\"; do
    echo spawn scp $i $LOGIN_USER@$TARGET_HOST:$TMP_DIR
    echo expect \\"Password:\\"
    echo send \\"$LOGIN_PASS\\\r\\"
    echo expect \\"100%\\"
done
`
spawn ssh $LOGIN_USER@$TARGET_HOST
expect \"Password:\"
send \"$LOGIN_PASS\r\"
expect -re \$prompt
send \"scsu epbadm\r\"
expect \"Password:\"
send \"$LOGIN_PASS\r\"
expect \"Reason:\"
send \"deploy\r\"
`
IFS=',' read -ra FILES <<< \"$SOURCE_FILE\"
for i in \"${FILES[@]}\"; do
    FILE_NAME=${i##*/}
    EXTENSION=${FILE_NAME##*.}
    PACKAGE_NAME=${FILE_NAME%.*}
    echo send \\"cp $TMP_DIR/$FILE_NAME $TARGET_DIR\\\r\\"
    echo expect -re \\\$prompt
    echo send \\"$DEPLOY_CMD $APP_NAME $EXTENSION $PACKAGE_NAME.properties\\\r\\"
    echo expect \\"Completed the deployment of Application\\"
done
`
send \"exit\r\"
send \"exit\r\"
exit
"
fi
