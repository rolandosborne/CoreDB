set -e

# create new account
EMIGO=`curl -f -s -X POST https://$1/ssi/access/emigos?token=$2`
echo "$EMIGO" | jq .

TOKEN=`echo $EMIGO | jq -r .token | base64 -d | jq -r .token`
echo "$TOKEN"

