#!/bin/bash/

#Starts up docker selenium hub with chrome node to run Selenium Tests
#Author atomicfruitcake

#Go to location of test suite
#cd /<Path to test suite>

#Start Selenium grid with chrome and firefox
docker-compose up

#Run the selenium tests
java -jar <tests>.jar

#Send results
if [ ! -f results.html ]; then
    rm -f results.html
fi

touch results.html
cp /test-output/html/index.html results.html

#Cleanup
docker-compose down

