USERNAME=`cat /data/config/mysql_username`
PASSWORD=`cat /data/config/mysql_password`
DATABASE=`cat /data/config/mysql_database`
HOST=`cat /data/config/mysql_host`

function transcode_photo {
  (
    flock -n 201 # transcode in progress if fails
    if [ "$?" == "0" ]; then
      while [ true ]; do
  
        PHOTO=`mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "select subject_asset.id, subject_asset.asset_id, subject.subject_id, account.emigo_id, subject_asset.original_id, subject_asset.transform, subject.id from subject_asset inner join subject on subject_asset.subject_id = subject.id inner join account on account.id = subject.account_id where (subject_asset.status='pending' or subject_asset.status='processing') and subject_asset.transform like 'P%' order by subject_asset.id asc limit 1"`
        if [ "$PHOTO" == "" ]; then
          break;
        fi

        tokens=( $PHOTO )
        ID="${tokens[0]}"
        ASSET="${tokens[1]}"
        SUBJECT="${tokens[2]}"
        EMIGO="${tokens[3]}"
        SOURCE="${tokens[4]}"
        TRANSFORM="${tokens[5]}"
        SUBJECTID="${tokens[6]}"

        logger "transcode photo: $1/$EMIGO/$SUBJECT/$ASSET"
        if [ -f "$1/$EMIGO/$SUBJECT/$SOURCE" ]; then
          CMD=""
          if [ "$TRANSFORM" == "P01" ]; then
            CMD='-auto-orient -resize 1280x720>'
          elif [ "$TRANSFORM" == "P02" ]; then
            CMD='-auto-orient -resize 2688x1520>'
          elif [ "$TRANSFORM" == "P03" ]; then
            CMD='-auto-orient -resize 3840x2160>'
    elif [[ "$TRANSFORM" =~ ^P04 ]]; then
            PERCENT="${TRANSFORM#P04}"
            if [ "$PERCENT" == "" ]; then
              PERCENT="+0"
            fi
            W=`identify -format "%w" -quiet $1/$EMIGO/$SUBJECT/$SOURCE`
      H=`identify -format "%h" -quiet $1/$EMIGO/$SUBJECT/$SOURCE`
            CROP=""
            if [ "$W" -lt "$H" ]; then
              SHIFT=$(( ${PERCENT} * ${W} / 100 ))
	      FLIPPED=`identify -verbose $1/$EMIGO/$SUBJECT/$SOURCE | grep "Orientation: LeftBottom"`
	      if [ "$?" == "0" ]; then
	        SHIFT=$(( -1 * $SHIFT ))
	      fi

              if [[ ! "$SHIFT" =~ ^- ]]; then
                SHIFT="+${SHIFT}"
              fi 
              CROP="-gravity center -crop ${W}x${W}+0${SHIFT} +repage"
            fi
            if [ "$W" -gt "$H" ]; then
              SHIFT=$(( ${PERCENT} * ${H} / 100 ))

	      FLIPPED=`identify -verbose $1/$EMIGO/$SUBJECT/$SOURCE | grep "Orientation: LeftBottom"`
	      if [ "$?" == "0" ]; then
                SHIFT=$(( -1 * $SHIFT ))
	      fi

              if [[ ! "$SHIFT" =~ ^- ]]; then
                SHIFT="+${SHIFT}"
              fi 
              CROP="-gravity center -crop ${H}x${H}${SHIFT}+0 +repage"
            fi
            if [ "$W" -eq "$H" ]; then
              CROP="-gravity center -crop ${W}x${H}+0+0 +repage"
            fi
            CMD="${CROP} -auto-orient -resize 360x360>"
          fi

          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='processing' where id=$ID and status!='deleted'"
          convert -strip $1/$EMIGO/$SUBJECT/$SOURCE $CMD $1/$EMIGO/$SUBJECT/$ASSET
          if [ "$?" == "0" ]; then
            SIZE=`du --bytes $1/$EMIGO/$SUBJECT/$ASSET | cut -f1`
            HASH=`sha256sum $1/$EMIGO/$SUBJECT/$ASSET | awk '{ print $1 }'`
            DATE=`date +%s`
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set created=$DATE, asset_size=$SIZE, asset_hash='$HASH', pending_id=null, status='ready' where id=$ID and status!='deleted'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject set revision=revision+1 where id='$SUBJECTID'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update account set view_revision=view_revision+1, show_revision=show_revision+1 where emigo_id='$EMIGO'"
          else
            logger "transcode photo failed"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi
        else
          logger "transcode photo failed"
          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
        fi        
      done
    fi
  ) 201>/opt/diatum/lock/transcode_photo
}

