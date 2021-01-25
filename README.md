#№№ Spring REST Service
[![Build Status](https://travis-ci.com/denisRudie/job4j-rest-service.svg?branch=main)](https://travis-ci.com/denisRudie/job4j-rest-service)
[![codecov](https://codecov.io/gh/denisRudie/job4j-rest-service/branch/main/graph/badge.svg)](https://codecov.io/gh/denisRudie/job4j-rest-service)

### О проекте
Spring CRUD
-приложение. Позволяет получить информацию по аккаунтам каждого работника, добавить/изменить/удалить аккаунт. Реализовано на Spring: 2 контроллера, которые взаимодействуют друг с другом через RestTemplate. Данные хранятся в БД Postgres.

### Technologies
* Java 14
* Spring (Boot, Data, Web(RestTemplate))
* Postgres
* Junit, Mockito
* Maven

### REST API
##### employees:
| команда                                 | запрос                                                                | ответ                                              |
|-----------------------------------------|-----------------------------------------------------------------------|----------------------------------------------------|
| получить всех работников и их аккаунты | GET ```https://sitename/employee/``` | Код состояния:```200``` Header: ```"Content-Type: application/json"```  Body: ```[{"id":1,"firstName":"denis","lastName":"petrov","inn":123321123321123,"hired":null,"accounts":[{"login":"parsentev","password":"123"}]}]``` |
| добавить новый аккаунт работника | POST ```https://sitename/employee/1``` Header: ```"Content-Type: application/json"``` Body: ```{"login": "test","password": "123","employee": {"id": "1"}}``` | Код состояния:```200``` Body: ```{"id": "1", "login": "test","password": "123","employee": {"id": "1"}}```|
| обновить аккаунт работника | PUT ```https://sitename/employee/1```  Header: ```"Content-Type: application/json"``` Body: ```{"id": "1", "login": "test","password": "123","employee": {"id": "1"}}``` | Код состояния:```200``` |
| удалить аккаунт работника | DELETE ```https://sitename/employee/1``` | Код состояния:```200``` |
