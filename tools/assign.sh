set -e

HOSTNAME=$1
PORT=$2

STAT=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
ACCT=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
ACCS=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
CONF=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`

echo "$HOSTNAME" > /home/ubuntu/nodename
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_host_port', 'https://$HOSTNAME:$PORT/ssi')"

echo "retreiving certificate" >> /home/ubuntu/install.log
sudo certbot certonly --standalone -d $HOSTNAME --agree-tos --no-eff-email --register-unsafely-without-email >> /home/ubuntu/install.log 2> /dev/null

mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_stat_token', '$STAT')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_config_token', '$CONF')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_account_token', '$ACCT')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_access_token', '$ACCS')"
