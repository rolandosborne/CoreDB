DNS=`cat /home/ubuntu/nodename`
sudo certbot -d $DNS --agree-tos --register-unsafely-without-email --manual --preferred-challenges dns --manual-auth-hook /home/ubuntu/add_challenge.sh --manual-cleanup-hook /home/ubuntu/remove_challenge.sh --manual-public-ip-logging-ok --force-renewal certonly
