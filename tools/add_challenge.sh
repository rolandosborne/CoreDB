#!/bin/bash
#add_challenge.sh

DDNS=`cat /data/diatum/ddns`
TOKEN=`cat /data/diatum/token`
curl -X PUT "$DDNS/device/challenge?token=$TOKEN&name=_acme-challenge&value=$CERTBOT_VALIDATION" 2> /dev/null