function transcode_audio {
  (
    flock -n 202 # transcode in progress if fails
    if [ "$?" == "0" ]; then
      while [ true ]; do

        AUDIO=`mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "select subject_asset.id, subject_asset.asset_id, subject.subject_id, account.emigo_id, subject_asset.original_id, subject_asset.transform, subject.id from subject_asset inner join subject on subject_asset.subject_id = subject.id inner join account on account.id = subject.account_id where (subject_asset.status='pending' or subject_asset.status='processing') and subject_asset.transform like 'A%' order by subject_asset.id asc limit 1"`
        if [ "$AUDIO" == "" ]; then
          break;
        fi

        tokens=( $AUDIO )
        ID="${tokens[0]}"
        ASSET="${tokens[1]}"
        SUBJECT="${tokens[2]}"
        EMIGO="${tokens[3]}"
        SOURCE="${tokens[4]}"
        TRANSFORM="${tokens[5]}"
        SUBJECTID="${tokens[6]}"

        logger "transcode audio: $1/$EMIGO/$SUBJECT/$ASSET"
        if [ -f "$1/$EMIGO/$SUBJECT/$SOURCE" ]; then 
          CMD=""
          if [ "$TRANSFORM" == "A01" ]; then
            CMD='-codec:a libmp3lame -qscale:a 1'
          elif [ "$TRANSFORM" == "A02" ]; then
            CMD='-codec:a libmp3lame -qscale:a 3'
          elif [ "$TRANSFORM" == "A03" ]; then
            CMD='-codec:a libmp3lame -qscale:a 6'
          elif [ "$TRANSFORM" == "A04" ]; then
            CMD='-codec:a libmp3lame -qscale:a 9'
          else
            logger "transcode audio failed [$TRANSFORM]"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi

          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='processing' where id=$ID and status!='deleted'"
          ffmpeg -i $1/$EMIGO/$SUBJECT/$SOURCE -y -f mp3 -map_metadata -1 $CMD $1/$EMIGO/$SUBJECT/$ASSET 2> /dev/null 
          if [ "$?" == "0" ]; then
            SIZE=`du --bytes $1/$EMIGO/$SUBJECT/$ASSET | cut -f1`
            HASH=`sha256sum $1/$EMIGO/$SUBJECT/$ASSET | awk '{ print $1 }'`
            DATE=`date +%s`
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set created=$DATE, asset_size=$SIZE, asset_hash='$HASH', pending_id=null, status='ready' where id=$ID and status!='deleted'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject set revision=revision+1 where id='$SUBJECTID'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update account set view_revision=view_revision+1, show_revision=show_revision+1 where emigo_id='$EMIGO'"
          else
            logger "transcode audio failed"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi
        else
          logger "transcode audio failed"
          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
        fi
      done
    fi
  ) 202>/opt/diatum/lock/transcode_audio
}

function transcode_video {
  (
    flock -n 203 # transcode in progress if fails
    if [ "$?" == "0" ]; then
      while [ true ]; do

        VIDEO=`mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "select subject_asset.id, subject_asset.asset_id, subject.subject_id, account.emigo_id, subject_asset.original_id, subject_asset.transform, subject.id from subject_asset inner join subject on subject_asset.subject_id = subject.id inner join account on account.id = subject.account_id where (subject_asset.status='pending' or subject_asset.status='processing') and subject_asset.transform like 'V%' order by subject_asset.id asc limit 1"`
        if [ "$VIDEO" == "" ]; then
          break;
        fi

        tokens=( $VIDEO )
        ID="${tokens[0]}"
        ASSET="${tokens[1]}"
        SUBJECT="${tokens[2]}"
        EMIGO="${tokens[3]}"
        SOURCE="${tokens[4]}"
        TRANSFORM="${tokens[5]}"
        SUBJECTID="${tokens[6]}"

        logger "transcode video [$TRANSFORM]: $1/$EMIGO/$SUBJECT/$ASSET"
        if [ -f "$1/$EMIGO/$SUBJECT/$SOURCE" ]; then 
        
          CMD=""
          if [ "$TRANSFORM" == "V01" ]; then
            CMD='-vf scale=640:-2 -vcodec libx265 -crf 32 -preset veryfast -tag:v hvc1 -acodec aac'
          elif [ "$TRANSFORM" == "V02" ]; then
            CMD='-vf scale=1920:-2 -vcodec libx265 -crf 23 -preset veryfast -tag:v hvc1 -acodec aac'
          elif [ "$TRANSFORM" == "V03" ]; then
            CMD='-vf scale=720:-2 -vcodec libx265 -crf 23 -preset veryfast -tag:v hvc1 -acodec aac'
          elif [ "$TRANSFORM" == "V04" ]; then
            CMD='-vf scale=320:-2 -vcodec libx265 -crf 32 -preset veryfast -tag:v hvc1 -acodec aac'
          else
            logger "transcode video failed [$TRANSFORM]"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi

          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='processing' where id=$ID and status!='deleted'"
          ffmpeg -i $1/$EMIGO/$SUBJECT/$SOURCE -y -f mp4 -map_metadata -1 $CMD $1/$EMIGO/$SUBJECT/$ASSET 2> /dev/null 
          if [ "$?" == "0" ]; then
            SIZE=`du --bytes $1/$EMIGO/$SUBJECT/$ASSET | cut -f1`
            HASH=`sha256sum $1/$EMIGO/$SUBJECT/$ASSET | awk '{ print $1 }'`
            DATE=`date +%s`
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set created=$DATE, asset_size=$SIZE, asset_hash='$HASH', pending_id=null, status='ready' where id=$ID and status!='deleted'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject set revision=revision+1 where id='$SUBJECTID'"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update account set view_revision=view_revision+1, show_revision=show_revision+1 where emigo_id='$EMIGO'"
          else
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi
        else
          logger "transcode video failed [$TRANSFORM]"
          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
        fi 
      done
    fi
  ) 203>/opt/diatum/lock/transcode_video
}

