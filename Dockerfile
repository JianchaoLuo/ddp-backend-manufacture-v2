#FROM maven:3.9.5 as build
#WORKDIR /app
#COPY pom.xml .
#COPY deploy-maven-settings.xml .
#RUN mvn install -f pom.xml -settings deploy-maven-settings.xml
#COPY . .
#RUN mvn package -f pom.xml -DskipTests

FROM openjdk:21
ENV TZ=Asia/Shanghai
COPY target/ddp-backend-manufacture-v2-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:MinRAMPercentage=70.0","-XX:MaxRAMPercentage=70.0","-jar","/app.jar","--spring.profiles.active=test"]
