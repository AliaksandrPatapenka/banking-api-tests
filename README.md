# Фреймворк для API тестирования (RestAssured + JUnit 5) с Telegram-уведомлениями

Автоматизированные тесты для REST API банковского сервиса с использованием **RestAssured**, **JUnit 5**, **Allure Reports** и интеграцией с **Jenkins**. Реализованы генерация тестовых данных, валидация JSON Schema, проверка данных в **PostgreSQL**, проверка **Kafka**-событий и отправка уведомлений в Telegram о результатах сборки.

Фреймворк покрывает тестами REST API банковского сервиса (блоки users и accounts, а также Kafka-события), развёрнутого локально.

---

## Используемые технологии

| Технология | Версия |
|:-----------|:-------|
| **Java** | 17 | 
| **RestAssured** | 5.3.0 |
| **JUnit 5** | 5.10.0 |
| **Jackson** | 2.15.2 |
| **JSON Schema Validator** | 5.3.0 |
| **Allure Framework** | 2.25.0 |
| **PostgreSQL (JDBC)** | 42.7.3 |
| **Kafka Clients** | 3.5.1 |
| **Maven** | 3.x |
| **Jenkins** | - |

---

## Структура проекта

```
src/test/java/com/apiAuto/
├── common/                      # Общие настройки и утилиты
│   ├── config/                  # Конфигурация (CommonData, DbConfig, KafkaConfig, Specs)
│   ├── constants/               # Общие константы (HttpStatus, KafkaConst)
│   └── helpers/                 # Вспомогательные классы (DbUtils, DbAssert, HttpAssert,
│                                # KafkaHelper, RequestTemplate, JsonContext, CommonDataGenerator)
│
└── presentation/                # Слой тестирования банковского API
    ├── constants/
    │   ├── endpoints/           # Endpoints (UsersEndpoints, AccountEndpoints)
    │   ├── kafka/               # Kafka-константы (TopicKafka, UserKafkaConst, AccountKafkaConst)
    │   ├── queryParam/          # Query-параметры (UserQueryParam, AccountQueryParam)
    │   ├── schemasPatchs/       # Пути к JSON Schema (UserSchemas, AccountSchemas, ErrorSchemas)
    │   ├── sql/                 # SQL-запросы (UserSql, AccountSql)
    │   └── testData/            # Тестовые данные (UserData, AccountData)
    │
    ├── dto/                     # POJO-модели для запросов (CreateUserDto)
    │
    ├── helpers/                 # Вспомогательные классы
    │   ├── accountHelper/       # Помощники для счетов (AccountSteps, AccountTemplate, AccountDb)
    │   ├── testHelper/          # Генерация данных, очистка БД (PresentationDataGenerator, PresentationDbCleanup)
    │   └── userHelper/          # Шаблоны и работа с БД для пользователей (UserTemplate, UserJsonTemplate, UserDb)
    │
    ├── properties/              # Свойства тестов (PresentationTestProperties)
    │
    └── test/                    # Тест-классы
        ├── accounts/            # Тесты для блока accounts (создание, пополнение, списание)
        ├── kafka/               # Тесты Kafka-событий (UserKafkaTest, AccountKafkaTest)
        └── users/               # Тесты для блока users (создание, получение по логину)

src/test/resources/
├── schemas/                     # JSON Schema для валидации ответов
│   ├── errorSchema/             # Схемы для ошибок (400)
│   └── presentation/
│       ├── accountSchema/       # Схемы для счетов (create, deposit, withdraw)
│       └── userSchema/          # Схемы для пользователей (create, by login)
├── allure.properties            # Настройки Allure
├── db.properties                # Настройки подключения к PostgreSQL
├── junit-platform.properties    # Настройки запуска JUnit
└── local.properties             # Локальные настройки (игнорируется в Jenkins)
```

## Команды для запуска

**Запуск всех тестов и генерация Allure-отчета:**
```
mvn clean test; allure generate target/allure-results --clean -o allure-report; allure open allure-report

```

### Параллельный запуск тестов

