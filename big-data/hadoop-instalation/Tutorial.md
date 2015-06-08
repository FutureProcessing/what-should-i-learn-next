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

sudo sed -i '/^export JAVA_HOME/ s:.*:export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64\nexport HADOOP_PREFIX=/usr/local/hadoop\nexport HADOOP_HOME=/usr/local/hadoop\n:' $HADOOP_PREFIX/etc/hadoop/hadoop-env.sh
sudo sed -i '/^export HADOOP_CONF_DIR/ s:.*:export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop/:' $HADOOP_PREFIX/etc/hadoop/hadoop-env.sh

# fixing the libhadoop.so like a boss
sudo rm -rf /usr/local/hadoop/lib/native
sudo mv /tmp/native /usr/local/hadoop/lib

#Make executable scripts for running hadoop
ls -la /usr/local/hadoop/etc/hadoop/*-env.sh
sudo chmod +x /usr/local/hadoop/etc/hadoop/*-env.sh
ls -la /usr/local/hadoop/etc/hadoop/*-env.sh


cd /usr/local
sudo chown hadoop hadoop-2.7.0
sudo chown hadoop hadoop
```

## Configure Hadoop

Edit `core-site.xml` and put there host name of your machine
``` XML 
 <configuration>
      <property>
          <name>fs.defaultFS</name>
          <value>hdfs://HOSTNAME:8020</value>
      </property>
  </configuration>
```

Edit `$HADOOP_PREFIX/etc/hadoop/hdfs-site.xml` 
``` xml
<configuration>    
    <property>
        <name>dfs.replication</name>
         <!-- put here value of how many nodes your want Hadoop to replicate data to -->
        <value>1</value>          
    </property>

    <!-- WHY this?  Here's the answer: http://log.rowanto.com/why-datanode-is-denied-communication-with-namenode/ -->    
    <property>
        <name>dfs.namenode.datanode.registration.ip-hostname-check</name>
        <value>false</value>
    </property>

</configuration>
```

Edit `$HADOOP_PREFIX/etc/hadoop/mapred-site.xml` with your hostname.
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>

```


Edit `$HADOOP_PREFIX/etc/hadoop/yarn-site.xml`: 

``` xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property> 

    <property>
      <name>yarn.application.classpath</name>
      <value>/usr/local/hadoop/etc/hadoop, /usr/local/hadoop/share/hadoop/common/*, /usr/local/hadoop/share/hadoop/common/lib/*, /usr/local/hadoop/share/hadoop/hdfs/*, /usr/local/hadoop/share/hadoop/hdfs/lib/*, /usr/local/hadoop/share/hadoop/mapreduce/*, /usr/local/hadoop/share/hadoop/mapreduce/lib/*, /usr/local/hadoop/share/hadoop/yarn/*, /usr/local/hadoop/share/hadoop/yarn/lib/*</value>
    </property>

    <property>
    <description>
      Number of seconds after an application finishes before the nodemanager's
      DeletionService will delete the application's localized file directory
      and log directory.

      To diagnose Yarn application problems, set this property's value large
      enough (for example, to 600 = 10 minutes) to permit examination of these
      directories. After changing the property's value, you must restart the
      nodemanager in order for it to have an effect.

      The roots of Yarn applications' work directories is configurable with
      the yarn.nodemanager.local-dirs property (see below), and the roots
      of the Yarn applications' log directories is configurable with the
      yarn.nodemanager.log-dirs property (see also below).
    </description>
    <name>yarn.nodemanager.delete.debug-delay-sec</name>
    <value>600</value>
  </property>

</configuration>
```

## Start and Initialize HDFS
```
$HADOOP_PREFIX/bin/hdfs namenode -format

$HADOOP_PREFIX/sbin/start-dfs.sh
$HADOOP_PREFIX/bin/hdfs dfs -mkdir -p /user/hadoop

```

## Start YARN
```
$HADOOP_PREFIX/sbin/start-yarn.sh
```

