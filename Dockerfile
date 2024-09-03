FROM openjdk:17
EXPOSE 1992
ADD target/expense-details.jar expense-details.jar
ENTRYPOINT ["java","-jar","expense-details.jar"]