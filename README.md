# Spring Webflux Sample
- Kotlin (Functional Endpoint With Coroutine)
- Spring Boot
- Spring Webflux
- Spring R2DBC (R2dbc-MySql)
- Spring Security
- OpenAPI 3.0 (springdoc)
- JWT Authentication (jjwt)
- Logging (slf4j)
- Tests (JUnit, WebTestClient)

### Getting Started
1. set .env in resources</br>
  1.1 format
   ```js
   database_host="your_db_host"
   database_port=3306 //your_db_port
   database_name="your_db_name"
   database_username="your_db_username"
   database_password="your_db_password"
   ```

2. generate base64 private/public keys</br>
  2.1 execute generateKeyPair.sh
   ```shell
   cd src/main/resources
   bash ./generateKeyPair.sh
    ```

