 # APP
 FROM azul/zulu-openjdk:17-latest
 WORKDIR /app

 COPY build/libs/cafree-1.0.0-SNAPSHOT.jar .

 EXPOSE 8080

 USER nobody
 ENTRYPOINT [\
     "java",\
     "-jar",\
     "-Djava.security.egd=file:/dev/./urandom",\
     "-Dsun.net.inetaddr.ttl=0",\
     "-Dspring.profiles.active=prod",\
     "cafree-1.0.0-SNAPSHOT.jar"\
 ]
 