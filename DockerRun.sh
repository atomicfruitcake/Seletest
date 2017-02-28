#!/bin/bash/

#Starts up docker selenium hub with chrome node
#Author atomicfruitcake

echo "Please ensure Docker image is deleted from virtualbox GUI"

# Remove old machine
docker-machine rm -f default

# Create a new machine called default using a virtualbox driver
docker-machine create --driver virtualbox default
eval "$(docker-machine env default)"

# Print the machines IP (should be 192.168.99.100 by default on boot2docker, or localhost if not)
docker-machine ip default

# Spin up the Selenium Grid
docker-compose up