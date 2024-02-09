# Monitoring-Service

Веб-сервис для подачи показаний счетчиков отопления, горячей и холодной воды

---

### Описание

- Показания можно подавать один раз в месяц.
- Ранее поданные показания редактировать запрещено.
- Последние поданные показания считаются актуальными.
- Пользователь может видеть только свои показания, администратор может видеть показания всех пользователей.
- Реализация соответствует описанным ниже требованиям и ограничениям.

---

### Требования

- предусмотреть расширение перечня подаваемых показаний
- данные хранятся в памяти приложения
- приложение должно быть консольным (никаих спрингов)
- регистрация пользователя
- авторизация пользователя
- реализовать эндпоинт для получения актуальных показаний счетчиков
- реализовать эндпоинт подачи показаний
- реализовать эндпоинт просмотра показаний за конкретный месяц
- реализовать эндпоинт просмотра истории подачи показаний
- реализовать контроль прав пользователя
- Аудит действий пользователя (авторизация, завершение работы, подача показаний, получение истории подачи показаний и
  тд)

### Нефункциональные требования

Unit-тестирование

Доп. материалы: https://drive.google.com/drive/folders/1rZaq58yAAcBKZqBeNSfpc6kb-GkuqHs0?usp=sharing

---

## Стек:

> Java 17, Maven, Postgresql, Lombok, JUnit, Mockito, AssertJ, Testcontainers

---

## Запуск приложения:

1) Находясь в корневой директории поднять БД
   > `docker compose up`
2) Собрать проект
   > `mvn clean package`
3) Запустить сгенерированный jar-файл
   > `java -jar application/ConsoleApp/target/ConsoleApp-1.0-SNAPSHOT-jar-with-dependencies.jar`
