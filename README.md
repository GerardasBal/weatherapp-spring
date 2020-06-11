Weather App project


Back-end was built using Spring framework.
Back-end connects to the MySQL database.


To start the project:


  clone repository


  start MySQL database locally
  
  

run commands

  _create schema YOUR_DATABASE_NAME;_
 
  _grant all privileges on YOUR_DATABASE_NAME to 'YOUR_USER'@'%';_




edit _application.properties_ file under _src/main/resources_ according to your database setting

default DATABASE_LOCATION: localhost

default PORT: 3306

  _spring.datasource.url=jdbc:mysql://DATABASE_LOCATION:PORT/DATABASE_SCHEMA_NAME_

  _spring.datasource.username=USER_NAME_

  _spring.datasource.password=USER_PASSWORD_

