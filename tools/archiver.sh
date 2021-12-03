set -e

COREDB=$1
SQLDIR=$2

Extract_EmigoRegistry () {
  NAME=`mysql -u root -proot $COREDB -sN -e "select name from emigo_registry where emigo_id='$1'"`
  NAME=${NAME//\'/\\\'}
  if [ "$NAME" != "NULL" ]; then
    NAME="'$NAME'"
  fi
  LOCATION=`mysql -u root -proot $COREDB -sN -e "select location from emigo_registry where emigo_id='$1'"`
  LOCATION=${LOCATION//\'/\\\'}
  if [ "$LOCATION" != "NULL" ]; then
    LOCATION="'$LOCATION'"
  fi
  DESCRIPTION=`mysql -u root -proot $COREDB -sN -e "select description from emigo_registry where emigo_id='$1'"`
  DESCRIPTION=${DESCRIPTION//\'/\\\'}
  if [ "$DESCRIPTION" != "NULL" ]; then
    DESCRIPTION="'$DESCRIPTION'"
  fi
  LOGO=`mysql -u root -proot $COREDB -sN -e "select logo from emigo_registry where emigo_id='$1'"`
  LOGO=${LOGO//\'/\\\'}
  if [ "$LOGO" != "NULL" ]; then
    LOGO="'$LOGO'"
  fi
  REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from emigo_registry where emigo_id='$1'"`
  VERSION=`mysql -u root -proot $COREDB -sN -e "select version from emigo_registry where emigo_id='$1'"`
  VERSION=${VERSION//\'/\\\'}
  if [ "$VERSION" != "NULL" ]; then
    VERSION="'$VERSION'"
  fi
  BLOCKED=`mysql -u root -proot $COREDB -sN -e "select blocked from emigo_registry where emigo_id='$1'"`
  HANDLE=`mysql -u root -proot $COREDB -sN -e "select handle from emigo_registry where emigo_id='$1'"`
  HANDLE=${HANDLE//\'/\\\'}
  if [ "$HANDLE" != "NULL" ]; then
    HANDLE="'$HANDLE'"
  fi
  NODE=`mysql -u root -proot $COREDB -sN -e "select node from emigo_registry where emigo_id='$1'"`
  NODE=${NODE//\'/\\\'}
  if [ "$NODE" != "NULL" ]; then
    NODE="'$NODE'"
  fi
  REGISTRY=`mysql -u root -proot $COREDB -sN -e "select registry from emigo_registry where emigo_id='$1'"`
  REGISTRY=${REGISTRY//\'/\\\'}
  if [ "$REGISTRY" != "NULL" ]; then
    REGISTRY="'$REGISTRY'"
  fi

  echo "call addemigo('$1', $NAME, $LOCATION, $DESCRIPTION, $LOGO, $REVISION, $VERSION, $BLOCKED, $HANDLE, $NODE, $REGISTRY);"
}

Extract_ServiceRegistry () {
  EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo_id from service_registry where id='$1'"`
  EMIGO=${EMIGO//\'/\\\'}
  if [ "$EMIGO" != "NULL" ]; then
    EMIGO="'$EMIGO'"
  fi
  BLOCKED=`mysql -u root -proot $COREDB -sN -e "select blocked from service_registry where id='$1'"`
  ENABLED=`mysql -u root -proot $COREDB -sN -e "select enabled from service_registry where id='$1'"`

  echo "insert ignore into service_registry (emigo_id, blocked, enabled) values ($EMIGO, $BLOCKED, $ENABLED);"
}

Extract_Emigo () {
  mysql -u root -proot $COREDB -sN -e "select id from emigo where account_id=$1" | while read id;
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo_id from emigo where id=$id"`
    PROMPT=`mysql -u root -proot $COREDB -sN -e "select prompt from emigo where id=$id"`
    ACCEPT=`mysql -u root -proot $COREDB -sN -e "select accept from emigo where id=$id"`
    ENABLED=`mysql -u root -proot $COREDB -sN -e "select enabled from emigo where id=$id"`
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from emigo where id='$id'"`
    HIDDEN=`mysql -u root -proot $COREDB -sN -e "select hidden from emigo where id=$id"`
    NOTES=`mysql -u root -proot $COREDB -sN -e "select notes from emigo where id=$id"`
    NOTES=${NOTES//\'/\\\'}
    if [ "$NOTES" != "NULL" ]; then
      NOTES="'$NOTES'"
    fi
    echo "insert into emigo (account_id, emigo_id, prompt, revision, accept, enabled, hidden, notes) select id, '$EMIGO', $PROMPT, $REVISION, $ACCEPT, $ENABLED, $HIDDEN, $NOTES from account where emigo_id='$2';"
  done
}

Extract_Label () {
  mysql -u root -proot $COREDB -sN -e "select id from label where account_id=$1" | while read id;
  do
    LABEL=`mysql -u root -proot $COREDB -sN -e "select label_id from label where id=$id"`
    LABEL=${LABEL//\'/\\\'}
    if [ "$LABEL" != "NULL" ]; then
      LABEL="'$LABEL'"
    fi
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from label where id='$id'"`
    NAME=`mysql -u root -proot $COREDB -sN -e "select name from label where id=$id"`
    NAME=${NAME//\'/\\\'}
    if [ "$NAME" != "NULL" ]; then
      NAME="'$NAME'"
    fi
    LOGO=`mysql -u root -proot $COREDB -sN -e "select logo from label where id=$id"`
    LOGO=${LOGO//\'/\\\'}
    if [ "$LOGO" != "NULL" ]; then
      LOGO="'$LOGO'"
    fi
    DESCRIPTION=`mysql -u root -proot $COREDB -sN -e "select description from label where id=$id"`
    DESCRIPTION=${DESCRIPTION//\'/\\\'}
    if [ "$DESCRIPTION" != "NULL" ]; then
      DESCRIPTION="'$DESCRIPTION'"
    fi
    echo "insert into label (account_id, label_id, revision, name, logo, description) select id, $LABEL, $REVISION, $NAME, $LOGO, $DESCRIPTION from account where emigo_id='$2';"
  done
}

Extract_Config () {
  mysql -u root -proot $COREDB -sN -e "select id from account_config where account_id=$1" | while read id;
  do
    CONFIG=`mysql -u root -proot $COREDB -sN -e "select config_id from account_config where id=$id"`
    CONFIG=${CONFIG//\'/\\\'}
    if [ "$CONFIG" != "NULL" ]; then
      CONFIG="'$CONFIG'"
    fi
    STR=`mysql -u root -proot $COREDB -sN -e "select str_value from account_config where id=$id"`
    STR=${STR//\'/\\\'}
    if [ "$STR" != "NULL" ]; then
      STR="'$STR'"
    fi
    NUM=`mysql -u root -proot $COREDB -sN -e "select num_value from account_config where id=$id"`
    BOOL=`mysql -u root -proot $COREDB -sN -e "select bool_value from account_config where id=$id"`
    echo "insert into account_config (account_id, config_id, str_value, num_value, bool_value) select id, $CONFIG, $STR, $NUM, $BOOL from account where emigo_id='$2';"
  done
}

Extract_RejectEmigo () {
  mysql -u root -proot $COREDB -sN -e "select id from reject_emigo where account_id=$1" | while read id;
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo_id from reject_emigo where id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    echo "insert into reject_emigo (account_id, emigo_id) select id, $EMIGO from account where emigo_id='$2';"
  done
}

Extract_User () {
  mysql -u root -proot $COREDB -sN -e "select id from user where account_id=$1" | while read id;
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo_id from user where id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    ACTTOKEN=`mysql -u root -proot $COREDB -sN -e "select account_token from user where id=$id"`
    ACTTOKEN=${ACTTOKEN//\'/\\\'}
    if [ "$ACTTOKEN" != "NULL" ]; then
      ACTTOKEN="'$ACTTOKEN'"
    fi
    SRVTOKEN=`mysql -u root -proot $COREDB -sN -e "select service_token from user where id=$id"`
    SRVTOKEN=${SRVTOKEN//\'/\\\'}
    if [ "$SRVTOKEN" != "NULL" ]; then
      SRVTOKEN="'$SRVTOKEN'"
    fi
    ENABLED=`mysql -u root -proot $COREDB -sN -e "select enabled from user where id=$id"`
    echo "insert into user (account_id, emigo_id, account_token, service_token, enabled) select id, $EMIGO, $ACTTOKEN, $SRVTOKEN, $ENABLED from account where emigo_id='$2';"
  done
}

Extract_Pass () {
  mysql -u root -proot $COREDB -sN -e "select id from pass where account_id=$1" | while read id;
  do
    PASS=`mysql -u root -proot $COREDB -sN -e "select pass from pass where id=$id"`
    PASS=${PASS//\'/\\\'}
    if [ "$PASS" != "NULL" ]; then
      PASS="'$PASS'"
    fi
    ISSUED=`mysql -u root -proot $COREDB -sN -e "select issued from pass where id=$id"`
    EXPIRES=`mysql -u root -proot $COREDB -sN -e "select expires from pass where id=$id"`
    ACTSHOW=`mysql -u root -proot $COREDB -sN -e "select account_show from pass where id=$id"`
    ACTIDENTITY=`mysql -u root -proot $COREDB -sN -e "select account_identity from pass where id=$id"`
    ACTPROFILE=`mysql -u root -proot $COREDB -sN -e "select account_profile from pass where id=$id"`
    ACTGROUP=`mysql -u root -proot $COREDB -sN -e "select account_group from pass where id=$id"`
    ACTSHARE=`mysql -u root -proot $COREDB -sN -e "select account_share from pass where id=$id"`
    ACTPROMPT=`mysql -u root -proot $COREDB -sN -e "select account_prompt from pass where id=$id"`
    ACTAPP=`mysql -u root -proot $COREDB -sN -e "select account_app from pass where id=$id"`
    ACTINDEX=`mysql -u root -proot $COREDB -sN -e "select account_index from pass where id=$id"`
    ACTCONTACT=`mysql -u root -proot $COREDB -sN -e "select account_contact from pass where id=$id"`
    ACTAGENT=`mysql -u root -proot $COREDB -sN -e "select account_agent from pass where id=$id"`
    ACTUSER=`mysql -u root -proot $COREDB -sN -e "select account_user from pass where id=$id"`
    ACTACCESS=`mysql -u root -proot $COREDB -sN -e "select account_access from pass where id=$id"`
    ACTACCOUNT=`mysql -u root -proot $COREDB -sN -e "select account_account from pass where id=$id"`
    echo "insert into pass (account_id, pass, issued, expires, account_show, account_identity, account_profile, account_group, account_share, account_prompt, account_app, account_index, account_contact, account_agent, account_user, account_access, account_account) select id, $PASS, $ISSUED, $EXPIRES, $ACTSHOW, $ACTIDENTITY, $ACTPROFILE, $ACTGROUP, $ACTSHARE, $ACTPROMPT, $ACTAPP, $ACTINDEX, $ACTCONTACT, $ACTAGENT, $ACTUSER, $ACTACCESS, $ACTACCOUNT from account where emigo_id='$2';"
  done
}

Extract_Alert () {
  mysql -u root -proot $COREDB -sN -e "select id from account_alert where account_id=$1" | while read id;
  do
    ALERT=`mysql -u root -proot $COREDB -sN -e "select alert_id from account_alert where id=$id"`
    ALERT=${ALERT//\'/\\\'}
    if [ "$ALERT" != "NULL" ]; then
      ALERT="'$ALERT'"
    fi
    CREATED=`mysql -u root -proot $COREDB -sN -e "select created from account_alert where id=$id"`
    MESSAGE=`mysql -u root -proot $COREDB -sN -e "select message from account_alert where id=$id"`
    MESSAGE=${MESSAGE//\'/\\\'}
    if [ "$MESSAGE" != "NULL" ]; then
      MESSAGE="'$MESSAGE'"
    fi
    echo "insert into account_alert (account_id, alert_id, created, message) select id, $ALERT, $CREATED, $MESSAGE from account where emigo_id='$2';"
  done
}

Extract_Prompt () {
  mysql -u root -proot $COREDB -sN -e "select id from account_prompt where account_id=$1" | while read id;
  do
    PROMPT=`mysql -u root -proot $COREDB -sN -e "select prompt_id from account_prompt where id=$id"`
    PROMPT=${PROMPT//\'/\\\'}
    if [ "$PROMPT" != "NULL" ]; then
      PROMPT="'$PROMPT'"
    fi
    IMAGE=`mysql -u root -proot $COREDB -sN -e "select image from account_prompt where id=$id"`
    IMAGE=${IMAGE//\'/\\\'}
    if [ "$IMAGE" != "NULL" ]; then
      IMAGE="'$IMAGE'"
    fi
    QUESTION=`mysql -u root -proot $COREDB -sN -e "select question from account_prompt where id=$id"`
    QUESTION=${QUESTION//\'/\\\'}
    if [ "$QUESTION" != "NULL" ]; then
      QUESTION="'$QUESTION'"
    fi
    echo "insert into account_prompt (account_id, prompt_id, image, question) select id, $PROMPT, $IMAGE, $QUESTION from account where emigo_id='$2';"
  done
}

Extract_Answer () {
  mysql -u root -proot $COREDB -sN -e "select account_answer.id from account_answer inner join account_prompt on account_answer.prompt_id = account_prompt.id where account_prompt.account_id=$1" | while read id;
  do
    PROMPT=`mysql -u root -proot $COREDB -sN -e "select account_prompt.prompt_id from account_prompt inner join account_answer on account_prompt.id = account_answer.prompt_id where account_answer.id=$id"`
    PROMPT=${PROMPT//\'/\\\'}
    if [ "$PROMPT" != "NULL" ]; then
      PROMPT="'$PROMPT'"
    fi
    ANSWERID=`mysql -u root -proot $COREDB -sN -e "select answer_id from account_answer where id=$id"`
    ANSWERID=${ANSWERID//\'/\\\'}
    if [ "$ANSWERID" != "NULL" ]; then
      ANSWERID="'$ANSWERID'"
    fi
    ANSWER=`mysql -u root -proot $COREDB -sN -e "select answer from account_answer where id=$id"`
    ANSWER=${ANSWER//\'/\\\'}
    if [ "$ANSWER" != "NULL" ]; then
      ANSWER="'$ANSWER'"
    fi
    echo "call addanswer('$2', $PROMPT, $ANSWERID, $ANSWER);"
  done
}

Extract_UserAgent () {
  mysql -u root -proot $COREDB -sN -e "select user_agent.id from user_agent inner join user on user_agent.user_id = user.id where user.account_id=$1" | while read id;
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select user.emigo_id from user inner join user_agent on user.id = user_agent.user_id where user_agent.id=$id"`;
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    MESSAGE=`mysql -u root -proot $COREDB -sN -e "select message from user_agent where id=$id"`
    MESSAGE=${MESSAGE//\'/\\\'}
    if [ "$MESSAGE" != "NULL" ]; then
      MESSAGE="'$MESSAGE'"
    fi
    SIGNATURE=`mysql -u root -proot $COREDB -sN -e "select signature from user_agent where id=$id"`
    SIGNATURE=${SIGNATURE//\'/\\\'}
    if [ "$SIGNATURE" != "NULL" ]; then
      SIGNATURE="'$SIGNATURE'"
    fi
    ISSUED=`mysql -u root -proot $COREDB -sN -e "select issued from user_agent where id=$id"`
    EXPIRES=`mysql -u root -proot $COREDB -sN -e "select expires from user_agent where id=$id"`
    TOKEN=`mysql -u root -proot $COREDB -sN -e "select token from user_agent where id=$id"`
    TOKEN=${TOKEN//\'/\\\'}
    if [ "$TOKEN" != "NULL" ]; then
      TOKEN="'$TOKEN'"
    fi
    echo "call adduseragent('$2', $EMIGO, $MESSAGE, $SIGNATURE, $ISSUED, $EXPIRES, $TOKEN);"
  done
}

Extract_OriginalAsset () {
  mysql -u root -proot $COREDB -sB -e "select upload.id from upload inner join subject on upload.subject_id = subject.id where subject.account_id=$1" | while read id;
  do
    SUBJECT=`mysql -u root -proot $COREDB -sN -e "select subject.subject_id from subject inner join upload on subject.id = upload.subject_id where upload.id=$id"`
    SUBJECT=${SUBJECT//\'/\\\'}
    if [ "$SUBJECT" != "NULL" ]; then
      SUBJECT="'$SUBJECT'"
    fi
    ASSET=`mysql -u root -proot $COREDB -sN -e "select asset_id from upload where id=$id"`
    ASSET=${ASSET//\'/\\\'}
    if [ "$ASSET" != "NULL" ]; then
      ASSET="'$ASSET'"
    fi
    NAME=`mysql -u root -proot $COREDB -sN -e "select original_name from upload where id=$id"`
    NAME=${NAME//\'/\\\'}
    if [ "$NAME" != "NULL" ]; then
      NAME="'$NAME'"
    fi
    STATUS=`mysql -u root -proot $COREDB -sN -e "select status from upload where id=$id"`
    STATUS=${STATUS//\'/\\\'}
    if [ "$STATUS" != "NULL" ]; then
      STATUS="'$STATUS'"
    fi
    CREATED=`mysql -u root -proot $COREDB -sN -e "select created from upload where id=$id"`
    SIZE=`mysql -u root -proot $COREDB -sN -e "select size from upload where id=$id"`
    HASH=`mysql -u root -proot $COREDB -sN -e "select hash from upload where id=$id"`
    HASH=${HASH//\'/\\\'}
    if [ "$HASH" != "NULL" ]; then
      HASH="'$HASH'"
    fi
    echo "call addoriginal('$2', $SUBJECT, $ASSET, $NAME, $STATUS, $CREATED, $SIZE, $HASH);"
  done
}

Extract_Tag () {
  mysql -u root -proot $COREDB -sB -e "select upload.id from upload inner join subject on upload.subject_id = subject.id where subject.account_id=$1" | while read id;
  do
    SUBJECT=`mysql -u root -proot $COREDB -sN -e "select subject.subject_id from subject inner join tag on subject.id = tag.subject_id where tag.id=$id"`
    SUBJECT=${SUBJECT//\'/\\\'}
    if [ "$SUBJECT" != "NULL" ]; then
      SUBJECT="'$SUBJECT'"
    fi
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo_id from tag where id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    TAG=`mysql -u root -proot $COREDB -sN -e "select tag_id from tag where id=$id"`
    TAG=${TAG//\'/\\\'}
    if [ "$TAG" != "NULL" ]; then
      TAG="'$TAG'"
    fi
    SCHEMA=`mysql -u root -proot $COREDB -sN -e "select schema_id from tag where id=$id"`
    SCHEMA=${SCHEMA//\'/\\\'}
    if [ "$SCHEMA" != "NULL" ]; then
      SCHEMA="'$SCHEMA'"
    fi
    DATA=`mysql -u root -proot $COREDB -sN -e "select data from tag where id=$id"`
    DATA=${DATA//\'/\\\'}
    if [ "$DATA" != "NULL" ]; then
      DATA="'$DATA'"
    fi
    MODIFIED=`mysql -u root -proot $COREDB -sN -e "select modified from tag where id=$id"`
    CREATED=`mysql -u root -proot $COREDB -sN -e "select created from tag where id=$id"`
    echo "call addtag('$2', $SUBJECT, $EMIGO, $TAG, $SCHEMA, $CREATED, $MOEIFIED, $DATA);"
  done
}

Extract_SubjectAsset () {
  mysql -u root -proot $COREDB -sB -e "select subject_asset.id from subject_asset inner join subject on subject_asset.subject_id = subject.id where subject.account_id=$1" | while read id;
  do
    SUBJECT=`mysql -u root -proot $COREDB -sN -e "select subject.subject_id from subject inner join subject_asset on subject.id = subject_asset.subject_id where subject_asset.id=$id"`
    SUBJECT=${SUBJECT//\'/\\\'}
    if [ "$SUBJECT" != "NULL" ]; then
      SUBJECT="'$SUBJECT'"
    fi
    PENDING=`mysql -u root -proot $COREDB -sN -e "select subject.subject_id from subject inner join subject_asset on subject.id = subject_asset.pending_id where subject_asset.id=$id"`
    PENDING=${PENDING//\'/\\\'}
    if [ "$PENDING" != "NULL" ]; then
      PENDING="'$PENDING'"
    fi
    ASSET=`mysql -u root -proot $COREDB -sN -e "select asset_id from subject_asset where id=$id"`
    ASSET=${ASSET//\'/\\\'}
    if [ "$ASSET" != "NULL" ]; then
      ASSET="'$ASSET'"
    fi
    ORIGINAL=`mysql -u root -proot $COREDB -sN -e "select original_id from subject_asset where id=$id"`
    ORIGINAL=${ORIGINAL//\'/\\\'}
    if [ "$ORIGINAL" != "NULL" ]; then
      ORIGINAL="'$ORIGINAL'"
    fi
    STATUS=`mysql -u root -proot $COREDB -sN -e "select status from subject_asset where id=$id"`
    STATUS=${STATUS//\'/\\\'}
    if [ "$STATUS" != "NULL" ]; then
      STATUS="'$STATUS'"
    fi
    TRANSFORM=`mysql -u root -proot $COREDB -sN -e "select status from subject_asset where id=$id"`
    TRANSFORM=${TRANSFORM//\'/\\\'}
    if [ "$TRANSFORM" != "NULL" ]; then
      TRANSFORM="'$TRANSFORM'"
    fi
    CREATED=`mysql -u root -proot $COREDB -sN -e "select created from subject_asset where id=$id"`
    SIZE=`mysql -u root -proot $COREDB -sN -e "select asset_size from subject_asset where id=$id"`
    HASH=`mysql -u root -proot $COREDB -sN -e "select asset_hash from subject_asset where id=$id"`
    HASH=${HASH//\'/\\\'}
    if [ "$HASH" != "NULL" ]; then
      HASH="'$HASH'"
    fi
    echo "call addasset('$2', $SUBJECT, $PENDING, $ASSET, $ORIGINAL, $TRANSFORM, $STATUS, $CREATED, $SIZE, $HASH);"
  done
}

Extract_Subject () {
  mysql -u root -proot $COREDB -sN -e "select id from subject where account_id=$1" | while read id;
  do
    SUBJECT=`mysql -u root -proot $COREDB -sN -e "select subject_id from subject where id=$id"`
    SUBJECT=${SUBJECT//\'/\\\'}
    if [ "$SUBJECT" != "NULL" ]; then
      SUBJECT="'$SUBJECT'"
    fi
    SCHEMA=`mysql -u root -proot $COREDB -sN -e "select schema_id from subject where id=$id"`
    SCHEMA=${SCHEMA//\'/\\\'}
    if [ "$SCHEMA" != "NULL" ]; then
      SCHEMA="'$SCHEMA'"
    fi
    VALUE=`mysql -u root -proot $COREDB -sN -e "select value from subject where id=$id"`
    VALUE=${VALUE//\'/\\\'}
    if [ "$VALUE" != "NULL" ]; then
      VALUE="'$VALUE'"
    fi
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from subject where id=$id"`
    TAG=`mysql -u root -proot $COREDB -sN -e "select tag_revision from subject where id=$id"`
    CREATED=`mysql -u root -proot $COREDB -sN -e "select created from subject where id=$id"`
    MODIFIED=`mysql -u root -proot $COREDB -sN -e "select modified from subject where id=$id"`
    EXPIRES=`mysql -u root -proot $COREDB -sN -e "select expires from subject where id=$id"`
    VIEWABLE=`mysql -u root -proot $COREDB -sN -e "select viewable from subject where id=$id"`
    echo "insert into subject (account_id, subject_id, schema_id, value, revision, tag_revision, created, modified, expires, viewable) select id, $SUBJECT, $SCHEMA, $VALUE, $REVISION, $TAG, $CREATED, $MODIFIED, $EXPIRES, $VIEWABLE from account where emigo_id='$2';"
  done
}

Extract_Attribute () {
  mysql -u root -proot $COREDB -sN -e "select id from account_attribute where account_id=$1" | while read id
  do
    ATTRIBUTE=`mysql -u root -proot $COREDB -sN -e "select attribute_id from account_attribute where id=$id"`
    ATTRIBUTE=${ATTRIBUTE//\'/\\\'}
    if [ "$ATTRIBUTE" != "NULL" ]; then
      ATTRIBUTE="'$ATTRIBUTE'"
    fi
    SCHEMA=`mysql -u root -proot $COREDB -sN -e "select schema_id from account_attribute where id=$id"`
    SCHEMA=${SCHEMA//\'/\\\'}
    if [ "$SCHEMA" != "NULL" ]; then
      SCHEMA="'$SCHEMA'"
    fi
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from account_attribute where id=$id"`
    VALUE=`mysql -u root -proot $COREDB -sN -e "select value from account_attribute where id=$id"`
    VALUE=${VALUE//\'/\\\'}
    if [ "$VALUE" != "NULL" ]; then
      VALUE="'$VALUE'"
    fi
    echo "insert into account_attribute (account_id, attribute_id, schema_id, revision, value) select id, $ATTRIBUTE, $SCHEMA, $REVISION, $VALUE from account where emigo_id='$2';"
  done
}

Extract_Service () {
  mysql -u root -proot $COREDB -sN -e "select id from service where account_id=$1" | while read id
  do
    SERVICE=`mysql -u root -proot $COREDB -sN -e "select emigo_id from service inner join service_registry on service.service_id=service_registry.id where service.id=$id"`
    TOKEN=`mysql -u root -proot $COREDB -sN -e "select token from service where id=$id"`
    TOKEN=${TOKEN//\'/\\\'}
    if [ "$TOKEN" != "NULL" ]; then
      TOKEN="'$TOKEN'"
    fi
    HIDDEN=`mysql -u root -proot $COREDB -sN -e "select hidden from service where id=$id"`
    ACTSHOW=`mysql -u root -proot $COREDB -sN -e "select account_show from service where id=$id"`
    ACTIDENTITY=`mysql -u root -proot $COREDB -sN -e "select account_identity from service where id=$id"`
    ACTPROFILE=`mysql -u root -proot $COREDB -sN -e "select account_profile from service where id=$id"`
    ACTGROUP=`mysql -u root -proot $COREDB -sN -e "select account_group from service where id=$id"`
    ACTSHARE=`mysql -u root -proot $COREDB -sN -e "select account_share from service where id=$id"`
    ACTPROMPT=`mysql -u root -proot $COREDB -sN -e "select account_prompt from service where id=$id"`
    ACTAPP=`mysql -u root -proot $COREDB -sN -e "select account_app from service where id=$id"`
    ACTINDEX=`mysql -u root -proot $COREDB -sN -e "select account_index from service where id=$id"`
    ACTAGENT=`mysql -u root -proot $COREDB -sN -e "select account_agent from service where id=$id"`
    ACTUSER=`mysql -u root -proot $COREDB -sN -e "select account_user from service where id=$id"`
    ACTACCESS=`mysql -u root -proot $COREDB -sN -e "select account_access from service where id=$id"`
    ACTACCOUNT=`mysql -u root -proot $COREDB -sN -e "select account_account from service where id=$id"`
    SRVSHOW=`mysql -u root -proot $COREDB -sN -e "select service_show from service where id=$id"`
    SRVIDENTITY=`mysql -u root -proot $COREDB -sN -e "select service_identity from service where id=$id"`
    SRVPROFILE=`mysql -u root -proot $COREDB -sN -e "select service_profile from service where id=$id"`
    SRVGROUP=`mysql -u root -proot $COREDB -sN -e "select service_group from service where id=$id"`
    SRVSHARE=`mysql -u root -proot $COREDB -sN -e "select service_share from service where id=$id"`
    SRVPROMPT=`mysql -u root -proot $COREDB -sN -e "select service_prompt from service where id=$id"`
    SRVAPP=`mysql -u root -proot $COREDB -sN -e "select service_app from service where id=$id"`
    SRVINDEX=`mysql -u root -proot $COREDB -sN -e "select service_index from service where id=$id"`
    SRVAGENT=`mysql -u root -proot $COREDB -sN -e "select service_agent from service where id=$id"`
    SRVUSER=`mysql -u root -proot $COREDB -sN -e "select service_user from service where id=$id"`
    SRVACCESS=`mysql -u root -proot $COREDB -sN -e "select service_access from service where id=$id"`
    SRVACCOUNT=`mysql -u root -proot $COREDB -sN -e "select service_account from service where id=$id"`
    ENABLED=`mysql -u root -proot $COREDB -sN -e "select enabled from service where id=$id"`
    echo "call addservice('$SERVICE', '$2', $TOKEN, $HIDDEN, $ACTSHOW, $ACTIDENTITY, $ACTPROFILE, $ACTGROUP, $ACTSHARE, $ACTPROMPT, $ACTAPP, $ACTINDEX, $ACTAGENT, $ACTUSER, $ACTACCESS, $ACTACCOUNT, $SRVSHOW, $SRVIDENTITY, $SRVPROFILE, $SRVGROUP, $SRVSHARE, $SRVPROMPT, $SRVAPP, $SRVINDEX, $SRVAGENT, $SRVUSER, $SRVACCESS, $SRVACCOUNT, $ENABLED);"
  done
}

Extract_AttributeLabel () {
  mysql -u root -proot $COREDB -sN -e "select attribute_label.id from attribute_label inner join account_attribute on attribute_label.account_attribute_id=account_attribute.id where account_attribute.account_id=$1" | while read id
  do
    ATTRIBUTE=`mysql -u root -proot $COREDB -sN -e "select attribute_id from account_attribute inner join attribute_label on account_attribute.id = attribute_label.account_attribute_id where attribute_label.id=$id"`
    ATTRIBUTE=${ATTRIBUTE//\'/\\\'}
    if [ "$ATTRIBUTE" != "NULL" ]; then
      ATTRIBUTE="'$ATTRIBUTE'"
    fi
    LABEL=`mysql -u root -proot $COREDB -sN -e "select label.label_id from label inner join attribute_label on label.id = attribute_label.label_id where attribute_label.id=$id"`
    LABEL=${LABEL//\'/\\\'}
    if [ "$LABEL" != "NULL" ]; then
      LABEL="'$LABEL'"
    fi
    echo call "addattributelabel('$2', $ATTRIBUTE, $LABEL);"
  done
}

Extract_SubjectLabel () {
  mysql -u root -proot $COREDB -sN -e "select subject_label.id from subject_label inner join subject on subject_label.subject_id=subject.id where subject.account_id=$1" | while read id
  do
    SUBJECT=`mysql -u root -proot $COREDB -sN -e "select subject.subject_id from subject inner join subject_label on subject.id = subject_label.subject_id where subject_label.id=$id"`
    SUBJECT=${SUBJECT//\'/\\\'}
    if [ "$SUBJECT" != "NULL" ]; then
      SUBJECT="'$SUBJECT'"
    fi
    LABEL=`mysql -u root -proot $COREDB -sN -e "select label.label_id from label inner join subject_label on label.id = subject_label.label_id where subject_label.id=$id"`
    LABEL=${LABEL//\'/\\\'}
    if [ "$LABEL" != "NULL" ]; then
      LABEL="'$LABEL'"
    fi
    echo call "addsubjectlabel('$2', $SUBJECT, $LABEL);"
  done
}

Extract_EmigoLabel () {
  mysql -u root -proot $COREDB -sN -e "select emigo_label.id from emigo_label inner join emigo on emigo_label.emigo_id=emigo.id where emigo.account_id=$1" | while read id
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo.emigo_id from emigo inner join emigo_label on emigo.id = emigo_label.emigo_id where emigo_label.id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    LABEL=`mysql -u root -proot $COREDB -sN -e "select label.label_id from label inner join emigo_label on label.id = emigo_label.label_id where emigo_label.id=$id"`
    LABEL=${LABEL//\'/\\\'}
    if [ "$LABEL" != "NULL" ]; then
      LABEL="'$LABEL'"
    fi
    echo call "addemigolabel('$2', $EMIGO, $LABEL);"
  done
}

Extract_ShareConnection () {
  mysql -u root -proot $COREDB -sN -e "select id from share_connection where account_id=$1" | while read id
  do
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo.emigo_id from emigo inner join share_connection on share_connection.emigo_id = emigo.id where share_connection.id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    SHARE=`mysql -u root -proot $COREDB -sN -e "select share_id from share_connection where share_connection.id=$id"`
    SHARE=${SHARE//\'/\\\'}
    if [ "$SHARE" != "NULL" ]; then
      SHARE="'$SHARE'"
    fi
    INPUT=`mysql -u root -proot $COREDB -sN -e "select in_token from share_connection where share_connection.id=$id"`
    INPUT=${INPUT//\'/\\\'}
    if [ "$INPUT" != "NULL" ]; then
      INPUT="'$INPUT'"
    fi
    OUTPUT=`mysql -u root -proot $COREDB -sN -e "select out_token from share_connection where share_connection.id=$id"`
    OUTPUT=${OUTPUT//\'/\\\'}
    if [ "$OUTPUT" != "NULL" ]; then
      OUTPUT="'$OUTPUT'"
    fi
    STATE=`mysql -u root -proot $COREDB -sN -e "select state from share_connection where share_connection.id=$id"`
    STATE=${STATE//\'/\\\'}
    if [ "$STATE" != "NULL" ]; then
      STATE="'$STATE'"
    fi
    UPDATED=`mysql -u root -proot $COREDB -sN -e "select updated from share_connection where share_connection.id=$id"`
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from share_connection where share_connection.id=$id"`
    echo call "addshareconnection('$2', $EMIGO, $SHARE, $INPUT, $OUTPUT, $STATE, $UPDATED, $REVISION);"
  done
}

Extract_SharePending () {
  mysql -u root -proot $COREDB -sN -e "select id from share_pending where account_id=$1" | while read id
  do
    EMIGOID=`mysql -u root -proot $COREDB -sN -e "select emigo.emigo_id from emigo inner join share_pending on share_pending.emigo_id = emigo.id where share_pending.id=$id"`
    EMIGOID=${EMIGOID//\'/\\\'}
    if [ "$EMIGOID" != "NULL" ]; then
      EMIGOID="'$EMIGOID'"
    fi
    EMIGO=`mysql -u root -proot $COREDB -sN -e "select emigo from share_pending where id=$id"`
    EMIGO=${EMIGO//\'/\\\'}
    if [ "$EMIGO" != "NULL" ]; then
      EMIGO="'$EMIGO'"
    fi
    SHARE=`mysql -u root -proot $COREDB -sN -e "select share_id from share_pending where id=$id"`
    SHARE=${SHARE//\'/\\\'}
    if [ "$SHARE" != "NULL" ]; then
      SHARE="'$SHARE'"
    fi
    EMIGOKEY=`mysql -u root -proot $COREDB -sN -e "select emigo_message_key from share_pending where id=$id"`
    EMIGOKEY=${EMIGOKEY//\'/\\\'}
    if [ "$EMIGOKEY" != "NULL" ]; then
      EMIGOKEY="'$EMIGOKEY'"
    fi
    EMIGOKEYTYPE=`mysql -u root -proot $COREDB -sN -e "select emigo_message_key_type from share_pending where id=$id"`
    EMIGOKEYTYPE=${EMIGOKEYTYPE//\'/\\\'}
    if [ "$EMIGOKEYTYPE" != "NULL" ]; then
      EMIGOKEYTYPE="'$EMIGOKEYTYPE'"
    fi
    EMIGOSIGNATURE=`mysql -u root -proot $COREDB -sN -e "select emigo_message_signature from share_pending where id=$id"`
    EMIGOSIGNATURE=${EMIGOSIGNATURE//\'/\\\'}
    if [ "$EMIGOSIGNATURE" != "NULL" ]; then
      EMIGOSIGNATURE="'$EMIGOSIGNATURE'"
    fi
    EMIGODATA=`mysql -u root -proot $COREDB -sN -e "select emigo_message_data from share_pending where id=$id"`
    EMIGODATA=${EMIGODATA//\'/\\\'}
    if [ "$EMIGODATA" != "NULL" ]; then
      EMIGODATA="'$EMIGODATA'"
    fi
    UPDATED=`mysql -u root -proot $COREDB -sN -e "select updated from share_pending where id=$id"`
    REVISION=`mysql -u root -proot $COREDB -sN -e "select revision from share_pending where id=$id"`
    echo call "addsharepending('$2', $EMIGOID, $EMIGO, $SHARE, $EMIGOKEY, $EMIGOKEYTYPE, $EMIGOSIGNATURE, $EMIGODATA, $UPDATED, $REVISION);"
  done
}

Extract_Message () {
  MESSAGE=`mysql -u root -proot $COREDB -sN -e "select message from account_message where account_id=$1"`
  MESSAGE=${MESSAGE//\'/\\\'}
  if [ "$MESSAGE" != "NULL" ]; then
    MESSAGE="'$MESSAGE'"
  fi
  SIGNATURE=`mysql -u root -proot $COREDB -sN -e "select signature from account_message where account_id=$1"`
  SIGNATURE=${SIGNATURE//\'/\\\'}
  if [ "$SIGNATURE" != "NULL" ]; then
    SIGNATURE="'$SIGNATURE'"
  fi
  PUBKEY=`mysql -u root -proot $COREDB -sN -e "select pubkey from account_message where account_id=$1"`
  PUBKEY=${PUBKEY//\'/\\\'}
  if [ "$PUBKEY" != "NULL" ]; then
    PUBKEY="'$PUBKEY'"
  fi
  KEYTYPE=`mysql -u root -proot $COREDB -sN -e "select pubkey_type from account_message where account_id=$1"`
  KEYTYPE=${KEYTYPE//\'/\\\'}
  if [ "$KEYTYPE" != "NULL" ]; then
    KEYTYPE="'$KEYTYPE'"
  fi
  echo "insert into account_message (account_id, message, signature, pubkey, pubkey_type) select id, $MESSAGE, $SIGNATURE, $PUBKEY, $KEYTYPE from account where emigo_id='$2';"
}

Extract_Account () {

  echo "drop procedure if exists addemigo;"
  echo "drop procedure if exists addasset;"
  echo "drop procedure if exists addoriginal;"
  echo "drop procedure if exists addtag;"
  echo "drop procedure if exists addanswer;"
  echo "drop procedure if exists addservice;"
  echo "drop procedure if exists addsubjectlabel;"
  echo "drop procedure if exists addattributelabel;"
  echo "drop procedure if exists addemigolabel;"
  echo "drop procedure if exists adduseragent;"
  echo "drop procedure if exists addserviceagent;"
  echo "drop procedure if exists addshareconnection;"
  echo "drop procedure if exists addsharepending;"
  echo "delimiter ##"
  echo "create procedure addemigo(in _id varchar(64), in _name varchar(1024), in _location varchar(1024), in _description varchar(4096), in _logo mediumtext, in _revision int(11), in _version varchar(32), in _blocked tinyint(4), in _handle varchar(128), in _node varchar(1024), in _registry varchar(1024) )"
  echo "begin"
  echo "  DECLARE REV INT DEFAULT NULL;"
  echo "  DECLARE NUM INT DEFAULT NULL;"
  echo "  select revision into REV from emigo_registry where emigo_id=_id;"
  echo "  select count(*) into NUM from emigo_registry where emigo_id=_id;"
  echo "  IF NUM = 0 THEN"
  echo "    insert into emigo_registry (emigo_id, name, location, description, logo, revision, version, blocked, handle, node, registry) values (_id, _name, _location, _description, _logo, _revision, _version, _blocked, _handle, _node, _registry);"
  echo "  END IF;"
  echo "  IF REV < _revision THEN"
  echo "    update emigo_registry set name=_name, location=_location, description=_description, logo=_logo, revision=_revision, version=_version, blocked=_blocked, handle=_handle, node=_node, registry=_registry where emigo_id=_id;"
  echo "  END IF;"
  echo "end ##"
  echo ""
  echo "create procedure addservice(in _serviceId varchar(64), in _accountId varchar(64), in _token varchar(64), in _hidden tinyint(4), in _accountShow tinyint(4), in _accountIdentity tinyint(4), in _accountProfile tinyint(4), in _accountGroup tinyint(4), in _accountShare tinyint(4), in _accountPrompt tinyint(4), in _accountApp tinyint(4), in _accountIndex tinyint(4), in _accountAgent tinyint(4), in _accountUser tinyint(4), in _accountAccess tinyint(4), in _accountAccount tinyint(4), in _serviceShow tinyint(4), in _serviceIdentity tinyint(4), in _serviceProfile tinyint(4), in _serviceGroup tinyint(4), in _serviceShare tinyint(4), in _servicePrompt tinyint(4), in _serviceApp tinyint(4), in _serviceIndex tinyint(4), in _serviceAgent tinyint(4), in _serviceUser tinyint(4), in _serviceAccess tinyint(4), in _serviceAccount tinyint(4), in _enabled tinyint(4))"
  echo "begin"
  echo "  DECLARE ACCOUNT INT DEFAULT NULL;"
  echo "  DECLARE SERVICE INT DEFAULT NULL;"
  echo "  select id into SERVICE from service_registry where emigo_id=_serviceId;"
  echo "  select id into ACCOUNT from account where emigo_id=_accountId;"
  echo "  insert into service (service_id, account_id, token, hidden, account_show, account_identity, account_profile, account_group, account_share, account_prompt, account_app, account_index, account_agent, account_user, account_access, account_account, service_show, service_identity, service_profile, service_group, service_share, service_prompt, service_app, service_index, service_agent, service_user, service_access, service_account, enabled) values (SERVICE, ACCOUNT, _token, _hidden, _accountShow, _accountIdentity, _accountProfile, _accountGroup, _accountShare, _accountPrompt, _accountApp, _accountIndex, _accountAgent, _accountUser, _accountAccess, _accountAccount, _serviceShow, _serviceIdentity, _serviceProfile, _serviceGroup, _serviceShare, _servicePrompt, _serviceApp, _serviceIndex, _serviceAgent, _serviceUser, _serviceAccess, _serviceAccount, _enabled);"
  echo "end ##"
  echo ""
  echo "create procedure addattributelabel(in _emigoId varchar(64), in _attribute varchar(32), in _label varchar(32))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE ATTRIBUTEID INT DEFAULT NULL;"
  echo "  DECLARE LABELID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into ATTRIBUTEID from account_attribute where account_id=ACCOUNTID and attribute_id=_attribute;"
  echo "  select id into LABELID from label where account_id=ACCOUNTID and label_id=_label;"
  echo "  insert into attribute_label (account_attribute_id, label_id) values (ATTRIBUTEID, LABELID);"
  echo "end ##"
  echo ""
  echo "create procedure addemigolabel(in _emigoId varchar(64), in _emigo varchar(64), in _label varchar(32))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE EMIGOID INT DEFAULT NULL;"
  echo "  DECLARE LABELID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into EMIGOID from emigo where account_id=ACCOUNTID and emigo_id=_emigo;"
  echo "  select id into LABELID from label where account_id=ACCOUNTID and label_id=_label;"
  echo "  insert into emigo_label (emigo_id, label_id) values (EMIGOID, LABELID);"
  echo "end ##"
  echo ""
  echo "create procedure addsubjectlabel(in _emigoId varchar(64), in _subject varchar(32), in _label varchar(32))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE SUBJECTID INT DEFAULT NULL;"
  echo "  DECLARE LABELID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into SUBJECTID from subject where account_id=ACCOUNTID and subject_id=_subject;"
  echo "  select id into LABELID from label where account_id=ACCOUNTID and label_id=_label;"
  echo "  insert into subject_label (subject_id, label_id) values (SUBJECTID, LABELID);"
  echo "end ##"
  echo ""
  echo "create procedure addanswer(in _emigoId varchar(64), in _prompt varchar(32), in _answerId varchar(32), in _answer varchar(1024))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE PROMPTID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into PROMPTID from account_prompt where account_id=ACCOUNTID and prompt_id=_prompt;"
  echo "  insert into account_answer (prompt_id, answer_id, answer) values (PROMPTID, _answerId, _answer);"
  echo "end ##"
  echo ""
  echo "create procedure addasset(in _emigoId varchar(64), in _subject varchar(32), in _pending varchar(32), in _asset varchar(32), in _original varchar(48), in _transform varchar(8), in _status varchar(32), in _created int(64), in _size int(64), in _hash varchar(64))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE SUBJECTID INT DEFAULT NULL;"
  echo "  DECLARE PENDINGID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into SUBJECTID from subject where account_id=ACCOUNTID and subject_id=_subject;"
  echo "  select id into PENDINGID from subject where account_id=ACCOUNTID and subject_id=_pending;"
  echo "  insert into subject_asset (subject_id, pending_id, asset_id, original_id, transform, status, created, asset_size, asset_hash) values (SUBJECTID, PENDINGID, _asset, _original, _transform, _status, _created, _size, _hash);"
  echo "end ##"
  echo ""
  echo "create procedure addoriginal(in _emigoId varchar(64), in _subject varchar(32), in _asset varchar(34), in _name varchar(1024), in _status varchar(32), in _created int(64), in _size int(64), in _hash varchar(64))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE SUBJECTID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into SUBJECTID from subject where account_id=ACCOUNTID and subject_id=_subject;"
  echo "  insert into subject_asset (subject_id, asset_id, original_name, status, created, size, hash) values (SUBJECTID, _asset, _name, _status, _created, _size, _hash);"
  echo "end ##"
  echo ""
  echo "create procedure addtag(in _emigoId varchar(64), in _subject varchar(32), in _emigo varchar(64), in _tag varchar(32), in _schema varchar(64), in _created int(64), in _modified int(64), in _data mediumtext)"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE SUBJECTID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into SUBJECTID from subject where account_id=ACCOUNTID and subject_id=_subject;"
  echo "  insert into tag (subject_id, emigo_id, tag_id, schema_id, data, modified, created) values (SUBJECTID, _emigo, _tag, _schema, _data, _modified, _created);"
  echo "end ##"
  echo ""
  echo "create procedure adduseragent(in _emigoId varchar(64), in _emigo varchar(64), in _message mediumtext, in _signature varchar(1024), in _issued int(64), in _expires int(64), in _token varchar(64))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE USERID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into USERID from user where account_id=ACCOUNTID and emigo_id=_emigo;"
  echo "  insert into user_agent (user_id, message, signature, issued, expires, token) values (USERID, _message, _signature, _issued, _expires, _token);"
  echo "end ##"
  echo ""
  echo "create procedure addserviceagent(in _emigoId varchar(64), in _emigo varchar(64), in _service varchar(64), in _issued int(64), in _expires int(64), in _token varchar(64))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE EMIGOID INT DEFAULT NULL;"
  echo "  DECLARE SERVICEID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into EMIGOID from emigo where account_id=ACCOUNTID and emigo_id=_emigo;"
  echo "  select id into SERVICEID from service_registry where emigo_id=_service;"
  echo "  insert into service_agent (emigo_id, service_id, issued, expires, token) values (EMIGOID, SERVICEID, _issued, _expires, _token);"
  echo "end ##"
  echo ""
  echo "create procedure addshareconnection(in _emigoId varchar(64), in _emigo varchar(64), in _share varchar(32), in _input varchar(64), in _output varchar(64), in _state varchar(16), in _updated int(64), in _revision int(11))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE EMIGOID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_emigoId;"
  echo "  select id into EMIGOID from emigo where account_id=ACCOUNTID and emigo_id=_emigo;"
  echo "  insert into share_connection (account_id, emigo_id, share_id, in_token, out_token, state, updated, revision) values (ACCOUNTID, EMIGOID, _share, _input, _output, _state, _updated, _revision);"
  echo "end ##"
  echo ""
  echo "create procedure addsharepending(in _accountId varchar(64), in _emigoId varchar(64), in _emigo varchar(64), in _share varchar(32), in _emigoKey varchar(4096), in _emigoKeyType varchar(32), in _emigoSignature varchar(1024), in _emigoData mediumtext, in _updated int(64), in _revision int(11))"
  echo "begin"
  echo "  DECLARE ACCOUNTID INT DEFAULT NULL;"
  echo "  DECLARE EMIGOID INT DEFAULT NULL;"
  echo "  select id into ACCOUNTID from account where emigo_id=_accountId;"
  echo "  IF _emigoId != '' THEN"
  echo "    select id into EMIGOID from emigo where account_id=ACCOUNTID and emigo_id=_emigoId;"
  echo "  END IF;"
  echo "  insert into share_pending (account_id, emigo_id, emigo, share_id, emigo_message_key, emigo_message_key_type, emigo_message_signature, emigo_message_data, updated, revision) values (ACCOUNTID, EMIGOID, _emigo, _share, _emigoKey, _emigoKeyType, _emigoSignature, _emigoData, _updated, _revision);"
  echo "end ##"
  echo ""

  # extract account
  Extract_EmigoRegistry $2
  ENABLED=`mysql -u root -proot $COREDB -sN -e "select enabled from account where id=$1"`
  LOCKED=`mysql -u root -proot $COREDB -sN -e "select locked from account where id=$1"`
  DIRTY=`mysql -u root -proot $COREDB -sN -e "select dirty from account where id=$1"`
  INDEX=`mysql -u root -proot $COREDB -sN -e "select index_revision from account where id=$1"`
  SHARE=`mysql -u root -proot $COREDB -sN -e "select share_revision from account where id=$1"`
  PROMPT=`mysql -u root -proot $COREDB -sN -e "select prompt_revision from account where id=$1"`
  USER=`mysql -u root -proot $COREDB -sN -e "select user_revision from account where id=$1"`
  IDENTITY=`mysql -u root -proot $COREDB -sN -e "select identity_revision from account where id=$1"`
  PROFILE=`mysql -u root -proot $COREDB -sN -e "select profile_revision from account where id=$1"`
  GROUP=`mysql -u root -proot $COREDB -sN -e "select group_revision from account where id=$1"`
  CONTACT=`mysql -u root -proot $COREDB -sN -e "select contact_revision from account where id=$1"`
  VIEW=`mysql -u root -proot $COREDB -sN -e "select view_revision from account where id=$1"`
  SHOW=`mysql -u root -proot $COREDB -sN -e "select show_revision from account where id=$1"`
  SERVICE=`mysql -u root -proot $COREDB -sN -e "select service_revision from account where id=$1"`
  echo "insert into account (emigo_id, enabled, locked, dirty, index_revision, share_revision, prompt_revision, user_revision, identity_revision, profile_revision, group_revision, contact_revision, view_revision, show_revision, service_revision) values ('$2', $ENABLED, $LOCKED, $DIRTY, $INDEX, $SHARE, $PROMPT, $USER, $IDENTITY, $PROFILE, $GROUP, $CONTACT, $VIEW, $SHOW, $SERVICE);"

  # extract relevant emigo_registry
  mysql -u root -proot $COREDB -sN -e "select emigo_id from emigo where account_id=$1" | while read emigo;
  do
    Extract_EmigoRegistry "$emigo"
  done

  # extract relevant emigo_registry by service
  mysql -u root -proot $COREDB -sN -e "select emigo_id from service_registry inner join service on service_registry.id=service.service_id where service.account_id=$1" | while read emigo;
  do
    Extract_EmigoRegistry "$emigo"
  done

  # extract relevant service_registry
  mysql -u root -proot $COREDB -sN -e "select service_id from service where account_id=$1" | while read service;
  do
    Extract_ServiceRegistry "$service"
  done

  # extract relevant emigo_registry by user
  mysql -u root -proot $COREDB -sN -e "select emigo_id from user where account_id=$1" | while read user;
  do
    Extract_EmigoRegistry "$user"
  done

  # extract releavant emigo_registry by tag
  mysql -u root -proot $COREDB -sN -e "select tag.emigo_id from tag inner join subject on tag.subject_id = subject.id where account_id=$1" | while read tag;
  do
    Extract_EmigoRegistry "$tag"
  done

  # identify relevant agents
  #mysql -u root -proot $COREDB -sN -e "select service_registry.emigo_id from service_registry inner join service_agent on service_registry.id = service_agent.service_id inner join emigo on service_agent.emigo_id = emigo.id where emigo.account_id=$1" | while read agent;
  #do
  #  Extract_EmigoRegistry "$agent"
  #done

  # extract relevant agents
  #mysql -u root -proot $COREDB -sN -e "select service_agent.service_id from service_agent inner join emigo on service_agent.emigo_id = emigo.id where emigo.account_id=$1" | while read service;
  #do
  #  Extract_ServiceRegistry "$service"
  #done

  # extract account_message
  Extract_Message $1 $2

  # extract emigo
  Extract_Emigo $1 $2

  # extract label
  Extract_Label $1 $2

  # extract account_attribute
  Extract_Attribute $1 $2

  # extract service
  Extract_Service $1 $2

  # extract attribute_label
  Extract_AttributeLabel $1 $2

  # extract emigo_label
  Extract_EmigoLabel $1 $2

  # extract subject
  Extract_Subject $1 $2

  # extract subject_label
  Extract_SubjectLabel $1 $2

  # extract account_config
  Extract_Config $1 $2

  # extract account_prompt
  Extract_Prompt $1 $2

  # extract account_answer
  Extract_Answer $1 $2

  # extract account_alert
  Extract_Alert $1 $2

  # extract pass
  Extract_Pass $1 $2

  # extract subject_asset
  Extract_SubjectAsset $1 $2

  # extract orginal_asset
  Extract_OriginalAsset $1 $2

  # extract subject tags
  Extract_Tag $1 $2

  # extract user
  Extract_User $1 $2

  # extract reject_emigo
  Extract_RejectEmigo $1 $2

  # extract share_connection
  Extract_ShareConnection $1 $2

  # extract share_pending
  Extract_SharePending $1 $2

}

touch $SQLDIR/emigo.lst
mysql -u root -proot $COREDB -sN -e "select id, emigo_id from account order by id" | while read line;
do
  ACCOUNT_ID=`echo "$line" | awk '{print $1;}'`
  EMIGO_ID=`echo "$line" | awk '{print $2;}'`

  echo "$EMIGO_ID" >> $SQLDIR/emigo.lst

  # extract account
  Extract_Account $ACCOUNT_ID $EMIGO_ID > $SQLDIR/$EMIGO_ID.sql

done


