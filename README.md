# CRM Project

客戶關係管理（Customer Relationship Management）系統的 **後端 RESTful API 專案**（API only，不含前端畫面）。

本專案是以 Spring Boot 建構的單體式（monolithic）Web 服務範例，提供公司（Company）與客戶（Client）資料的 CRUD 管理功能，並以 JWT 搭配 Spring Security 實作無狀態（stateless）的登入、登出與角色權限控管。資料庫與 Redis 皆採用內嵌（embedded）方式執行，**不需安裝任何外部服務即可啟動**，適合作為示範、學習或快速原型開發使用；也已容器化並部署至 GCP Cloud Run。

## 專案類型

| 項目 | 說明 |
|------|------|
| 類型 | 後端 RESTful API 服務（無前端） |
| 架構 | 單體式 Spring Boot 應用，分層架構（Controller → Service → DAO → Entity） |
| 業務領域 | CRM：公司與客戶資料管理 |
| 認證方式 | JWT（Bearer Token）+ Redis 註銷機制，Stateless Session |
| 授權方式 | 角色制（RBAC），依 HTTP Method 區分權限 |
| 資料儲存 | H2 In-Memory Database（重啟後資料重置） |
| API 文件 | OpenAPI 3 / Swagger UI |
| 部署方式 | 可執行 Jar、Docker 容器、GCP Cloud Run |

## 使用技術

- **語言 / 建置**：JDK 25、Apache Maven
- **框架**：Spring Boot 4.0、Spring Web MVC、Spring Security、Spring Data JPA、Spring Data Redis、Spring Boot Actuator、Bean Validation
- **資料庫**：H2 Database（in-memory）
- **快取 / Token 儲存**：Embedded Redis
- **認證**：java-jwt（auth0，HMAC512 簽章）、BCrypt 密碼雜湊
- **可觀測性**：Micrometer Tracing（Brave），Log 輸出 traceId / spanId
- **API 文件**：springdoc-openapi（Swagger UI）
- **其他**：Jackson、Lombok、Guava、Logback
- **測試**：JUnit 5、Mockito、Spring MockMvc
- **開發工具**：IntelliJ IDEA、Git、Sourcetree、Postman、Docker

## 功能與實作重點

- 使用 Spring Boot 快速搭建 RESTful API，遵循 REST 設計原則（POST、GET、PUT、DELETE），提升 API 可讀性及易於管理
- 使用 Spring Data JPA 作為 ORM 實現，持久化資料表操作
- 使用 Spring Security 統一管理角色訪問權限
- 基於 JWT 作為唯一識別的認證 / 授權方式，並搭配 Redis 實現 JWT 註銷（登出）功能
- 善用 Java 新版語法特性（lambda、Optional、Stream、switch expression 等），簡化開發流程
- 良好的封裝及明確的變數名稱，遵守 SRP 單一職責原則
- 整合 Docker 容器化，可部署至任何雲平台，目前部署於 GCP Cloud Run
- 為 Service 與 API 權限撰寫 Unit Test / Web Test，確保功能不因擴充而受影響

## 專案結構

```
src/main/java/com/sample/crm
├── api
│   ├── controller   # REST API 進入點（Auth、Company、Client）
│   ├── domain       # Request / Response DTO
│   └── service      # 業務邏輯
├── dao
│   ├── entity       # JPA Entity（Company、Client、Employee）
│   └── repository   # Spring Data JPA Repository（*Dao）
├── system
│   ├── config       # Security、JWT Filter、Logout、Redis、Swagger 設定
│   ├── domain       # 角色、常數、登入使用者資訊
│   └── service      # JWT 簽發 / 驗證、UserDetailsService
└── util             # 共用工具
src/main/resources
├── application.yml      # 應用程式設定
├── schema.sql           # 資料表結構（啟動時建立）
├── data.sql             # 初始資料（公司與測試帳號）
└── logback-spring.xml   # Log 設定
```

## 資料模型

| 資料表 | 說明 | 主要欄位 |
|--------|------|----------|
| `company` | 公司 | `id`、`name`、`address`、建立 / 更新人員與時間 |
| `client` | 客戶，隸屬於某公司 | `id`、`company_id`（FK → `company.id`）、`name`、`email`、`phone`、建立 / 更新人員與時間 |
| `employee` | 系統使用者（登入帳號） | `username`、`password`（BCrypt）、`role` |

