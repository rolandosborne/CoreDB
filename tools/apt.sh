set -e

echo "0"
echo "update packages" >> /home/ubuntu/install.log
sudo apt-get update >> /home/ubuntu/install.log 2>&1
echo "10"
echo "install net-tools" >> /home/ubuntu/install.log
sudo apt-get -y install net-tools >> /home/ubuntu/install.log 2>&1
echo "install fail2ban" >> /home/ubuntu/install.log
sudo apt-get -y install fail2ban >> /home/ubuntu/install.log 2>&1
echo "install jq" >> /home/ubuntu/install.log
sudo apt-get -y install jq >> /home/ubuntu/install.log 2>&1
echo iptables-persistent iptables-persistent/autosave_v4 boolean false | sudo debconf-set-selections
echo iptables-persistent iptables-persistent/autosave_v6 boolean false | sudo debconf-set-selections
sudo apt-get -y install iptables-persistent
echo "30"
echo "install tomcat9" >> /home/ubuntu/install.log
sudo apt-get -y install tomcat9 >> /home/ubuntu/install.log 2>&1
echo "50"
echo "install mariadb-server" >> /home/ubuntu/install.log
sudo apt-get -y install mariadb-server >> /home/ubuntu/install.log 2>&1
echo "60"
echo "install certbot" >> /home/ubuntu/install.log
sudo apt-get -y install certbot >> /home/ubuntu/install.log 2>&1
echo "70"
echo "install imagemagik" >> /home/ubuntu/install.log
sudo apt-get -y install imagemagick-6.q16 >> /home/ubuntu/install.log 2>&1
echo "80"
echo "install ffmpeg" >> /home/ubuntu/install.log
sudo apt-get -y install ffmpeg >> /home/ubuntu/install.log 2>&1
echo "90"
echo "upgrade" >> /home/ubuntu/install.log
sudo apt-get -y upgrade >> /home/ubuntu/install.log 2>&1
echo "100"

