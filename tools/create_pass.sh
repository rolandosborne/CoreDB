if [ "$1" == "" ]; then
  echo "include account token"
  exit 1;
fi

PASS=`curl -f -s -d '{ "enableIdentity": true, "enableShare": true, "enableAccess": true, "enableIndex": true, "enableGroup": true, "enableProfile": true }' -H "Content-Type: application/json" -X POST "https://node.dev.coredb.org:8443/app/access/accounts/tokens?token=$1"`
DATA=`echo "$PASS" | jq -r .data`
echo -e "PASS: $DATA"