В проекте настроен параллельный запуск тестов для ускорения выполнения:
- **JUnit уровень** — `junit-platform.properties`
- **Maven уровень** — `maven-surefire-plugin`

---

## CI/CD (Jenkins)

Проект интегрирован с Jenkins. Пайплайн (`Jenkinsfile`) поддерживает параметризированную сборку:

**Параметры сборки:**
- `REPO_URL` — ссылка на тестируемый репозиторий репозитория
- `BRANCH_NAME` — ветка тестируемого репозитория
- `TEST_SUITE` — пакет тестов
- `BASE_URL` — базовый URL API
- `BASE_PATHS` — базовый путь API

---

## Telegram-уведомления

Jenkins-пайплайн отправляет уведомления в Telegram о статусе сборки:

- 🚀 Тесты **ЗАПУШЕНЫ!**
- ✅ Тесты отработали **УСПЕШНО!**
- ⚠️ Тесты **УПАЛИ!**
- ❌ Тесты **НЕ ЗАПУСТИЛИСЬ!**

---

## Allure-отчетность

После выполнения тестов генерируется детальный Allure-отчет.

---

# Чеклист покрытия тестами API

## Позитивные тесты

| № метода | № кейса | Метод | Название теста | Статус код |
|----------|---------|-------|----------------|------------|
| 1 | | POST /users — Создание пользователя | | |
| | Case1.1 | POST /users | Создание пользователя без привязки друзей | 200 |
| | Case1.2 | POST /users | Создание пользователя с привязкой 3 друзей | 200 |
| 2 | | GET /users/{login} — Получение пользователя по логину | | |
| | Case2.1 | GET /users/{login} | Получение пользователя по существующему в БД логину | 200 |
| 3 | | POST /accounts — Создание счёта | | |
| | Case3.1 | POST /accounts | Создание счёта у пользователя, у которого отсутствуют счёта | 200 |
| | Case3.2 | POST /accounts | Создание счёта у пользователя, у которого уже есть счёт | 200 |
| 4 | | POST /accounts/{id}/deposit — Пополнение счёта | | |
| | Case4.1 | POST /accounts/{id}/deposit | Пополнение счета (max значение) | 200 |
| | Case4.2 | POST /accounts/{id}/deposit | Пополнение счета (min значение) | 200 |
| 5 | | POST /accounts/{id}/withdraw — Списание со счёта | | |
| | Case5.1 | POST /accounts/{id}/withdraw | Списание части баланса со счёта при достаточном балансе | 200 |
| | Case5.2 | POST /accounts/{id}/withdraw | Списание всего баланса со счёта при достаточном балансе | 200 |
| | Case5.3 | POST /accounts/{id}/withdraw | Списание со счёта нулевого значения (баланс нет нулевой) | 200 |

## Негативные тесты

| № метода | № кейса | Метод | Название теста | Статус код |
|----------|---------|-------|----------------|------------|
| 1 | | POST /users — Создание пользователя | | |
| | Case1.2 | POST /users | Создание пользователя с существующим в базе данных логином | 400 |
| 2 | | GET /users/{login} — Получение пользователя по логину | | |
| | Case2.1 | GET /users/{login} | Получение пользователя по несуществующему в БД логину | 400 |
| 3 | | POST /accounts — Создание счёта | | |
| | Case3.1 | POST /accounts | Создание счёта для несуществующего пользователя | 400 |
| 4 | | POST /accounts/{id}/deposit — Пополнение счёта | | |
| | Case4.1 | POST /accounts/{id}/deposit | Пополнение счета (отрицательное значение) | 400 |
| 5 | | POST /accounts/{id}/withdraw — Списание со счёта | | |
| | Case5.1 | POST /accounts/{id}/withdraw | Списание суммы, превышающей текущий баланс (не нулевой) | 400 |
| | Case5.2 | POST /accounts/{id}/withdraw | Списание со счёта при нулевом балансе | 400 |

---

## Лицензия

Этот проект является демонстрационным и не имеет лицензии. Используйте на свой страх и риск:)
