FROM azul/zulu-openjdk-alpine
MAINTAINER Wei Li <liwei2151284@163.com>
COPY guestbook-service/target/guestbook-service.jar guestbook-service.jar
ENTRYPOINT ["java", "-jar", "/guestbook-service.jar"]
EXPOSE 2222