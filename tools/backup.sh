set -e

sudo rm -rf backup
mkdir -p backup
mysqldump -u root -proot coredb > backup/coredb.sql
sudo cp -r /opt/diatum/keystore backup/
sudo cp -r /opt/diatum/asset backup/
sudo chown -R ubuntu:ubuntu backup
rm -f backup.tgz
tar -czvf backup.tgz backup

