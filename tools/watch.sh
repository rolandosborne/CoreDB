CPU=`mysql -u root -proot coredb -sN -e "select processor from system order by timestamp desc limit 10"`
COUNT=`echo $CPU | wc -w`
SUM=0
if [ $COUNT -eq 10 ]; then
  for x in $CPU
  do
    SUM=$(($SUM + $x))
  done
  if [ $SUM -lt 20 ]; then
    CUR=`date +%s`
    echo "restart... $CUR" >> /home/ubuntu/restart
    sudo service tomcat9 restart
  fi
fi 

