# Stocks app - Future Processing recruitment process

## Requirements 
* java ^1.8.0_151
* Apache Maven 3.3.9
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
* If you want to change amount of stocks - go to localhost:8080/h2, use credentials form application.properties and execute query on STOCK table, example UPDATE STOCK
                                                                                                                         SET AMOUNT = 1000