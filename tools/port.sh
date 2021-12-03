COREDB_PORT=$1
timeout 5s nc -l $COREDB_PORT > /dev/null &
NC_PID=$!
sleep 1
curl -X PUT "https://portal.diatum.net/app/device/port?value=$COREDB_PORT" 2> /dev/null > /dev/null
wait $NC_PID
RET=$?
if [ "$RET" != "0" ]; then
  echo "port $COREDB_PORT not accessible"
fi
exit $RET

