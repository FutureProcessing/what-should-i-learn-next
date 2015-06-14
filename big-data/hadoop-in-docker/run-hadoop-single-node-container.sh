#! /bin/bash

docker run -d --name hadoop -v /home/docker-user:/docker-share -p 2122:2122 -p 8020-8050:8020-8050 -p 8088:8088 -p 19888:19888 -p 49707:49707 -p 50000-50100:50000-50100 -h $HOSTNAME hadoop-image
