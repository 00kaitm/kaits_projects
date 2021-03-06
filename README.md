# Fruit Shop API
Online Item Shop Application 
## Project Description
This Shop API is a list of fruits (items) and users that can be assigned to an itemOwner. Different users (regular users and admin users) can login to access special features. 

## Technologies Used
- Java
- Maven
- Log4J
- JDBC
- Spring Boot 
- Spring MVC
- Spring Data JPA
- PostgreSQL
- JUnit
- Mockito
- Docker
- Promtail
- Loki
- Grafana
- Prometheus

## Features
Users can: 
   - see fruits they own

Admin users can: 
   - see all fruits
   - add new fruits
   - update fruits
   - delete fruits

## To-do list:
- Add more user login functionality 
- More metric monitoring 

## Getting Started
- Clone Repository using Git Bash

`git clone https://github.com/00kaitm/kaits_projects.git`

* Create enviornment variables for your own database credentials(DB_URL,DB_USER,DB_PASS)
* Navigate to repository on local device. Then navigate to fruit_shop2.0 folder
> `cd fruit-shop2.0`
* Run docker-compose.yml to package and containerize
> `docker-compose up -d`

## Usage 
    As an Admin, I can view all items.
        GET /fruit
    As an Admin, I can add a new item.
        POST /fruit
    As an Admin, I can update a item.
        PUT /fruit/{id}
    As an Admin, I can view items by ID.
        GET /fruit/{id}
    As an Admin, I can delete an item.
        DELETE /fruit/{id}

