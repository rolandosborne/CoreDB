set -e

sleep 1
ping -c 1 diatum.org
if [ "$?" == "0" ]; then
  exit 0;
fi
exit 1
