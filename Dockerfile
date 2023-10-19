# Stage 2: Указываем базовый образ с установленной Java (JRE) для запуска приложения
FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} e-quality-boot-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/e-quality-boot-0.0.1-SNAPSHOT.jar"]
