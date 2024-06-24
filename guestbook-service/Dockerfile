FROM azul/zulu-openjdk-alpine
MAINTAINER Wang Qing <wq@jfrogchina.com>
COPY target/guestbook-service.jar guestbook-service.jar
ENTRYPOINT ["java", "-jar", "/guestbook-service.jar"]
EXPOSE 2222