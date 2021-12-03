set -e

echo "enter node: (apps.diatum.net)"
read NODE

echo "enter handle:"
read HANDLE

echo "enter access token:"
read ACCESS

echo "$NODE : $HANDLE : $ACCESS"

# create new account
EMIGO=`curl -f -s -X POST https://$NODE/ssi/access/emigos?token=$ACCESS`
echo "$EMIGO" | jq -r .token | base64 -d
TOKEN=`echo $EMIGO | jq -r .token | base64 -d | jq -r .token`
REGISTRY=`echo $EMIGO | jq -r .emigo.data | base64 -d | jq -r .registry`

# assign registry and handle
curl -f -s -d "$HANDLE" -H "Content-Type: application/json" -X PUT "https://$NODE/ssi/identity/handle?token=$TOKEN" > /dev/null

# register account
MSG=`curl -f -s "https://$NODE/ssi/identity/message?token=$TOKEN"`
curl -f -s -d "$MSG" -H "Content-Type: application/json" -X POST "$REGISTRY/emigo/messages" > /dev/null 
curl -f -s -X PUT "https://$NODE/ssi/identity/dirty?token=$TOKEN&flag=false" > /dev/null

echo "SUCCESS"
