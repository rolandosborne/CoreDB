set -e

sleep 1
echo "50"
PORTAL=$1
LOGIN=$2
AVAILABLE=`curl $PORTAL/device/login?login=$LOGIN 2> /dev/null` 
if [ "$AVAILABLE" == "true" ]; then
  exit 0
fi

exit 1
  
