# Monitoring-Service

Веб-сервис для подачи показаний счетчиков отопления, горячей и холодной воды

---

### Описание

- Показания можно подавать один раз в месяц.
- Ранее поданные показания редактировать запрещено.
- Последние поданные показания считаются актуальными.
- Пользователь может видеть только свои показания, администратор может видеть показания всех пользователей.

---

### Pull Requests

1) [Pull request "Вводная"](https://github.com/elGordoGato/Monitoring-Service/pull/1)

2) [Pull request "JDBC. Миграции БД"](https://github.com/elGordoGato/Monitoring-Service/pull/2)

3) [Pull request "Сервлеты. АОП"](https://github.com/elGordoGato/Monitoring-Service/pull/3)

4) [Pull request "Знакомство с Spring Framework"](https://github.com/elGordoGato/Monitoring-Service/pull/4)

5) [Pull request "Spring boot"](https://github.com/elGordoGato/Monitoring-Service/pull/5)

---

## Стек:

> Java 17, Spring Boot, Spring Security, Maven, Postgresql, AspectJ, MapStruct, Jackson, Lombok, JUnit, Mockito,
> AssertJ, Testcontainers

---

### Спецификация API

 - [Документация OpenAPI](http://localhost:8080/v3/api-docs)
 - [Swagger UI](http://localhost:8080/swagger-ui/index.html)

Файл со спецификацией можно найти по пути: `swagger/monitoring-service-open-api.yml`

---

## Запуск приложения:

1) Находясь в корневой директории собрать проект:
   > `mvn clean package`
2) Запустить docker compose:
   > `docker compose up`
