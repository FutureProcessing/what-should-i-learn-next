#!/bin/bash

IMAGE_TAG=wsiln-cd:1
HOST_BUILD=vm-wsiln-02:2375
IMAGE_NAME=wsiln-cd:1

docker -H $HOST_BUILD build -t $IMAGE_NAME .
#docker -H $HOST_BUILD push $IMAGE_NAME
#docker -H $HOST_BUILD rmi $IMAGE_NAME

HOST_DEPLOY=vm-wsiln-02:2375

CONTAINER_NAME="wsiln-cd"
PORT_CONFIG="-p 80:8081"

docker -H $HOST_DEPLOY stop $CONTAINER_NAME
docker -H $HOST_DEPLOY rm -f $CONTAINER_NAME
docker -H $HOST_DEPLOY run -d --name $CONTAINER_NAME $PORT_CONFIG $IMAGE_NAME
