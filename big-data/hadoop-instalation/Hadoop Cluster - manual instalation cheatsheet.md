How to install Hadoop in cluster
================================

Example showing how to install simple Master-Slave cluster on two Ubuntu machines.

# On Master and Slave instances

## Prerequisites
 
### Add hadoop user with SU privileges

### Configure passwordless ssh to localhost
``` bash
ssh-keygen -q -N "" -t rsa -f /home/hadoop/.ssh/id_rsa
cp /home/hadoop/.ssh/id_rsa.pub /home/hadoop/.ssh/authorized_keys
```

### Configure passwordless ssh to to other mashines from master to slaves
Append `/home/hadoop/.ssh/id_rsa.pub` from master to `/home/hadoop/.ssh/authorized_keys` on slave nodes 

### Install Java
```  bash
sudo apt-get update
sudo apt-get install openjdk-7-jdk
```

### Make sure there isn't an entry for your hostname mapped to 127.0.0.1 or 127.0.1.1 in /etc/hosts
Otherwise the ConnectionRefused exception will occur when nodemanagers from slaves try to connect to master.
More explanation here: http://wiki.apache.org/hadoop/ConnectionRefused

## Hadoop Instalation

### Define following environment variables:
``` 
JAVA_HOME="/path/to/your/java/home"
HADOOP_HOME=/usr/local/hadoop
HADOOP_PREFIX="/usr/local/hadoop"
HADOOP_COMMON_HOME="/usr/local/hadoop"
HADOOP_HDFS_HOME="/usr/local/hadoop"
HADOOP_MAPRED_HOME="/usr/local/hadoop"
HADOOP_YARN_HOME="/usr/local/hadoop"
HADOOP_CONF_DIR="/usr/local/hadoop/etc/hadoop"
YARN_CONF_DIR="/usr/local/hadoop/etc/hadoop"
```

### Install Hadoop
``` bash   
# hadoop
sudo wget http://www.eu.apache.org/dist/hadoop/common/hadoop-2.7.0/hadoop-2.7.0.tar.gz 
sudo tar -xzf hadoop-2.7.0.tar.gz -C /usr/local
cd /usr/local
sudo chown -R hadoop hadoop-2.7.0
sudo ln -s ./hadoop-2.7.0 hadoop
   
# download native support
mkdir -p /tmp/native
wget http://dl.bintray.com/sequenceiq/sequenceiq-bin/hadoop-native-64-2.7.0.tar
tar -xf hadoop-native-64-2.7.0.tar -C /tmp/native

# fixing the libhadoop.so like a boss
sudo rm -rf /usr/local/hadoop/lib/native
sudo mv /tmp/native /usr/local/hadoop/lib

#Make executable scripts for running hadoop
ls -la /usr/local/hadoop/etc/hadoop/*-env.sh
sudo chmod +x /usr/local/hadoop/etc/hadoop/*-env.sh
ls -la /usr/local/hadoop/etc/hadoop/*-env.sh

#Make hadoop owner of hadoop files
cd /usr/local
sudo chown hadoop hadoop-2.7.0
sudo chown hadoop hadoop
```

## Hadoop Configuration

Edit `$HADOOP_PREFIX/etc/hadoop/core-site.xml` and put there host name of your master machine
``` XML 
 <configuration>
      <property>
          <name>fs.defaultFS</name>
          <value>hdfs://HOSTNAME:8020</value>
      </property>
  </configuration>
```

Edit `$HADOOP_PREFIX/etc/hadoop/hdfs-site.xml`: 
``` xml
<configuration>    
    <property>
        <name>dfs.replication</name>
         <!-- put here value of how many nodes your want Hadoop to replicate data to -->
        <value>2</value>          
    </property>
    
    <property>
        <name>dfs.namenode.rpc-bind-host</name>             
        <value>0.0.0.0</value>          
    </property>
    
    <property>
        <name>dfs.namenode.servicerpc-bind-host</name>             
        <value>0.0.0.0</value>          
    </property>
        
    <!-- WHY this?  Here's the answer: http://log.rowanto.com/why-datanode-is-denied-communication-with-namenode/ -->    
    <property>
        <name>dfs.namenode.datanode.registration.ip-hostname-check</name>
        <value>false</value>
    </property>
</configuration>
```

Edit `$HADOOP_PREFIX/etc/hadoop/mapred-site.xml`:
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

Edit `$HADOOP_PREFIX/etc/hadoop/yarn-site.xml`: with your master hostname.

``` xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property> 
    
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>MASTER_HOSTNAME</value>
    </property>

    <property>
      <name>yarn.application.classpath</name>
      <value>/usr/local/hadoop/etc/hadoop, /usr/local/hadoop/share/hadoop/common/*, /usr/local/hadoop/share/hadoop/common/lib/*, /usr/local/hadoop/share/hadoop/hdfs/*, /usr/local/hadoop/share/hadoop/hdfs/lib/*, /usr/local/hadoop/share/hadoop/mapreduce/*, /usr/local/hadoop/share/hadoop/mapreduce/lib/*, /usr/local/hadoop/share/hadoop/yarn/*, /usr/local/hadoop/share/hadoop/yarn/lib/*</value>
    </property>

    <property>
      <name>yarn.nodemanager.delete.debug-delay-sec</name>
      <value>600</value>
    </property>
</configuration>
```

# Only On Master

## Make hadoop master-node aware of slave nodes 
Edit `$HADOOP_PREFIX/etc/hadoop/slaves` and put there your slave hosts.
``` bash
localhost
slave-host
```

## Start and Initialize HDFS
``` bash
$HADOOP_PREFIX/bin/hdfs namenode -format

$HADOOP_PREFIX/sbin/start-dfs.sh
$HADOOP_PREFIX/bin/hdfs dfs -mkdir -p /user/hadoop
```

## Start YARN
```
$HADOOP_PREFIX/sbin/start-yarn.sh
```

## Verify running processes
`jps` command on Master should result in something like:
```
13629 DataNode
15059 ResourceManager
15514 Jps
13830 SecondaryNameNode
15194 NodeManager
```

`jps` on slave:
```
10243 NodeManager
10343 Jps
9506 DataNode
```

You can also visit `http://master-host:50070` to verify hdfs configuration and nodes.
Checkout `http://master-host:8088` to verify resource manager works correctly and has all child nodes attached
