set -e

echo "creating new services..."
SRV=`curl -f -s -H "Accept: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services?token=asdfasdf&handle=AwesomeNewApp&password=pass1"`
SRV_MSG=`echo $SRV | jq '.message'`
SRV_TOK=`echo $SRV | jq -r '.token'`
SRV_ID=`echo $SRV | jq -r '.emigo.emigoId'`
APP=`curl -f -s -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services?token=asdfasdf&handle=AwesomeDupApp&password=pass2"`
APP_MSG=`echo $APP | jq '.message'`
APP_TOK=`echo $APP | jq -r '.token'`
APP_ID=`echo $APP | jq -r '.emigo.emigoId'`

echo "creating new account..."
LNK=`curl -f -s -d '{ "enableShow": true }' -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services/created?token=$SRV_TOK&handle=NewAccount&password=pass"`
ACT=`curl -f -s -d "$LNK" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/accounts/created?token=asdf"`
ACT_MSG=`echo $ACT | jq '.message'`
ACT_TOK=`echo $ACT | jq -r '.token'`
ACT_ID=`echo $ACT | jq -r '.emigo.emigoId'`

echo "linking account to primary service..."
USR=`curl -f -s -d "$ACT" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services/tokens?id=$SRV_ID&token=$SRV_TOK"`

echo "linking account to secondary service..."
APM=`curl -f -s -d '{ "enableShow": true }' -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services/attached?token=$APP_TOK&emigoId=$ACT_ID"`
DUP=`curl -f -s -d "$APM" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/accounts/attached?emigoId=$ACT_ID&password=pass"`
USR=`curl -f -s -d "$DUP" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services/tokens?id=$APP_ID&token=$APP_TOK"`

echo "creating emigos..."
for i in 0 1 2
do
  LNK=`curl -f -s -d '{ "enableShow": true }' -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/services/created?token=$SRV_TOK&handle=AnEmigo$i&password=pass"`
  EMG[$i]=`curl -f -s -d "$LNK" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/access/accounts/created?token=asdf" | jq '.'`
  TOK[$i]=`echo ${EMG[$i]} | jq -r '.token'`
  EMG_MSG[$i]=`curl -f -s -d "{ \"data\": \"AnEmigo$i\" }" -H "Content-Type: application/json" -X PUT "https://db-node-0001.coredb.org:8443/app/identity/handle?token=${TOK[$i]}"`
done

echo "adding account emigos..."
for i in 0 1 2
do
  EMG_ENT[$1]=`curl -f -s -d "${EMG_MSG[$i]}" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/index/emigos?id=asdfasdf&token=$ACT_TOK" | jq '.'`
done

echo "deleteing account emigo..."
N1=`curl -f -s "https://db-node-0001.coredb.org:8443/app/index/emigos?id=asdfasdf&token=$ACT_TOK&match=1&limit=10&offset=0" | jq length`
if [ $N1 -ne 1 ]
then
  exit -1
fi
EID=`echo ${EMG[1]} | jq -r '.emigo.emigoId'`
curl -f -s -X DELETE "https://db-node-0001.coredb.org:8443/app/index/emigos/$EID?id=asdfasdf&token=$ACT_TOK"
N2=`curl -f -s "https://db-node-0001.coredb.org:8443/app/index/emigos?id=asdfasdf&token=$ACT_TOK&match=1&limit=10&offset=0" | jq length`
if [ $N2 -eq 1 ]
then
  exit -1
fi

echo "adding topics..."
for i in 0 1 2 3 4 5 6 7 8 9 10 
do
  TOP_MSG[$i]=`curl -f -s -d "{ \"name\" : \"ATOPIC-$i\", \"description\" : \"a test\" }" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/catalog/topic?id=asdfasdf&token=$ACT_TOK"`
  TOP_ENT[$i]=`curl -f -s -d ${TOP_MSG[$i]} -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/catalog/topics?id=asdfasdf&token=$ACT_TOK"`
done
#curl "https://db-node-0001.coredb.org:8443/app/catalog/topics?id=asdfasdf&token=$ACT_TOK&match=AT&offset=2&limit=8"
TOP=`curl -f -s "https://db-node-0001.coredb.org:8443/app/catalog/topics?id=asdfasdf&token=$ACT_TOK&match=AT&offset=2&limit=8"`
N3=`echo $TOP | jq length`
if [ $N3 -ne 8 ]
then
  exit -1
fi
TNM=`echo $TOP | jq '.[1].topic.name'`
TID=`echo $TOP | jq -r '.[1].topicId'`
if [ $TNM == "ATOPIC-2" ]
then
  exit -1
fi

echo "adding labels..."
EMG=`curl -f -s "https://db-node-0001.coredb.org:8443/app/index/emigos?id=asdfasdf&token=$ACT_TOK&offset=0&limit=1" | jq -r '.[0].emigo.emigoId'`
sleep 2
for i in 0 1 2 3 4 5
do
  LAB[$i]=`curl -f -s -d "{ \"name\" : \"Label-$i\" }" -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/group/labels?handle=asdfasdf&token=$ACT_TOK" | jq -r '.uuid'`
  LAD[$i]=`curl -f -s -X POST "https://db-node-0001.coredb.org:8443/app/index/labels/${LAB[$i]}/emigos/$EMG?handle=asdfasdf&token=$ACT_TOK"`
done
for i in 0 1 2 3
do
  curl -f -s -X DELETE "https://db-node-0001.coredb.org:8443/app/index/labels/${LAB[$i]}/emigos/$EMG?handle=asdfasdf&token=$ACT_TOK"
done
LBL=`curl -f -s "https://db-node-0001.coredb.org:8443/app/index/emigos?id=asdfasdf&token=$ACT_TOK&offset=0&limit=1" | jq -r '.[0].data.labels[0].label.name'`
if [ $LBL != "Label-4" ]
then
  exit -1
fi

echo "adding prompts..."
for i in 0 1
do
  PRT[$i]=`curl -f -s -d '{ "name" : "PROMPTNAME", "question" : { "text" : "What is the meaning of life?" } }' -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/prompt/questions?token=$ACT_TOK" | jq -r '.promptId'`
  ANS[$i]=`curl -f -s -d '{ "text" : "41" }' -H "Content-Type: application/json" -X POST "https://db-node-0001.coredb.org:8443/app/prompt/questions/${PRT[$i]}/answers?token=$ACT_TOK"`
done

echo "adding subjects..."
for i in 0 1 2 3 4 5
do
  SUB[$i]=`curl -f -s -X POST "https://db-node-0001.coredb.org:8443/app/show/subjects?token=$ACT_TOK"` 
  SID[$i]=`echo ${SUB[$i]} | jq -r '.subjectId'`
  AST[$i]=`curl -f -s -F 'file=@img.jpg' =@OST "https://db-node-0001.coredb.org:8443/app/show/subjects/${SID[$i]}/assets?token=$ACT_TOK&authorization=asdf&type=aW1hZ2UvanBlZ"`
  AID[$i]=`echo ${AST[$i]} | jq -r '.assetId'`
  curl -f -s "https://db-node-0001.coredb.org:8443/app/show/subjects/${SID[$i]}/assets/${AID[$i]}?token=$ACT_TOK&authorization=asdf" > asset.out
  diff asset.out img.jpg
done

echo "deleting service..."
curl -f -s -X DELETE "https://db-node-0001.coredb.org:8443/app/service/emigos/$SRV_ID?token=$ACT_TOK"

echo -e "\e[1;32mSUCCESS\e[0m"
