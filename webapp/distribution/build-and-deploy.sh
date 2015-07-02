#!/bin/bash

IMAGE_TAG=$1
DOCKER_HOST=$2:2375

HOST_BUILD=$DOCKER_HOST
HOST_DEPLOY=$DOCKER_HOST
IMAGE_NAME=wsiln-cd:$IMAGE_TAG

docker -H $HOST_BUILD build -t $IMAGE_NAME .

CONTAINER_NAME="wsiln-cd"
PORT_CONFIG="-p 8081:8081"

docker -H $HOST_DEPLOY stop $CONTAINER_NAME
docker -H $HOST_DEPLOY rm -f $CONTAINER_NAME
docker -H $HOST_DEPLOY run -d --name $CONTAINER_NAME $PORT_CONFIG $IMAGE_NAME