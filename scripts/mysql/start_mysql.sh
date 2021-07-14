#!/bin/bash

# https://hub.docker.com/_/mysql
# https://www.cloudsavvyit.com/10703/how-to-run-mysql-in-a-docker-container/

docker run --name mysql \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -v $HOME/mysql-data:/var/lib/mysql \
  -p 3306:3306 \
  -d \
  mysql:8.0

docker ps | grep mysql

