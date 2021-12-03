shopt -s nullglob
for emigo in /opt/diatum/asset/*; do
  EMIGO="$(basename -- $emigo)"

  # check if account has been deleted
  EMIGO_COUNT=`mysql -u root -proot coredb -sN -e "select count(*) from account where emigo_id='$EMIGO'"`
  if [ "$?" == "0" ]; then
    if [ "$EMIGO_COUNT" == "0" ]; then
      # delete emigo path
      logger "removing assets for emigo [$EMIGO]"
      rm -rf /opt/diatum/asset/$EMIGO
    else

      shopt -s nullglob
      for subject in /opt/diatum/asset/$EMIGO/*; do
        SUBJECT="$(basename -- $subject)"

        # check if subject has been deleted
        SUBJECT_COUNT=`mysql -u root -proot coredb -sN -e "select count(*) from subject inner join account on subject.account_id=account.id where subject.subject_id='$SUBJECT' and account.emigo_id='$EMIGO'"`
        if [ "$?" == "0" ]; then
          if [ "$SUBJECT_COUNT" == "0" ]; then
            # delete subject path
            logger "removing assets for subject [$EMIGO - $SUBJECT]"
            echo "/opt/diatum/assets/$EMIGO/$SUBJECT"
            rm -rf /opt/diatum/asset/$EMIGO/$SUBJECT
          else

            shopt -s nullglob
            for asset in /opt/diatum/asset/$EMIGO/$SUBJECT/*; do
              ASSET="$(basename -- $asset)"

              # check if asset has been deleted
              ASSET_COUNT=`mysql -u root -proot coredb -sN -e "select count(*) from subject_asset inner join subject on subject_asset.subject_id=subject.id inner join account on subject.account_id=account.id where subject.subject_id='$SUBJECT' and account.emigo_id='$EMIGO' and subject_asset.asset_id='$ASSET' and subject_asset.status='deleted'";`
              if [ "$?" == "0" ] && [ "$ASSET_COUNT" == "1" ]; then
                # asset has been deleted
                logger "removing asset from subject [$EMIGO - $SUBJECT - $ASSET]"
                rm -f /opt/diatum/asset/$EMIGO/$SUBJECT/$ASSET
              fi
              UPLOAD_COUNT=`mysql -u root -proot coredb -sN -e "select count(*) from upload inner join subject on upload.subject_id=subject.id inner join account on subject.account_id=account.id where subject.subject_id='$SUBJECT' and account.emigo_id='$EMIGO' and upload.asset_id='$ASSET' and upload.status='deleted'";`
              if [ "$?" == "0" ] && [ "$UPLOAD_COUNT" == "1" ]; then
                # upload has been deleted
                logger "removing asset from subject [$EMIGO - $SUBJECT - $ASSET]"
                rm -f /opt/diatum/asset/$EMIGO/$SUBJECT/$ASSET
              fi
            done

          fi
        fi
      done

    fi
  fi
done

