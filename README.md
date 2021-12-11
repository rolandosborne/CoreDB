CoreDB is a node for a decentralized network hosting digital identities based on public/private keys and not blockchain. By self-hosting your digitial identity, you control who can see your contact details, photos and videos. Your data is transmitted only between self-hosted nodes and the mobile devices of people you authorize.
  
### Installation
A [Portainer template](https://github.com/rolandosborne/CoreDB/blob/main/tools/template.json) and a [Docker Compose stack](https://github.com/rolandosborne/CoreDB/blob/main/tools/docker-stack.yaml) is proivded for ARM64 & AMD64. The stack allows for using a shared MariaDB instance while the template has it bundled within the container. It's assumed you have a reverse proxy like nginx-proxy-manager to provide HTTPS. Let me know if there is a specific archtecture or orchestration tool you would like support and I will add it.

### Tutorial
<p align="center"><sub>Below is a video walkthrough on how to install a node and use IndiView in a basic selfhosting setup.</sub></p>
<p align="center">
  <a href="https://s3.us-west-2.amazonaws.com/org.coredb.indiview/IndiView_Tutorial.mp4"><img src="/doc/photos/turtorial.png" width="70%"/></a>
</p>  

### Issues
If you encounter any problems installing or using the selfhosted node, please let me know by posting in the discussions tab in the 'Help' category.

### Contribute
Any feedback on the design, usability, features or bugs is greatly appreciated. For those with coding experience, the backend code is written with Go and the frontend is written with ReactJS.
