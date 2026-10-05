FROM maven:3.6.3-openjdk-8 as builder
COPY src/ src/
COPY pom.xml pom.xml
RUN mvn package -Dmaven.test.skip

FROM java:8 as runner
ENV DB_URL=$DB_URL
ENV DB_USER=$DB_USER
ENV DB_PASS=$DB_PASS
COPY --from=builder target/fruit_shop2.0.jar fruit_shop2.0.jar
ENTRYPOINT ["java", "-jar", "/fruit_shop2.0.jar"]