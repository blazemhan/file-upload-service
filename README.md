#  File Upload Service

A RESTful File Upload API built with **Java 17** and **Spring Boot 3.x** that allows
users to upload, retrieve, and delete files.

##  Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 In-Memory Database
- Maven
- SpringDoc OpenAPI (Swagger UI)

##  How to Build and Run

### Prerequisites
- Java 17+ installed
- Maven 3.8+ installed

 Swagger UI\
After starting the application, access the interactive API documentation at:

http://localhost:8080/swagger-ui.html


POST	/files/upload	-Upload a file\
GET	/files/{id}	  -Retrieve file metadata by ID\
DELETE	/files/{id}	-Delete a file by ID

## 📏 Upload Limits & Supported Formats

| Rule | Value |
|-|--|
| Max file size | **5 MB** |
| Allowed types | `jpg`, `png`, `pdf` |
|   

🗃 H2 Console
Access the H2 database console at:

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:filedb
Username: sa
Password: (empty)

## Assumptions
Files are stored in an uploads/ directory created at the project root.\
The H2 in-memory database is used — data is lost on restart.\
Unique filenames are generated using UUID to avoid collisions\
The mock virus scan rejects any file with "virus" in its original filename.
### Build
```bash  
mvn clean install  
mvn spring-boot:run
