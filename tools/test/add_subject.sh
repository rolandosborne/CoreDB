set -e

SUBJECT=`curl -f -s -X POST $1/show/subjects?token=$2`
echo "$SUBJECT" | jq .
SUBJECT_ID=`echo $SUBJECT | jq -r .subject.subjectId`
exit 1;

while [ true ]; do

  ASSETS=`curl --form file=@vid1.mov "https://$1/ssi/show/subjects/$SUBJECT_ID/assets?token=$TOKEN&transforms=V01,V02,V03,V04,V01,V02"`
  echo "$ASSETS" | jq .

  while [ true ]; do

    sleep 5

    ENTRY=`curl -f -s https://$1/ssi/show/subjects/$SUBJECT_ID?token=$TOKEN`
    echo "$ENTRY" | jq .

    STATUS=`echo $ENTRY | jq -r .status`
    if [ "$STATUS" != "pending" ]; then
      break;
    fi

  done

done
