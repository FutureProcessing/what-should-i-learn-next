#! /bin/bash

sudo apt-get update
sudo apt-get install rpm -y

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




