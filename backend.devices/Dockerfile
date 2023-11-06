FROM maven:3.8.3-openjdk-17 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/
COPY ./checkstyle.xml /root/
WORKDIR /root
RUN mvn package
RUN java -Djarmode=layertools -jar /root/target/backend.devices-1.0-SNAPSHOT.jar list
RUN java -Djarmode=layertools -jar /root/target/backend.devices-1.0-SNAPSHOT.jar extract
RUN ls -l /root

FROM openjdk:17-jdk-slim-buster

ENV TZ=UTC
#ENV DB_IP=0.0.0.0
#ENV DB_PORT=5432
#ENV DB_USER=postgres
#ENV DB_PASSWORD=roota

COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/snapshot-dependencies/ ./

RUN sleep 10
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher","-XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m"]