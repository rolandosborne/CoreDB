set -e

PORTAL=$1
LOGIN=$2
PASSWORD=$3
PORT=$4

STAT=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
ACCT=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
ACCS=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
CONF=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 32 ; echo ''`
echo "10"

echo "sending device params" >> /home/ubuntu/install.log
PORTAL_TOKEN=`curl -f -s -d "{ \"login\": \"$LOGIN\", \"password\": \"$PASSWORD\", \"port\": $PORT, \"app\": \"ssi\", \"identity\": { \"accountToken\": \"$ACCT\", \"accessToken\": \"$ACCS\", \"statToken\": \"$STAT\", \"confToken\": \"$CONF\" } }" -H "Content-Type: application/json" -X POST "$PORTAL/device" 2> /dev/null`
echo "$PORTAL_TOKEN" > /home/ubuntu/token
echo "$PORTAL" > /home/ubuntu/portal
sudo cp add_challenge.sh /home/ubuntu/
sudo cp remove_challenge.sh /home/ubuntu/
echo "25"

echo "waiting for dns sync" >> /home/ubuntu/install.log
while [ true ]
do
  sleep 2
  ENTRY=`curl "$PORTAL/device?login=${LOGIN}&password=${PASSWORD}" 2> /dev/null`
  STATUS=`echo $ENTRY | jq -r .status`
  if [ "$STATUS" == "synced" ]; then
    echo "75"
    NAME=`echo $ENTRY | jq -r .hostname`
    echo "$NAME" > /home/ubuntu/nodename
    mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_host_port', 'https://$NAME:$PORT/ssi')"
    echo "retreiving certificate" >> /home/ubuntu/install.log
    sudo certbot -d $NAME --agree-tos --register-unsafely-without-email --manual --preferred-challenges dns --manual-auth-hook /home/ubuntu/add_challenge.sh --manual-cleanup-hook /home/ubuntu/remove_challenge.sh --manual-public-ip-logging-ok certonly >> /home/ubuntu/install.log 2> /dev/null
    echo "90"
    break;
  fi
done

mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_stat_token', '$STAT')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_config_token', '$CONF')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_account_token', '$ACCT')"
mysql -u root -proot coredb -e "insert into config (config_id, str_value) values ('server_access_token', '$ACCS')"
echo "100"
