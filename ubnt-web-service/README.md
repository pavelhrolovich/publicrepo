# Pushstream webservice

## Prerequisites

- Java 1.8+
- Maven 3.2+
- Memory settings: for a day operation should run on 512mb as Xmx setting

## Installation

### Standalone application
Execute maven packaging goal:
- package server: mvn clean package
- run server from the target directory:
    - cd /target
    - java -DXmx512m -Xms256m -jar ubnt-homework.jar

### Deploy to tomcat
- package server: mvn clean package -Ptomcat
- deploy WAR application to tomcat instance, force application to start

## How to test the service 

Swagger UI is configured on the server. Please look /swagger-ui.html

## Pros and cons

The solution is memory based, so on a long run it require 512M of heap in order to run without having OOM errors. 
The exact memory setting is not known and is not in scope of the task to determine proper memory settings. Also, the server does not have any cleanup of the in-memory storage, it is not required for a homework like this. 
The server exposes live view of current stats to the user and require to use paging to fetch all records via API
. This might lead for API to return inconsistent results over pages. 