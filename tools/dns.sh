set -e


DOMAINS=`curl https://rdns.diatum.net/domains 2> /dev/null`
DOMAINS_LENGTH=`echo "$DOMAINS" | jq length`
for (( i=0; i<$DOMAINS_LENGTH; i++ ))
do
  DOMAIN=`echo "$DOMAINS" | jq -r ".[$i].url"`
  COUNT=`curl $DOMAIN/domain 2> /dev/null`
  if (( $COUNT > 0 )); then
    echo "AVAILABLE!"
  fi
done


