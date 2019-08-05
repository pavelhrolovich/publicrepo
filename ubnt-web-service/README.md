# UBNT homework: pushstream webservice

## Prerequisites

- Java 1.8+
- Maven 3.2+
- Memory settings: for a day operation should run on 512 Xmx setting

## Installation

### Standalone application
Execute maven packaging goal:
- package server: mvn clean package
- run server from the target directory:
    - cd /target
    - java -DXmx512m -Xms256m -jar ubnt-homework.jar

### Deploy to tomcat
- package server: mvn clean package -Ptomcat
- I recommend to have at least 512m available
- deploy WAR application to tomcat instance, force application to start

## How to test the service 

Swagger UI is configured on the server. Please look /swagger-ui.html

## Pros and cons

The solution is memory based, so it require 512M of heap in order to run without having OOM errors. 
The exact memory setting is not known and is not in scope of the task to determine proper memory settings. 
The server does not have any cleanup of the in-memory storage, it is not required for a homework like this. 
The server has a tradeoff between memory consumption and user experience in favor of memory consumption, so the
 server exposes live view of current stats to the user and require paging. This might lead for API to return
  inconsistent results over pages. 
   
## Sample application

It is deployed to AWS. 
See http://ec2dashboard.eu-central-1.elasticbeanstalk.com/swagger-ui.html
