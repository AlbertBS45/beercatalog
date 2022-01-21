FROM openjdk:11
ADD target/beercatalog.jar beercatalog.jar
ENTRYPOINT ["java", "-jar","beercatalog.jar"]
EXPOSE 8080