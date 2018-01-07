# Stocks app - Future Processing recruitment process

## Requirements 
* node ^4.2.6
* npm ^3.5.2
* [See more https://nodejs.org/en/]

## How to build&run
* mvn clean install -P linux-dev - if you are using Linux
* mvn clean install -P windows-dev - if you are using Windows
* mvn spring-boot:run

## Important notes
* If you want to disable h2 console - take a look at application.properties
* The h2 console is enable by default
* If you want to execute sql command - go to localhost:8080/h2 and use credentials from application.properties to login
