#!/bin/bash
APP_DIR=/opt/lm.project/lm.cloud.config.server/
APP_NAME=lm.cloud.config.server

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
  echo 'STOPPING SERVER......'
  kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
  echo 'STOP FAIL! SO KILL SERVER'
  kill -9 $tpid
else
  echo 'STOP SUCCESSFUL'
fi

cd ${APP_DIR}
rm -rf nohup.out
jar uvf ${APP_NAME}.jar config/*
sleep 5
nohup /usr/bigdata/jdk1.8.0_131/bin/java -Xms1024m -Xmx1024m -Xmn256m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/jvmErrorDump -cp dependency/*:${APP_NAME}.jar -Dspring.profiles.active=dev lm.cloud.config.server.App &
sleep 1
tail -f nohup.out

