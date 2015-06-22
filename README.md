What Should I Learn Next
========================

# What it is
This is a sample Proof of Concept of operating on BIG DATA using Hadoop and Elastic.

### What it does
Front-end application allows user to search for technologies that he knows and basing on that suggesting 
another technologies that he might be interested in. 

### How
Data is downloaded from StackOverflow Data Dump
[https://archive.org/details/stackexchange]

It is then inserted into Hadoop HDFS and processed by Hadoop job. Results are put into Elastic index.

# DIY tutorial
Try to use this example yourself

## Prepare Environment

### Install Hadoop
You can use Docker container to setup Hadoop very quickly for PoC. 
This image will help you: [Hadoop on Docker](https://registry.hub.docker.com/u/sequenceiq/hadoop-docker/)

Here is tutorial how to run [Hadoop Single Node in Docker](big-data/hadoop-in-docker/README.md)

If you want to install Hadoop manually, you can use [this  cheatsheet](big-data/hadoop-instalation/Hadoop Cluster - manual instalation cheatsheet.md).

### Install Elastic
You can use this Docker image [Elastic](https://registry.hub.docker.com/_/elasticsearch/)
Expose ports 9200 and 9300


## Download data 
Download data dump from [StackOverlow](https://archive.org/details/stackexchange)
In PoC we're using only stackoverflow.com-Posts\Posts.xml
 
## Put Posts.xml into HDFS
The easiest way to put Posts.xml int hdfs.
1. Copy Posts.xml onto machine where Hadoop is installed.
2. Put Posts.xml into hadoop
```
cd $HADOOP_HOME;
$HADOOP_PREFIX/bin/hadoop fs -put Posts.xml /user/root/input/Posts.xml
```
It'll put Posts.xml file into HDFS into directory /user/root/input

## Prepare JAR file with job
Go to big-data/map-reduce directory and compile and packagemap-reduce job:
```
cd big-data/map-reduce
mvn clean package
```
It'll create a file map-reduce-1.0-SNAPSHOT-jar-with-dependencies.jar.
Copy it onto a Hadoop machine.

## Prepare indexes in Elastic
```
cd big-data/elastic-upload
npm install
node initializeIndices.js elasticHost:9200 technologies
```

## Execute Haddop job
On Hadoop machine start Hadoop job
```
cd $HADOOP_HOME
$HADOOP_PREFIX/bin/hadoop jar map-reduce-1.0-SNAPSHOT-jar-with-dependencies.jar  /user/root/input/Posts.xml --elasticHost=yourElasticHost --indexName=technologies
```

Job will execute and results will be uploaded to Elastic index.

## Host frontend application
```
cd webapp
npm install
grunt develop
```


