DNS=`cat /data/diatum/nodename`
sudo diff /etc/letsencrypt/live/$DNS/cert.pem /data/diatum/cert/cert.pem > /dev/null
if [ "$?" == "1" ]; then
  cp -L /etc/letsencrypt/live/$DNS/cert.pem /data/diatum/cert/cert.pem
  cp -L /etc/letsencrypt/live/$DNS/chain.pem /data/diatum/cert/chain.pem
  cp -L /etc/letsencrypt/live/$DNS/fullchain.pem /data/diatum/cert/fullchain.pem
  cp -L /etc/letsencrypt/live/$DNS/privkey.pem /data/diatum/cert/privkey.pem
  /usr/share/tomcat9/bin/catalina.sh stop
  /usr/share/tomcat9/bin/catalina.sh start
fi

