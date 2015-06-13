soccer-model
==============

Test assignment for the swissQuote
Total implementation time is about 6 hours (I decided to take a bit more time than expected in order to show a bit more of my code)


Running
========

In order to run the project you need Maven (tested with version 3.3.1) and Java 8 (test on OS X with Java 1.8.0_05)

To compile the entire project, run "mvn package".

To run the application, run "mvn spring-boot:run" and open http://localhost:8080/ .

Additional ways to run are
1) java -jar target/soccer-model-1.0-SNAPSHOT.jar after the project is built
2) deploy target/soccer-model-1.0-SNAPSHOT.jar as a WAR file on any Servlet 3.0 container
3) within your favorite IDE import Maven project and run/debug min method in the com.maxmatveev.soccer.Bootstrap class

Filter results by using League/Team filter
Sort results by clicking on the table headers

More info
==========

Backend of the application is built using Spring Boot framework for the startup code and dependency injection
Data layer consists of Spring JDBC Template library and in-memory HSQLDB database
External database could be used by supplying proper application.properties configuration file

CSV file is parsed by the means of Apache Commons CSV library

UI is built using Vaadin framework

Google Guava is used to simplify boilerplate code required to work with collections

Limitations
============

Because of the time limitations following parts need improvement
1) There are no integration tests so DAO classes & SQL queries are not tested (persistence was not expected anyways)
2) UI is implemented in the single class without proper separation as it is built only to add some UI to the app
2a) UI is not tested for the reason above
3) There is no i18n for the frontend
4) UI is not mobile-friendly
5) Sorting on the UI is done in Java instead of SQL
