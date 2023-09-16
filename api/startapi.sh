#!/bin/bash
echo "-----Starting api-----"

echo "-----mvn clean install-----"
mvn clean install

echo "-----starting the jar-----"
java -jar target/sports.scraper-0.0.1-SNAPSHOT.jar &