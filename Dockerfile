FROM adoptopenjdk/openjdk11
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY start.sh .
RUN chmod +x start.sh
CMD sh start.sh
# ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "app.jar"]
EXPOSE 8080
