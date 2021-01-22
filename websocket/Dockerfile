FROM adoptopenjdk/openjdk11:ubi

COPY build/libs/*.jar websocket.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-XX:MaxRAM=72m", "-Xmx300m", "-Xss512k", "/websocket.jar"]