#!/bin/bash

# https://hub.docker.com/_/redis

docker run --name redis \
  -p 6379:6379 \
  -d \
  redis:latest

docker ps | grep redis

