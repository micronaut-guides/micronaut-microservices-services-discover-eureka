# Tests 

Run Eureka.

cd ../eureka$
eureka $ ./gradlew bootRun

Run the microservices

./gradlew run -parallel 

Open a new terminal and run both functional and acceptance test

./gradlew test