function transcode_frame {
  (
    flock -n 204 # transcode in progress if fails
    if [ "$?" == "0" ]; then
      while [ true ]; do
        FRAME=`mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "select subject_asset.id, subject_asset.asset_id, subject.subject_id, account.emigo_id, subject_asset.original_id, subject_asset.transform, subject.id from subject_asset inner join subject on subject_asset.subject_id = subject.id inner join account on account.id = subject.account_id where (subject_asset.status='pending' or subject_asset.status='processing') and subject_asset.transform like 'F%' order by subject_asset.id asc limit 1"`
        if [ "$FRAME" == "" ]; then
          break;
        fi

        tokens=( $FRAME )
        ID="${tokens[0]}"
        ASSET="${tokens[1]}"
        SUBJECT="${tokens[2]}"
        EMIGO="${tokens[3]}"
        SOURCE="${tokens[4]}"
        TRANSFORM="${tokens[5]}"
        SUBJECTID="${tokens[6]}"

        logger "transcode frame [$TRANSFORM]: $1/$EMIGO/$SUBJECT/$ASSET"
        if [ -f "$1/$EMIGO/$SUBJECT/$SOURCE" ]; then 

          if [[ "$TRANSFORM" =~ ^F01- ]]; then
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='processing' where id=$ID and status!='deleted'"
            TS=${TRANSFORM#F01-}
            ffmpeg -ss 0 -i $1/$EMIGO/$SUBJECT/$SOURCE -y -vframes 1 -q:v 2 /tmp/frame.jpg 2> /dev/null 
            ffmpeg -ss $TS -i $1/$EMIGO/$SUBJECT/$SOURCE -y -vframes 1 -q:v 2 /tmp/frame.jpg 2> /dev/null 
            if [ "$?" == "0" ]; then
          DD=`identify -format "%[fx:min(w,h)]" /tmp/frame.jpg`
              convert -strip /tmp/frame.jpg -auto-orient -gravity center -crop ${DD}x${DD}+0+0 +repage -resize 360x360\> $1/$EMIGO/$SUBJECT/$ASSET
              if [ "$?" == "0" ]; then
                SIZE=`du --bytes $1/$EMIGO/$SUBJECT/$ASSET | cut -f1`
                HASH=`sha256sum $1/$EMIGO/$SUBJECT/$ASSET | awk '{ print $1 }'`
                DATE=`date +%s`
                mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set created=$DATE, asset_size=$SIZE, asset_hash='$HASH', pending_id=null, status='ready' where id=$ID and status!='deleted'"
		mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject set revision=revision+1 where id='$SUBJECTID'"
                mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update account set view_revision=view_revision+1, show_revision=show_revision+1 where emigo_id='$EMIGO'"
              else
                logger "transcode frame failed1 [$TRANSFORM]"
                mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
              fi
            else
              logger "transcode frame failed2 [$TRANSFORM]"
              mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
            fi 
          else
            logger "transcode frame failed3 [$TRANSFORM]"
            mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
          fi
        else
          logger "transcode frame failed1 [$TRANSFORM]"
          mysql -u $USERNAME -p$PASSWORD -h $HOST $DATABASE -sN -e "update subject_asset set status='failed' where id=$ID and status!='deleted'"
        fi
      done
    fi
  ) 204>/opt/diatum/lock/transcode_frame
}


transcode_photo $1 &
transcode_audio $1 &
transcode_video $1 &
transcode_frame $1 &
