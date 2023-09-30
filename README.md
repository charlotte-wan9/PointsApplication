# fetch-test
## To Run:
- Please execute these steps in the directory ```/fetch-test/```

### Using Makefile
- To build the application and run JUnit tests: ```make build```
- To start the Spring Boot Web Application: ```make start```
- To start the Spring Boot Web Application using executable jar: ```make runjar```

### If Makefile doesn't work
- Please run ```chmod +x mvnw``` **FIRST** prior to any command below to give permission to mvnw script, thanks!
- Then, build the application and run JUnit tests with ```./mvnw clean package```
- Lastly, start the Spring Boot Web Application on server port 8000 with ```./mvnw spring-boot:run```, OR run ```java -jar target/takehometest-0.0.1-SNAPSHOT.jar```

### Thank you for reviewing!