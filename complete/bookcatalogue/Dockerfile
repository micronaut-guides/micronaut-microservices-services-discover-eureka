FROM openjdk:14-alpine
COPY build/libs/bookcatalogue-*-all.jar bookcatalogue.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "bookcatalogue.jar"]