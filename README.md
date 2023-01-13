# CRM Project

## tools/technologies 使用技術列表
- JDK17
- Apache Maven
- Spring Boot 3.0.1
- Spring Boot actuator
- Spring Data JPA
- Micrometer Tracing
- H2 Database
- Embedded-redis
- jjwt
- Jackson
- JUnit5
- Lombok
- SwaggerUI 3.0
- Intellij IDEA
- Git
- Sourcetree
- Postman
- Docker

## implementation
- 使用spring-boot快速搭建RESTFul API
- 整合docker容器化，可部署至任何雲平台
- 部署至GCP Cloud Run
- 遵循REST設計原則，POST，GET，PUT，DELETE，簡化API可讀性及易於管理
- 使用spring-data-jpa作為ORM實現，持久化資料表操作
- 使用jdk1.8特性，lambda、optional，簡化開發流程及順暢度
- 運用繼承觀念，減少重複性代碼的產生
- 良好的封裝及明確的變數名稱，遵守SRP單一職責原則
- 使用Spring Security統一管理角色訪問權限。
- 基於jwt作為SSO唯一識別的認證/授權方式
- 搭配redis實現jwt註銷功能。
- 為function撰寫unit test，確保功能不因擴充而影響

## Token-based authentication
- Authentication/Authorization
- stateless authentication
- 實現登入、登出、授權請求

## User 測試帳號
- username: `superuser`, `manager`, `operator`
- password: `pwd`
- role:
  - superuser: access to all functions.
  - manager: modify/delete/view company/client data.
  - operator: create/view company/client data.

## Deploy and Run
##### spring-boot run:
- `mvn spring-boot:run`  
##### dokcer image build:
- `mvn clean package -Dmaven.test.skip=true`
- `docker build -t crm-project:v0.0.1 .`
##### docker image run :
- `docker run -d -p 8080:8080 crm-project:v0.0.1`
- [Open Local SwaggerUI](http://localhost:8080/crm-project/swagger-ui/index.html "Local SwaggerUI")
##### cloud platform:
- [SwaggerUI](https://crm-project-jvi6knj3iq-de.a.run.app/crm-project/swagger-ui/index.html "SwaggerUI")