FROM openjdk:14-alpine
COPY build/libs/bookrecommendation-*-all.jar bookrecommendation.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "bookrecommendation.jar"]