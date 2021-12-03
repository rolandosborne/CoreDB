DROP USER 'root'@'localhost';
CREATE USER 'root'@'%' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* to 'root'@'%';
FLUSH PRIVILEGES;
use mysql
UPDATE user SET password=PASSWORD("root") WHERE User='root';
FLUSH PRIVILEGES;

