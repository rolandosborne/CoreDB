set -e

echo "enter node: (apps.diatum.net)"
read NODE

echo "enter handle:"
read HANDLE

echo "enter registry: (registry.diatum.net)"
read REGISTRY

echo "enter access token:"
read ACCESS

echo "$NODE : $HANDLE : $REGISTRY : $ACCESS"

# create new account
EMIGO=`curl -f -s -X POST https://$NODE:8443/app/access/emigos?token=$ACCESS`
echo "$EMIGO" | jq .
TOKEN=`echo $EMIGO | jq -r .token | base64 -d | jq -r .token`

# assign registry and handle
curl -f -s -d "{ \"data\" : \"https://$REGISTRY:8443/app\" }" -H "Content-Type: application/json" -X PUT "https://$NODE:8443/app/identity/registry?token=$TOKEN" > /dev/null
curl -f -s -d "{ \"data\" : \"$HANDLE\" }" -H "Content-Type: application/json" -X PUT "https://$NODE:8443/app/identity/handle?token=$TOKEN" > /dev/null

# register account
MSG=`curl -f -s "https://$NODE:8443/app/identity/message?token=$TOKEN"`
curl -f -s -d "$MSG" -H "Content-Type: application/json" -X POST "https://$REGISTRY:8443/app/emigo/messages" > /dev/null
curl -f -s -X PUT "https://$NODE:8443/app/identity/dirty?token=$TOKEN&flag=false" > /dev/null

