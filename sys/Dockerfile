FROM ubuntu:20.04
LABEL maintainer="roland.osborne@gmail.com"

ENV TZ=America/Los_Angeles
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update

RUN apt-get -y install curl
RUN apt-get -y install net-tools
RUN apt-get -y install jq
RUN apt-get -y install netcat
RUN apt-get -y install unzip
RUN apt-get -y install wget
RUN apt-get -y install openssh-server
RUN apt-get -y install git
RUN apt-get -y install vim

RUN apt-get -y upgrade

ADD .vimrc /root
ADD configure.sh /root

RUN mkdir -p /data

ENTRYPOINT ["/root/configure.sh"]

