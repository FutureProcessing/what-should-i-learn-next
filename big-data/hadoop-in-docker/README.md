Hadoop Single Node in Docker container.
=======================================

This instruction is based on [image provided by SequenceIQ](https://registry.hub.docker.com/u/sequenceiq/hadoop-docker/)

## How to run?

### Build image
To build an image just run [build-hadoop-single-node-image.sh] 
``` bash
./build-hadoop-single-node-image.sh
```

### Run container
To run container use this command:
``` bash
docker run -d --name hadoop -p 2122:2122 -p 8020-8050:8020-8050 -p 8088:8088 -p 19888:19888 -p 49707:49707 -p 50000-50100:50000-50100 hadoop-image
```

Or use the [prepared script](run-hadoop-single-node-container.sh).
``` bash
./run-hadoop-single-node-container.sh
```
This script also maps the `/home/hadoop-user` directory to `/docker-share` directory within container.
Modify it for your machine 

### Check
Verify that your hadoop cluster is working by opening in your browser `your-host:50070` and `your-host:8088`.

## How to use
The easiest way is to map a host directory to `/docker-share` directory within the container.
You can then just copy files you want to put into HDFS into mapped directory.

### Bash into the container
Execute on host
``` bash
docker exec -it hadoop bash
```
`hadoop` is a name of the container
