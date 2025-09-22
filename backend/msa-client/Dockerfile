FROM eclipse-temurin:21-jdk

WORKDIR /opt/app

COPY build/libs/*.jar msa-client.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "msa-client.jar"]