FROM openjdk:14-alpine
COPY build/libs/bookinventory-*-all.jar bookinventory.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "bookinventory.jar"]