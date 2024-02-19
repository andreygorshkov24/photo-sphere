#!/bin/bash
./gradlew clean build
cd build/libs
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar foto-sphere-1.0-SNAPSHOT.jar
