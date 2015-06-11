FROM ubuntu:14.04

#==============
# Install ssh, npm
#==============
RUN apt-get update && apt-get install git-core openssh-server  -y wget git curl zip unzip && rm -rf /var/lib/apt/lists/*
 
#====================
# Node.js installation and npm update
#====================

RUN apt-get update --fix-missing
RUN apt-get install -y nodejs-legacy 
RUN apt-get install -y npm
RUN npm update

#==============
# Install grunt, bower
#==============
RUN npm install -g bower grunt-cli

#==============
# Copying start.sh script
#==============
COPY start.sh /usr/local/bin/start.sh
RUN  chmod +x /usr/local/bin/start.sh

#==============
# Adding app sources
#==============

ADD app.zip /var/app.zip
RUN unzip /var/app.zip -d /app
RUN cd /app/app && npm install

EXPOSE 8081	
	
CMD ["/usr/local/bin/start.sh"]
    






