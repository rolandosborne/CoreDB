#!/bin/bash
#portal.sh

DDNS=`cat /data/diatum/ddns`
TOKEN=`cat /data/diatum/token`
curl -X PUT "$DDNS/device?token=$TOKEN" 2> /dev/null
