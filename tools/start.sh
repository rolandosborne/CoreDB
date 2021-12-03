NAME=$1
PASSWORD=$2

echo "USING: $NAME $PASSWORD"

service mysql start

# wait for mysql to start
while [ true ]; do
  sleep 1;
  ALIVE=`/usr/bin/mysqladmin ping`
  if [ "$ALIVE" == "mysqld is alive" ]; then
    break;
  fi
done

mysql < /tmp/setup.sql
mysql -u root -proot -e "create database coredb"
mysql -u root -proot coredb < /tmp/baseline.sql
mysql -u root -proot coredb < /tmp/V01__noop.sql
mysql -u root -proot coredb < /tmp/V02__messaging.sql
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_version', '0.1.48')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('default_registry', 'https://registry.diatum.net/app')"

while [ true ]; do
  sleep 1;
done

