How to install Hadoop in cluster
================================

Example showing how to install simple Master-Slave cluster on two Ubuntu machines.

On Master and Slave instances
-----------------------------

## Create hadoop user
```
sudo adduser hadoop

#Add sudo privileges for hadoop user 
sudo visudo

#In file type:
hadoop  ALL=(ALL:ALL)   ALL
#Save by CTRL+X, followed by 'Y'

# Change shell for hadoop user

sudo chsh -s /bin/bash hadoop

```

## Install Hadoop and its dependencies
```  bash
sudo apt-get update
sudo apt-get install curl -y

# install Java
sudo apt-get install openjdk-7-jdk
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

# download native support
mkdir -p /tmp/native
curl -Ls http://dl.bintray.com/sequenceiq/sequenceiq-bin/hadoop-native-64-2.7.0.tar | tar -x -C /tmp/native

# hadoop
sudo curl -s http://www.eu.apache.org/dist/hadoop/common/hadoop-2.7.0/hadoop-2.7.0.tar.gz | sudo tar -xz -C /usr/local/
cd /usr/local
sudo ln -s ./hadoop-2.7.0 hadoop


#Edit file `/etc/environment` 
vim /etc/environment

#and add there following environment variables:

JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64"
HADOOP_PREFIX="/usr/local/hadoop"
HADOOP_COMMON_HOME="/usr/local/hadoop"
HADOOP_HDFS_HOME="/usr/local/hadoop"
HADOOP_MAPRED_HOME="/usr/local/hadoop"
HADOOP_YARN_HOME="/usr/local/hadoop"
HADOOP_CONF_DIR="/usr/local/hadoop/etc/hadoop"
YARN_CONF_DIR="/usr/local/hadoop/etc/hadoop"
PATH=(... leave as it is ...)
#..........

sudo sed -i '/^export JAVA_HOME/ s:.*:export JAVA_HOME=/usr/java/default\nexport HADOOP_PREFIX=/usr/local/hadoop\nexport HADOOP_HOME=/usr/local/hadoop\n:' $HADOOP_PREFIX/etc/hadoop/hadoop-env.sh
sudo sed -i '/^export HADOOP_CONF_DIR/ s:.*:export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop/:' $HADOOP_PREFIX/etc/hadoop/hadoop-env.sh

sudo rm -rf /usr/local/hadoop/lib/native
sudo mv /tmp/native /usr/local/hadoop/lib




```






