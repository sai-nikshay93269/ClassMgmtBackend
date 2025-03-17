# Spring Boot Microservices Project
(Eureka Server, Config Server, API Gateway, Kafka, File Storage, JWT, Authentication, Authorization, Redis, Docker)

# About the project

<ul style="list-style-type:disc">
  <li>This project is based Spring Boot Microservices</li>
  <li>User can register and login through auth service by user role (ADMIN or USER) through api gateway</li>
  <li>User can send any request to relevant service through api gateway with its bearer token</li>
</ul>

8 services whose name are shown below have been devised within the scope of this project.

- Config Server
- Eureka Server
- API Gateway
- Auth Service
- Job Service
- User Service
- Notification Service
- File Storage


### Used Dependencies

* Core
    * Spring
        * Spring Boot
        * Spring Security
            * Spring Security JWT
            * Authentication
            * Authorization
        * Spring Web
            * FeighClient
        * Spring Data
            * Spring Data JPA
            * PostgreSQL
        * Spring Cloud
            * Spring Cloud Gateway Server
            * Spring Cloud Config Server
            * Spring Cloud Config Client
    * Netflix
        * Eureka Server
        * Eureka Client
* Database
    * PostgreSQL
* Kafka
* Redis
* Docker 
* Validation
* File storage
* Modelmapper
* Openapi UI
* Lombok
* Log4j2


### 🔨 Run the App

<b>Local</b>

<b>1 )</b> Clone project `git clone https://github.com/devsyx/spring-boot-microservices.git`

<b>2 )</b> Go to the project's home directory :  `cd spring-boot-microservices`

<b>3 )</b> Run docker compose <b>`docker compose up`</b></b>

<b>4 )</b> Run <b>Eureka Server</b>

<b>5 )</b> Run <b>Gateway</b>

<b>6 )</b> Run <b>Config Server</b>

<b>7 )</b> Run other services (<b>auth-service</b>, <b>user-service</b>, <b>classes-service</b>, <b>notification-service</b>  and lastly <b>
file-storage</b>)

<b>8 )</b> For swagger ui localhost:8080/v1/{service-name}/swagger-ui/index.html</b>


### Screenshots

<details>
<summary>Click here to show the screenshot of project</summary>
    <p> Eureka Server</p>
    <img src ="screenshots/eureka.png" alt="">
    <p>User Service Swagger UI</p>
    <img src ="screenshots/user.png" alt="">
    <p>Job Service Swagger UI</p>
    <img src ="screenshots/category-advert.png" alt="">
    <img src ="screenshots/offer-job.png" alt="">
    <p> Auth Service Swagger UI </p>
    <img src ="screenshots/auth.png" alt="">
    <p>Notification Kafka UI</p>
    <img src ="screenshots/kafka-ui.png" alt="">
    <p>File Storage Postman</p>
    <img src ="screenshots/file-upload.png" alt="">
    <img src ="screenshots/file-download.png" alt="">
</details>
