# SpringCrud 🚀

A robust, production-ready **Spring Boot** RESTful API application demonstrating complete **CRUD** (Create, Read, Update, Delete) operations for managing a product inventory. 

This application leverages modern technologies such as **Java 26**, **Spring Boot 4.0.5**, **Hazelcast Distributed Caching**, **Jetty Application Server**, **MySQL Database**, **Actuator Metrics**, and **Springdoc OpenAPI (Swagger)** for comprehensive API documentation.

---

## 🛠️ Tech Stack & Features

*   **Language & Runtime:** Java 26
*   **Framework:** Spring Boot 4.0.5
*   **Web Server:** Embedded Jetty (replaces default Tomcat server for optimized performance)
*   **Database:** MySQL (Production) & H2 (In-memory testing)
*   **Caching:** Hazelcast Spring Cache (Distributed cache for fast query retrieval and cache invalidation)
*   **API Documentation:** Springdoc OpenAPI / Swagger UI
*   **Validation:** Jakarta Bean Validation (`@NotNull`, `@Size`, `@Min`, etc.)
*   **Monitoring & Metrics:** Spring Boot Actuator
*   **Unit & Integration Testing:** Spring Boot WebMVC Test, JpaTest, H2 in-memory DB

---

## 📂 Project Structure & Class Blueprint

### Directory Tree

Below is the layout of the project, including key packages and source files:

```text
SpringCrud/
├── src/
│   ├── main/
│   │   ├── java/com/springcrud/
│   │   │   ├── SpringCrudApplication.java             # Main Application Entry Point
│   │   │   ├── config/
│   │   │   │   └── ProductCacheConfig.java            # Hazelcast Caching Beans Config
│   │   │   ├── controller/
│   │   │   │   └── productRestController.java         # REST Controller exposing endpoints
│   │   │   ├── entites/
│   │   │   │   └── product.java                       # Product JPA Entity (with annotations)
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java        # Exception Handler Stub
│   │   │   └── repos/
│   │   │       └── Repository.java                    # JPA Repository for DB interaction
│   │   └── resources/
│       └── application.properties                 # Spring Boot settings & cache mapping
│   └── test/
│       └── java/com/springcrud/
│           ├── ProductRestControllerMvcTest.java      # MockMvc Controller Endpoint tests
│           └── SpringCrudApplicationTests.java        # Spring Context verification tests
├── pom.xml                                            # Maven configuration and dependencies
└── README.md                                          # Project documentation
```
### Classes Blueprint

1. **[SpringCrudApplication.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/SpringCrudApplication.java)**
   * **Role:** Application Bootstrap.
   * **Details:** Launches the embedded Jetty server and initializes the Spring application context.

2. **[ProductCacheConfig.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/config/ProductCacheConfig.java)**
   * **Role:** Cache Manager configuration.
   * **Details:** Declares and registers Hazelcast configuration and manager instances. Specifies default `product-cache` details (e.g. TTL of 3000 seconds).

3. **[productRestController.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/controller/productRestController.java)**
   * **Role:** REST Controller Layer.
   * **Details:** Listens to HTTP requests on path `/products/` and interacts with database repository. Implements caching annotations (`@Cacheable` and `@CacheEvict`) and triggers validations.

4. **[product.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/entites/product.java)**
   * **Role:** Data Entity Model.
   * **Details:** Maps to the MySQL database tables, containing properties `id`, `name`, `description`, and `price` along with validation constraints (`@NotNull`, `@Size`, `@Min`). Implements `Serializable` so it can be saved in Hazelcast.

5. **[Repository.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/repos/Repository.java)**
   * **Role:** Database Repository Layer.
   * **Details:** Extends `JpaRepository<product, Integer>` to provide standard out-of-the-box DB operations (CRUD).

6. **[GlobalExceptionHandler.java](file:///d:/springboot/SpringCrud/src/main/java/com/springcrud/exception/GlobalExceptionHandler.java)**
   * **Role:** Exception Handling Stub.
   * **Details:** Place to declare global controllers' exception handlers.

---

## 📋 Prerequisites

Before running the application, make sure you have the following installed:

*   **Java Development Kit (JDK):** Version 26 or higher
*   **Apache Maven:** Version 3.8+
*   **MySQL Server:** Active instance running locally or remotely

---

## ⚙️ Configuration

The application configuration can be found and modified in `src/main/resources/application.properties`. 

```properties
spring.application.name=SpringCrud

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/springDb
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Configurations
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Context Path & Server URL
server.servlet.context-path=/api
productapi.service.url=http://localhost:8080/api/products/

# Logging
logging.file.name=log/application.log
logging.level.root=info
logging.level.org.springframework.web=DEBUG

# OpenAPI / Swagger Settings
springdoc.swagger-ui.enabled=true
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui/index.html

# Caching Configuration
spring.cache.type=hazelcast

# Actuator Endpoints Exposure
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

> ⚠️ **Note:** Ensure a database named `springDb` exists in your MySQL server before running the app. You can create it using:
> ```sql
> CREATE DATABASE springDb;
> ```

---

## 🚀 Getting Started & Running

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    cd SpringCrud
    ```

2.  **Build the Project:**
    Build the project and download all Maven dependencies:
    ```bash
    ./mvnw clean install
    ```

3.  **Run the Application:**
    Start the Spring Boot application using Jetty:
    ```bash
    ./mvnw spring-boot:run
    ```
    Once started, the application will be accessible at: `http://localhost:8080/api`

---

## 🔌 API Endpoints

All requests should be prefixed with `/api`.

| HTTP Method | Endpoint | Description | Cache Behavior |
| :--- | :--- | :--- | :--- |
| **GET** | `/products/` | Retrieve all products in the database | - |
| **GET** | `/products/{id}` | Retrieve a specific product by its ID | **Cached** (`product-cache`) |
| **POST** | `/products/` | Create a new product. ID is auto-generated | - |
| **PUT** | `/products/` | Update details of an existing product | - |
| **DELETE** | `/products/{id}` | Delete a product by its ID | **Evicted** from cache |

### Product Object Schema

```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product Description (Max 100 characters)",
  "price": 100
}
```

---

## ⚡ Performance Optimization (Hazelcast Cache)

Hazelcast is configured as the cache provider (`ProductCacheConfig.java`) to reduce database overhead for read-heavy operations:

*   **Caching Reads:** The `GET /api/products/{id}` endpoint is annotated with `@Cacheable("product-cache")` which saves retrieved records into the distributed cache.
*   **Cache Eviction on Write/Delete:** The `DELETE /api/products/{id}` endpoint is annotated with `@CacheEvict("product-cache")` to ensure stale data is removed from the cache when a product is deleted.
*   **TTL Configuration:** The default Time To Live (TTL) for cache records is set to `3000` seconds.

---

## 🛡️ Validation Rules

*   **Name:** Must not be null (`@NotNull`).
*   **Description:** Maximum length of 100 characters (`@Size(max=100)`).
*   **Price:** Minimum value of 1 (`@Min(1)`).

---

## 📖 Documentation & Monitoring

*   **Swagger UI (API Docs):** Access interactively via your browser to view and test endpoints:
    [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)
*   **Actuator Endpoint (Health/Metrics):**
    [http://localhost:8080/api/actuator](http://localhost:8080/api/actuator)

---

## 🧪 Running Tests

To execute the test suite (comprising MockMvc controller tests and repository JPA tests):
```bash
./mvnw test
```
The test suite utilizes an in-memory **H2 database** so it doesn't modify your local MySQL database.
