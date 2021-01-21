FROM adoptopenjdk/openjdk11:ubi

COPY build/libs/*.jar api.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Xmx300m", "/api.jar"]