FROM curlimages/curl:latest AS download
USER root
RUN mkdir -p /appinsight
WORKDIR appinsight
RUN curl -LJO https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.1.1/applicationinsights-agent-3.1.1.jar


# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11:ubi
# Add Maintainer Info
LABEL maintainer="nutentadev@outlook.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/api-gateway-0.0.1-SNAPSHOT.jar

RUN mkdir -p /app
COPY --from=download /appinsight/applicationinsights-agent-3.1.1.jar /app/


ADD target/*.jar /app/
ADD target/*.json /app/


# Add the application's jar to the container
# ADD ${JAR_FILE} api-gateway-0.0.1-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-javaagent:app/applicationinsights-agent-3.1.1.jar","-Djava.security.egd=file:/dev/./urandom","-jar","app/api-gateway-0.0.1-SNAPSHOT.jar"]

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway-0.0.1-SNAPSHOT.jar"]

