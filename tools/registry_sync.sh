set -e
TOKEN=`mysql -s -u root -proot coredb -e "select str_value from config where config_id='server_account_token'"`
NODE=`mysql -s -u root -proot coredb -e "select str_value from config where config_id='server_host_port'"`
mysql -s -u root -proot coredb -e "select emigo_id from account" | while read line;
do
  echo "ID: $line";
  MSG=`curl -f -s -X PUT "$NODE/admin/accounts/$line/revision?token=$TOKEN" | jq .`
  REGISTRY=`echo "$MSG" | jq -r .data | base64 --decode | jq -r .registry`
  echo "$MSG" > msg.tmp
  curl -f -s -d @msg.tmp -H "Content-Type: application/json" -X POST "$REGISTRY/amigo/messages" > /dev/null
done

echo -e "\e[1;32mSUCCESS\e[0m"


