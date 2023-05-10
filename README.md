![image](https://github.com/mkhlktvnk/Clevertec-News-Management-System/assets/70900496/986694de-a291-45d6-8d30-909ab575d119)

# News Management System 📰

## Project Description ✍️

#### This project consists of two parts: an API for managing news and comments, which allows users to view, add, edit, and delete news and comments based on their role, as well as an API for authentication, which allows users to register, log in, and validate previously issued access tokens.

## Technologies Used 🚀
* Java 
* Gradle
* Spring Boot
* PostgreSQL
* Liquibase
* Docker
* Redis / Custom in-memory cache

## API 🌐
### Authorization server:
| Http Method | URI            | Description                                                        |
|-------------|----------------|--------------------------------------------------------------------|
| GET         | /auth/validate | Access token validation                                            |
| POST        | /auth/token    | Issuing an access token according to the user's authorization data |
### News management server (resource):
| Http Method | URI                             | Description                                                                   |
|-------------|---------------------------------|-------------------------------------------------------------------------------|
| GET         | /api/v0/news                    | Retrieve news with pagination and optional full-text search                   |
| GET         | /api/v0/news/{newsId}/comments/ | Retrieve comments by news ID with pagination and optional full text search    |
| GET         | /api/v0/news/{newsId}           | Retrieve news by newsId                                                       |
| GET         | /api/v0/comments/{commentId}    | Retrieve comment by commentId                                                 |
| POST        | /api/v0/news                    | Create news                                                                   |
| POST        | /api/v0/comments                | Add comment to news (newsId should be in request body)                        |
| PATCH       | /api/v0/news/{newsId}           | Partially update news (only those fields that are present in request body)    |
| PATCH       | /api/v0/comments/{commentId}    | Partially update comment (only those fields that are present in request body) |
| DELETE      | /api/v0/news/{newsId}           | Remove news by id                                                             |
| DELETE      | /api/v0/comments/{commentId}    | Remove comment by id                                                          |  
## Database structure 💽
### Users database:
```postgresql
    CREATE TABLE IF NOT EXISTS roles
    (
        id        SERIAL PRIMARY KEY,
        authority VARCHAR(255) NOT NULL UNIQUE
    );
    
    CREATE TABLE IF NOT EXISTS users
    (
        id       SERIAL PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS users_roles
    (
        user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
        role_id INTEGER REFERENCES roles (id) ON DELETE CASCADE,
        PRIMARY KEY (user_id, role_id)
    );
```
### News database:
```postgresql
    CREATE TABLE IF NOT EXISTS news
    (
        id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        username VARCHAR(255) NOT NULL,
        time  TIMESTAMP    NOT NULL DEFAULT NOW(),
        title VARCHAR(255) NOT NULL,
        text  TEXT         NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS comments
    (
        id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        time     TIMESTAMP    NOT NULL DEFAULT NOW(),
        username VARCHAR(255) NOT NULL,
        text     TEXT         NOT NULL,
        news_id  BIGINT       NOT NULL REFERENCES news (id)
    );
```
