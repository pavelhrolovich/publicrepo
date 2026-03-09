# The EC2 Dashboard API Project

## Prerequisites

- Java 1.8+
- Maven 3.2+
- AWS 

## Installation

Execute maven packaging goal:
- mvn clean package
- deploy application using AWS BeansTalk or whatever to AWS cloud


## Sample application

See http://ec2dashboard.eu-central-1.elasticbeanstalk.com/swagger-ui.html

- create user with R/O access to AWS EC2 service (AmazonEC2ReadOnlyAccess policy) in your AWS profile
- get access token for your user using OAuth2 protocol (You can use swagger if you'd like)
    - post to http://ec2dashboard.eu-central-1.elasticbeanstalk.com/oauth/token
    - basic auth: aws-dashboard-client & aws-dashboard-secret
    - grant_type: password
    - username & password: your AWS user credentials
- using the token execute the service REST API 

PLEASE NOTE THE SERVER DOES NOT USE HTTPS CONNECTION SO ALL YOUR DATA WILL BE TRANSMITTED AS A PLAIN TEXT.