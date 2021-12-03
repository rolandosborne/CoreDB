#!/bin/bash
set -e

# env { MYSQL_DATABASE, MYSQL_USER, MYSQL_PASSWORD, MYSQL_HOST }

echo "bind 'set mark-symlinked-directories on'" >> /root/.bashrc

# wait for mysql to start
echo "waiting for mysql conneciton"
while [ true ]; do
  sleep 1;
  ALIVE=`/usr/bin/mysqladmin -u$MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST ping 2> /dev/null || true`
  if [ "$ALIVE" = "mysqld is alive" ]; then
    break;
  fi
done

if [ -f "/data/config/configured" ]; then
  echo "container already configured"
  cat /data/config/configured
else 
  echo "yup, configured" > /data/config/configured
  cat /data/config/configured
  
  chown tomcat:tomcat /var/lib/tomcat9/conf/*
  chown tomcat:tomcat /data/asset
  chown tomcat:tomcat /data/keystore

  echo -e "spring.datasource.url=jdbc:mariadb://$MYSQL_HOST:3306/$MYSQL_DATABASE\nspring.datasource.username=$MYSQL_USER\nspring.datasource.password=$MYSQL_PASSWORD\nspring.datasource.driver-class-name=org.mariadb.jdbc.Driver" > /data/config/config.properties
  echo "$MYSQL_HOST" > /data/config/mysql_host
  echo "$MYSQL_USER" > /data/config/mysql_username
  echo "$MYSQL_PASSWORD" > /data/config/mysql_password
  echo "$MYSQL_DATABASE" > /data/config/mysql_database

  MINOR=`cat /opt/diatum/minor.version`
  MAJOR=`cat /opt/diatum/major.version`

  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST $MYSQL_DATABASE < /opt/diatum/baseline.sql
  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST $MYSQL_DATABASE < /opt/diatum/V01__noop.sql
  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST $MYSQL_DATABASE < /opt/diatum/V02__messaging.sql
  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST $MYSQL_DATABASE < /opt/diatum/V03__portal.sql
  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD -h $MYSQL_HOST $MYSQL_DATABASE -e "insert into config (config_id, str_value) values ('server_version', '$MAJOR.$MINOR.0')"
fi

# drop back to pre-build 9.0.16, multifile supported changed
cd /root/
cp tomcat/bin/* /usr/share/tomcat9/bin/
cp tomcat/lib/* /usr/share/tomcat9/lib/
/usr/share/tomcat9/bin/catalina.sh start

# wait for docker signal
#read line <$pipe;
tail -f /usr/share/tomcat9/logs/catalina.out