啟動時會自動執行 `schema.sql` 與 `data.sql`，預先建立兩筆公司資料（Apple、Microsoft）與三組測試帳號。

## API 一覽

所有 API 皆位於 context path `/crm-project` 之下。

| Method | Path | 說明 |
|--------|------|------|
| POST | `/auth/login` | 登入，回傳 JWT 字串 |
| POST | `/auth/logout` | 登出，註銷目前的 JWT |
| GET | `/companies` | 查詢公司清單 |
| POST | `/companies` | 新增公司 |
| GET | `/companies/{id}` | 查詢單一公司 |
| PUT | `/companies/{id}` | 更新公司 |
| DELETE | `/companies/{id}` | 刪除公司 |
| GET | `/clients` | 查詢客戶清單 |
| POST | `/clients` | 新增客戶 |
| POST | `/clients/collection` | 一次新增多筆客戶 |
| GET | `/clients/{id}` | 查詢單一客戶 |
| PUT | `/clients/{id}` | 更新客戶 |
| DELETE | `/clients/{id}` | 刪除客戶 |

完整的 Request / Response 格式請參考 Swagger UI。

## Token-based Authentication

- Authentication / Authorization
- Stateless authentication
- 實現登入、登出、授權請求

### 認證流程

1. 呼叫 `POST /auth/login` 並帶入帳號密碼，驗證成功後回傳 JWT（有效期限 30 分鐘），同時將 Token 存入 Redis。
2. 之後的請求於 Header 帶入 `Authorization: Bearer <JWT>`。
3. 伺服器驗證 JWT 簽章與效期，並比對 Redis 中該使用者的 Token，一致才視為已登入。
4. 呼叫 `POST /auth/logout` 時會刪除 Redis 中的 Token，該 JWT 即刻失效。

> 補充：
> - 同一帳號重新登入會覆蓋 Redis 中的 Token，舊 Token 隨即失效（同一時間僅一組有效 Token）。
> - JWT 簽章金鑰於每次啟動時隨機產生，服務重啟後所有既有 Token 皆會失效。

### 角色權限

權限依 HTTP Method 判斷：

| HTTP Method | 允許角色 |
|-------------|----------|
| GET | 所有人 |
| POST（新增） | `superuser`、`operator` |
| PUT（修改）、DELETE（刪除） | `superuser`、`manager` |

## 測試帳號

- username：`superuser`、`manager`、`operator`
- password：`pwd`
- role：
  - superuser：可使用所有功能
  - manager：可修改 / 刪除 / 查詢 company 與 client 資料
  - operator：可新增 / 查詢 company 與 client 資料

## 建置與執行

### 環境需求

- JDK 25
- Apache Maven
- Docker（選用）

### 本機執行

```bash
mvn spring-boot:run
```

預設使用 `local` profile（Log 輸出至 console）；如需使用 `dev` profile（Log 輸出至 `logs/` 目錄）：

```bash
mvn spring-boot:run -Pdev
```

### 執行測試

```bash
mvn test
```

> 測試會啟動 Embedded Redis（port 6379），請確認該 port 未被佔用。

### Docker 建置與執行

```bash
mvn clean package -Dmaven.test.skip=true
docker build -t crm-project:v0.0.1 .
docker run -d -p 8080:8080 crm-project:v0.0.1
```

### 服務啟動後可使用的位址

| 項目 | URL |
|------|-----|
| Swagger UI | [http://localhost:8080/crm-project/swagger-ui/index.html](http://localhost:8080/crm-project/swagger-ui/index.html) |
| H2 Console | [http://localhost:8080/crm-project/h2-console](http://localhost:8080/crm-project/h2-console)（JDBC URL：`jdbc:h2:mem:crmdb`，帳號 / 密碼：`sa` / `sa`） |
| Health Check | [http://localhost:8080/crm-project/actuator/health](http://localhost:8080/crm-project/actuator/health) |

### 雲端平台

- [SwaggerUI（GCP Cloud Run）](https://crm-project-jvi6knj3iq-de.a.run.app/crm-project/swagger-ui/index.html "SwaggerUI")
