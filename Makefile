setup:
	chmod +x mvnw

build: setup
	./mvnw clean package

start: setup
	./mvnw spring-boot:run

runjar: setup build
	java -jar target/takehometest-0.0.1-SNAPSHOT.jar