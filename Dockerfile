# define base docker image
FROM openjdk:17
LABEL maintainer="hieutt"
ADD target/the-bookshelf-0.0.1-SNAPSHOT.jar the-bookshelf-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/the-bookshelf-0.0.1-SNAPSHOT.jar"]
