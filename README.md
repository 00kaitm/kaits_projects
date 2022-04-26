# Item Shop API
Online Fruit Shop Application 
## Project Description
This Shop API is a list of fruits (items) and users that can be assigned to an itemOwner. Different users (regular users and admin users) can login to access special features. 

## Technologies Used
- Java
- Spring Boot 
- Spring MVC
- Spring Data JPA
- PostgreSQL
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

To-do list:
- Add more user login functionality 
- More metric monitoring 

## Getting Started
- Clone Repository using Git Bash

`git clone https://github.com/00kaitm/kaits_projects.git`

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

