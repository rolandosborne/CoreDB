#!/bin/bash
set -e

echo "bind 'set mark-symlinked-directories on'" >> /root/.bashrc

if [ -f "/data/config/configured" ]; then
  cat /data/config/configured

  chown -R mysql:mysql /var/lib/mysql
  /etc/init.d/mysql start

  # wait for mysql to start
  echo "waiting for mysql conneciton"
  while [ true ]; do
    sleep 1;
    ALIVE=`/usr/bin/mysqladmin -uroot -proot -h localhost ping 2> /dev/null || true`
    if [ "$ALIVE" = "mysqld is alive" ]; then
      break;
    fi
  done
else 
  cp -r /root/mysql/* /var/lib/mysql/

  chown -R mysql:mysql /var/lib/mysql
  /etc/init.d/mysql start

  # wait for mysql to start
  echo "waiting for mysql conneciton"
  while [ true ]; do
    sleep 1;
    ALIVE=`/usr/bin/mysqladmin -uroot -proot -h localhost ping 2> /dev/null || true`
    if [ "$ALIVE" = "mysqld is alive" ]; then
      break;
    fi
  done

  chown tomcat:tomcat /var/lib/tomcat9/conf/*
  chown tomcat:tomcat /data/asset
  chown tomcat:tomcat /data/keystore

  echo -e "spring.datasource.url=jdbc:mariadb://localhost:3306/coredb\nspring.datasource.username=root\nspring.datasource.password=root\nspring.datasource.driver-class-name=org.mariadb.jdbc.Driver" > /data/config/config.properties
  echo "localhost" > /data/config/mysql_host
  echo "root" > /data/config/mysql_username
  echo "root" > /data/config/mysql_password
  echo "coredb" > /data/config/mysql_database

  MINOR=`cat /opt/diatum/minor.version`
  MAJOR=`cat /opt/diatum/major.version`

  mysql -h localhost < /opt/diatum/setup.sql
  mysql -u root -proot -e "create database coredb"
  mysql -u root -proot -h localhost coredb < /opt/diatum/baseline.sql
  mysql -u root -proot -h localhost coredb < /opt/diatum/V01__noop.sql
  mysql -u root -proot -h localhost coredb < /opt/diatum/V02__messaging.sql
  mysql -u root -proot -h localhost coredb < /opt/diatum/V03__portal.sql
  mysql -u root -proot -h localhost coredb -e "insert into config (config_id, str_value) values ('server_version', '$MAJOR.$MINOR.0')"

  echo "configured" > /data/config/configured
fi

# drop back to pre-build 9.0.16, multifile supported changed
cd /root/
cp tomcat/bin/* /usr/share/tomcat9/bin/
cp tomcat/lib/* /usr/share/tomcat9/lib/
/usr/share/tomcat9/bin/catalina.sh start

# wait for docker signal
#read line <$pipe;
tail -f /usr/share/tomcat9/logs/catalina.out
