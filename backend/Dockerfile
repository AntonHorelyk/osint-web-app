    FROM gradle:8.14.0-jdk21-alpine AS build
    WORKDIR /app
    COPY . .
    RUN gradle --no-daemon clean shadowJar
    
    FROM eclipse-temurin:21-jre-alpine
    WORKDIR /app
    COPY --from=build /app/build/libs/*-all.jar app.jar
    EXPOSE 8080
    ENTRYPOINT ["java","-jar","/app/app.jar"] 