set -e

# create new account
EMIGO=`curl -f -s -X POST https://$1/ssi/access/emigos?token=$2`

#echo "$EMIGO" | jq .
TOKEN=`echo $EMIGO | jq -r .token | base64 -d | jq -r .token`
echo "$TOKEN"

SUBJECT=`curl -f -s -X POST https://$1/ssi/show/subjects?token=$TOKEN`
echo "$SUBJECT"
SUBJECT_ID=`echo $SUBJECT | jq -r .subject.subjectId`

ASSETS=`curl --form file=@vid1.mov "https://$1/ssi/show/subjects/$SUBJECT_ID/assets?token=$TOKEN&transforms=V01"`
echo "$ASSETS" | jq .

while [ true ]; do

  sleep 5

  ENTRY=`curl -f -s https://$1/ssi/show/subjects/$SUBJECT_ID?token=$TOKEN`
  echo "$ENTRY" | jq .

  STATUS=`echo $ENTRY | jq -r .ready`
  if [ "$STATUS" == "true" ]; then
    break;
  fi

done

