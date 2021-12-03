set -e

sleep 1
echo "50"
PORTAL=$1
AVAILABLE=`curl -f $PORTAL/device/available 2> /dev/null` 
if [ "$AVAILABLE" -gt "0" ]; then
  exit 0
fi

exit 1
  
