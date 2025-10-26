FROM gradle:8-jdk21 as builder 

WORKDIR /app

COPY . .

RUN ["./gradlew", "build" ,"-x", "test"]

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
RUN useradd worker 
RUN chown -R worker:worker /app
USER worker 
CMD ["java","-jar","app.jar"]

