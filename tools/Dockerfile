FROM ubuntu:20.04
LABEL maintainer="rolandosborne@diatum.org"

RUN apt-get update

ENV TZ=America/Los_Angeles
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get -y install curl
RUN apt-get -y install net-tools
RUN apt-get -y install fail2ban
RUN apt-get -y install jq
RUN apt-get -y install vim
RUN apt-get -y install netcat
RUN apt-get -y install imagemagick-6.q16
RUN apt-get -y install ffmpeg
RUN apt-get -y install tomcat9
RUN apt-get -y install mariadb-client
RUN apt-get -y install mariadb-server
RUN apt-get -y install unzip
RUN apt-get -y install wget

RUN apt-get -y upgrade

ADD tomcat9.service /lib/systemd/system/

RUN mkdir -p /usr/share/tomcat9/temp/
RUN mkdir -p /usr/share/tomcat9/logs/
RUN chown tomcat:tomcat /usr/share/tomcat9/logs
RUN mkdir -p /usr/share/tomcat9/webapps/
RUN mkdir -p /usr/share/tomcat9/conf
ADD web.xml /usr/share/tomcat9/conf/
ADD server.xml /usr/share/tomcat9/conf/
ADD tomcat-users.xml /usr/share/tomcat9/conf/

ADD .vimrc /root

RUN mkdir -p /opt/diatum
ADD transcode.sh /opt/diatum
ADD garbage.sh /opt/diatum

ADD minor.version /opt/diatum/
ADD major.version /opt/diatum/
ADD setup.sql /opt/diatum/
ADD baseline.sql /opt/diatum/
ADD V01__noop.sql /opt/diatum/
ADD V02__messaging.sql /opt/diatum/
ADD V03__portal.sql /opt/diatum/
ADD configure.sh /root
RUN mkdir -p /opt/diatum/lock
RUN chown tomcat:tomcat /opt/diatum/lock

RUN mkdir -p /data/asset
RUN mkdir -p /data/keystore
RUN mkdir -p /data/config

RUN cp -r /var/lib/mysql /root/

RUN wget https://github.com/rolandosborne/CoreDB/releases/latest/download/app.war
RUN mv app.war /usr/share/tomcat9/webapps/ROOT.war
ADD tomcat.tar.gz /root/
#ADD app.war /usr/share/tomcat9/webapps/ROOT.war

ENTRYPOINT ["/root/configure.sh"]

