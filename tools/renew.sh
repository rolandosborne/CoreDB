DNS=`cat /home/ubuntu/nodename`
sudo cp -L /etc/letsencrypt/live/$DNS/cert.pem /var/lib/tomcat9/conf/cert.pem
sudo cp -L /etc/letsencrypt/live/$DNS/chain.pem /var/lib/tomcat9/conf/chain.pem
sudo cp -L /etc/letsencrypt/live/$DNS/fullchain.pem /var/lib/tomcat9/conf/fullchain.pem
sudo cp -L /etc/letsencrypt/live/$DNS/privkey.pem /var/lib/tomcat9/conf/privkey.pem
sudo chown tomcat:tomcat /var/lib/tomcat9/conf/*
sudo service tomcat9 restart

