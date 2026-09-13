# 1. Пользователи (Presentation)

## 1.1. Создание пользователя (отличается от Проекта 1 — новые поля)

### 1.1.1 POST /users — позитивный (успешное создание)
- [х] Создать модель `CreateUserDTO` в `models/` (login, name, age, gender, hairColor, friends)
- [ ] Создать модель `OperationResponseDTO` в `models/`
- [х] Добавить эндпоинт `ENDPOINT_CREATE_USER` в `presentation/base/properties/patch/UsersPatch.java`
- [х] Добавить тест в существующий `PostUsersTest.java` (или создать новый `CreateUserNewTest.java`)
- [х] Добавить JSON schema `createUserSuccessSchema.json` в `schemas/userCrudSchema/`
- [х] Создать хелпер `UserHelper.createUserWithFriends()` для подготовки тестовых данных
- [х] Добавить проверку в БД: `UserRepository.findByLogin()` — проверка новых полей (age, gender, hairColor)
- [ ] Добавить проверку события в Kafka: `ClientEvent` (createUser)

### 1.1.2 POST /users — негативный (пользователь с таким логином уже существует)
- [ ] Переиспользовать модель из 1.1.1
- [ ] Переиспользовать эндпоинт из 1.1.1
- [ ] Добавить тест в `CreateUserNewTest.java`
- [ ] Добавить JSON schema `400userAlreadyExistsSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия дубликата в БД по логину

### 1.1.3 POST /users — негативный (друг не существует)
- [ ] Переиспользовать модель из 1.1.1
- [ ] Переиспользовать эндпоинт из 1.1.1
- [ ] Добавить тест в `CreateUserNewTest.java`
- [ ] Добавить JSON schema `400friendNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия создания пользователя в БД

### 1.1.4 POST /users — негативный (неизвестная ошибка сервера)
- [ ] Переиспользовать модель из 1.1.1
- [ ] Переиспользовать эндпоинт из 1.1.1
- [ ] Добавить тест в `CreateUserNewTest.java`
- [ ] Добавить JSON schema `500errorSchema.json` в `schemas/errorSchema/`

---

## 1.2. Получение пользователя по логину

### 1.2.1 GET /users/{login} — позитивный (существующий логин)
- [ ] Создать модель `CheckUserDTO` в `models/` (login, name, age, gender, hairColor, friends)
- [ ] Добавить эндпоинт `ENDPOINT_USERS_LOGIN` в `presentation/base/properties/patch/UsersPatch.java`
- [ ] Создать тестовый класс `GetUsersByLoginTest.java` в `test/users/`
- [ ] Добавить JSON schema `userByLoginSchema.json` в `schemas/userCrudSchema/`
- [ ] Добавить хелпер `UserHelper.getUserByLogin()` для получения токена и логина
- [ ] Добавить проверку данных в БД (JPA repository `UserRepository.findByLogin()`)

### 1.2.2 GET /users/{login} — негативный (несуществующий логин)
- [ ] Переиспользовать модель `CheckUserDTO` из 1.2.1
- [ ] Переиспользовать эндпоинт из 1.2.1
- [ ] Добавить тест в `GetUsersByLoginTest.java`
- [ ] Переиспользовать JSON schema `400notFoundSchema.json` из `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по логину

---

## 1.3. Получение пользователя по ID (отличается от Проекта 1 — путь /users/id/{id})

### 1.3.1 GET /users/id/{id} — позитивный (существующий ID)
- [ ] Создать модель `CheckUserDTO` в `models/`
- [ ] Добавить эндпоинт `ENDPOINT_USERS_ID` в `presentation/base/properties/patch/UsersPatch.java`
- [ ] Добавить тест в `GetUsersByIdTest.java` (создать новый файл)
- [ ] Добавить JSON schema `userByIdSchema.json` в `schemas/userCrudSchema/`
- [ ] Добавить проверку в БД: `UserRepository.findById()`

### 1.3.2 GET /users/id/{id} — негативный (несуществующий ID)
- [ ] Переиспользовать модель из 1.3.1
- [ ] Переиспользовать эндпоинт из 1.3.1
- [ ] Добавить тест в `GetUsersByIdTest.java`
- [ ] Переиспользовать JSON schema `400notFoundSchema.json` из `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

---

## 1.4. Управление друзьями

### 1.4.1 POST /users/friend/{login}/{friend} — позитивный (успешное добавление друга)
- [ ] Создать модель `AddFriendRequest` в `models/` (если требуется тело запроса)
- [ ] Добавить эндпоинт `ENDPOINT_ADD_FRIEND` в `presentation/base/properties/patch/UsersPatch.java`
- [ ] Создать тестовый класс `AddFriendTest.java` в `test/users/`
- [ ] Добавить JSON schema `addFriendSuccessSchema.json` в `schemas/userCrudSchema/`
- [ ] Создать хелпер `UserHelper.createTwoUsersAndAddAsFriends()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `FriendshipRepository.existsByUserAndFriend()`

### 1.4.2 POST /users/friend/{login}/{friend} — негативный (пользователь уже в друзьях)
- [ ] Переиспользовать модель из 1.4.1
- [ ] Переиспользовать эндпоинт из 1.4.1
- [ ] Добавить тест в `AddFriendTest.java`
- [ ] Добавить JSON schema `addFriendAlreadyFriendsSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку в БД: существование записи в таблице друзей до и после запроса

### 1.4.3 POST /users/friend/{login}/{friend} — негативный (друг не существует)
- [ ] Переиспользовать модель из 1.4.1
- [ ] Переиспользовать эндпоинт из 1.4.1
- [ ] Добавить тест в `AddFriendTest.java`
- [ ] Добавить JSON schema `addFriendNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку в БД: отсутствие записи в таблице друзей после запроса

### 1.4.4 POST /users/friend/{login}/{friend} — негативный (попытка добавить себя)
- [ ] Переиспользовать модель из 1.4.1
- [ ] Переиспользовать эндпоинт из 1.4.1
- [ ] Добавить тест в `AddFriendTest.java`
- [ ] Добавить JSON schema `addFriendSelfSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку в БД: отсутствие записи в таблице друзей после запроса

### 1.4.5 GET /users/friend/{id} — позитивный (список друзей пользователя)
- [ ] Создать модель `FriendListResponse` в `models/` (список строк)
- [ ] Добавить эндпоинт `ENDPOINT_GET_FRIENDS` в `presentation/base/properties/patch/UsersPatch.java`
- [ ] Добавить тест в `AddFriendTest.java` (или создать `GetFriendsTest.java`)
- [ ] Добавить JSON schema `friendsListSchema.json` в `schemas/userCrudSchema/`
- [ ] Добавить проверку в БД: `FriendshipRepository.findFriendsByUserId()`

### 1.4.6 GET /users/friend/{id} — негативный (пользователь не найден)
- [ ] Переиспользовать модель из 1.4.5
- [ ] Переиспользовать эндпоинт из 1.4.5
- [ ] Добавить тест в `GetFriendsTest.java`
- [ ] Переиспользовать JSON schema `400notFoundSchema.json` из `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия пользователя в БД по ID

---

## 1.5. Фильтрация пользователей

### 1.5.1 GET /users/filter — позитивный (по цвету волос и гендеру)
- [ ] Создать модель `UserFilterRequest` в `models/` (hairColor, gender)
- [ ] Создать модель `FilterUserResponse` в `models/` (список CheckUserDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USERS_FILTER` в `presentation/base/properties/patch/UsersPatch.java`
- [ ] Создать тестовый класс `FilterUsersTest.java` в `test/users/`
- [ ] Добавить JSON schema `usersFilterSchema.json` в `schemas/userCrudSchema/`
- [ ] Создать хелпер `UserHelper.createUsersByFilter()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `UserRepository.findByHairColorAndGender()`

### 1.5.2 GET /users/filter — негативный (некорректные параметры)
- [ ] Переиспользовать модель из 1.5.1
- [ ] Переиспользовать эндпоинт из 1.5.1
- [ ] Добавить тест в `FilterUsersTest.java`
- [ ] Добавить JSON schema `400invalidFilterSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку пустого списка в БД по некорректным параметрам

---

# 2. Счета (Presentation)

## 2.1. Создание счетов

### 2.1.1 POST /accounts?userLogin={login} — позитивный (успешное создание)
- [ ] Создать модель `CreateAccountResponse` в `models/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_CREATE_ACCOUNT` в `presentation/base/properties/patch/AccountsPatch.java` (создать новый файл)
- [ ] Создать тестовый класс `CreateAccountTest.java` в `test/accounts/` (создать новую директорию)
- [ ] Добавить JSON schema `createAccountSuccessSchema.json` в `schemas/accountSchema/` (создать новую директорию)
- [ ] Создать хелпер `AccountHelper.createUserWithAccount()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `AccountRepository.findByUserLogin()`

### 2.1.2 POST /accounts?userLogin={login} — негативный (пользователь не существует)
- [ ] Переиспользовать модель из 2.1.1
- [ ] Переиспользовать эндпоинт из 2.1.1
- [ ] Добавить тест в `CreateAccountTest.java`
- [ ] Добавить JSON schema `400userNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД таблицы accounts

---

## 2.2. Просмотр счетов

### 2.2.1 GET /accounts — позитивный (список всех счетов)
- [ ] Создать модель `AccountListResponse` в `models/` (список CheckAccountDTO)
- [ ] Добавить эндпоинт `ENDPOINT_GET_ALL_ACCOUNTS` в `presentation/base/properties/patch/AccountsPatch.java`
- [ ] Добавить тест в `CreateAccountTest.java` (или создать `GetAccountsTest.java`)
- [ ] Добавить JSON schema `accountsListSchema.json` в `schemas/accountSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findAll()`

### 2.2.2 GET /accounts/{accountId} — позитивный (счёт найден)
- [ ] Создать модель `CheckAccountDTO` в `models/`
- [ ] Добавить эндпоинт `ENDPOINT_GET_ACCOUNT_BY_ID` в `presentation/base/properties/patch/AccountsPatch.java`
- [ ] Добавить тест в `GetAccountsTest.java`
- [ ] Добавить JSON schema `accountDetailsSchema.json` в `schemas/accountSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()`

### 2.2.3 GET /accounts/{accountId} — негативный (счёт не найден)
- [ ] Переиспользовать модель из 2.2.2
- [ ] Переиспользовать эндпоинт из 2.2.2
- [ ] Добавить тест в `GetAccountsTest.java`
- [ ] Переиспользовать JSON schema `400notFoundSchema.json` из `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

---

## 2.3. Пополнение счёта

### 2.3.1 POST /accounts/{id}/deposit — позитивный (успешное пополнение)
- [ ] Создать модель `DepositRequest` в `models/` (BigDecimal amount)
- [ ] Создать модель `DepositResponse` в `models/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_DEPOSIT` в `presentation/base/properties/patch/AccountsPatch.java`
- [ ] Создать тестовый класс `DepositTest.java` в `test/accounts/`
- [ ] Добавить JSON schema `depositSuccessSchema.json` в `schemas/accountSchema/`
- [ ] Создать хелпер `AccountHelper.createAccountWithBalance()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — увеличение баланса
- [ ] Добавить проверку события в Kafka: `AccountEvent` (deposit)

### 2.3.2 POST /accounts/{id}/deposit — негативный (счёт не найден)
- [ ] Переиспользовать модель из 2.3.1
- [ ] Переиспользовать эндпоинт из 2.3.1
- [ ] Добавить тест в `DepositTest.java`
- [ ] Добавить JSON schema `400accountNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.3.3 POST /accounts/{id}/deposit — негативный (отрицательная сумма)
- [ ] Переиспользовать модель из 2.3.1
- [ ] Переиспользовать эндпоинт из 2.3.1
- [ ] Добавить тест в `DepositTest.java`
- [ ] Добавить JSON schema `400negativeAmountSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

---

## 2.4. Снятие средств

### 2.4.1 POST /accounts/{id}/withdraw — позитивный (успешное снятие)
- [ ] Создать модель `WithdrawRequest` в `models/` (BigDecimal amount)
- [ ] Создать модель `WithdrawResponse` в `models/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_WITHDRAW` в `presentation/base/properties/patch/AccountsPatch.java`
- [ ] Создать тестовый класс `WithdrawTest.java` в `test/accounts/`
- [ ] Добавить JSON schema `withdrawSuccessSchema.json` в `schemas/accountSchema/`
- [ ] Создать хелпер `AccountHelper.createAccountWithSufficientBalance()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — уменьшение баланса
- [ ] Добавить проверку события в Kafka: `AccountEvent` (withdraw)

### 2.4.2 POST /accounts/{id}/withdraw — негативный (счёт не найден)
- [ ] Переиспользовать модель из 2.4.1
- [ ] Переиспользовать эндпоинт из 2.4.1
- [ ] Добавить тест в `WithdrawTest.java`
- [ ] Добавить JSON schema `400accountNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.4.3 POST /accounts/{id}/withdraw — негативный (отрицательная сумма)
- [ ] Переиспользовать модель из 2.4.1
- [ ] Переиспользовать эндпоинт из 2.4.1
- [ ] Добавить тест в `WithdrawTest.java`
- [ ] Добавить JSON schema `400negativeAmountSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.4.4 POST /accounts/{id}/withdraw — негативный (недостаточно средств)
- [ ] Переиспользовать модель из 2.4.1
- [ ] Переиспользовать эндпоинт из 2.4.1
- [ ] Добавить тест в `WithdrawTest.java`
- [ ] Добавить JSON schema `400insufficientFundsSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

---

## 2.5. Перевод между счетами

### 2.5.1 POST /accounts/transfer/{fromAccountId}/{toAccountId} — позитивный (успешный перевод)
- [ ] Создать модель `TransferRequest` в `models/` (BigDecimal amount)
- [ ] Создать модель `TransferResponse` в `models/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_TRANSFER` в `presentation/base/properties/patch/AccountsPatch.java`
- [ ] Создать тестовый класс `TransferTest.java` в `test/accounts/`
- [ ] Добавить JSON schema `transferSuccessSchema.json` в `schemas/accountSchema/`
- [ ] Создать хелпер `AccountHelper.createTwoAccountsWithBalance()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — изменение баланса обоих счетов
- [ ] Добавить проверку событий в Kafka: `AccountEvent` (transfer) для обоих счетов

### 2.5.2 POST /accounts/transfer/{fromAccountId}/{toAccountId} — негативный (отправляющий счёт не найден)
- [ ] Переиспользовать модель из 2.5.1
- [ ] Переиспользовать эндпоинт из 2.5.1
- [ ] Добавить тест в `TransferTest.java`
- [ ] Добавить JSON schema `400fromAccountNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.5.3 POST /accounts/transfer/{fromAccountId}/{toAccountId} — негативный (получающий счёт не найден)
- [ ] Переиспользовать модель из 2.5.1
- [ ] Переиспользовать эндпоинт из 2.5.1
- [ ] Добавить тест в `TransferTest.java`
- [ ] Добавить JSON schema `400toAccountNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.5.4 POST /accounts/transfer/{fromAccountId}/{toAccountId} — негативный (отрицательная сумма)
- [ ] Переиспользовать модель из 2.5.1
- [ ] Переиспользовать эндпоинт из 2.5.1
- [ ] Добавить тест в `TransferTest.java`
- [ ] Добавить JSON schema `400negativeAmountSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.5.5 POST /accounts/transfer/{fromAccountId}/{toAccountId} — негативный (недостаточно средств)
- [ ] Переиспользовать модель из 2.5.1
- [ ] Переиспользовать эндпоинт из 2.5.1
- [ ] Добавить тест в `TransferTest.java`
- [ ] Добавить JSON schema `400insufficientFundsSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 2.5.6 POST /accounts/transfer/{fromAccountId}/{toAccountId} — негативный (владелец счёта не найден)
- [ ] Переиспользовать модель из 2.5.1
- [ ] Переиспользовать эндпоинт из 2.5.1
- [ ] Добавить тест в `TransferTest.java`
- [ ] Добавить JSON schema `400ownerNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

---

# 3. Транзакции (Presentation)

## 3.1. Получение транзакций

### 3.1.1 GET /transactions/check — позитивный (список всех транзакций)
- [ ] Создать модель `AllTransactionsResponse` в `models/` (CheckTransactionsDTO)
- [ ] Добавить эндпоинт `ENDPOINT_GET_ALL_TRANSACTIONS` в `presentation/base/properties/patch/TransactionsPatch.java` (создать новый файл)
- [ ] Создать тестовый класс `GetAllTransactionsTest.java` в `test/transactions/` (создать новую директорию)
- [ ] Добавить JSON schema `allTransactionsSchema.json` в `schemas/transactionSchema/` (создать новую директорию)
- [ ] Создать хелпер `TransactionHelper.createSampleTransactions()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `TransactionRepository.findAll()`

### 3.1.2 GET /transactions/check — негативный (ошибка сервера)
- [ ] Переиспользовать модель из 3.1.1
- [ ] Переиспользовать эндпоинт из 3.1.1
- [ ] Добавить тест в `GetAllTransactionsTest.java`
- [ ] Добавить JSON schema `500errorSchema.json` в `schemas/errorSchema/`

### 3.1.3 GET /transactions/account/{id} — позитивный (транзакции по аккаунту и типу deposit)
- [ ] Создать модель `AccountTransactionsResponse` в `models/` (список TransactionDTO)
- [ ] Создать модель `TransactionTypeDTO` в `models/`
- [ ] Добавить эндпоинт `ENDPOINT_GET_TRANSACTIONS_BY_ACCOUNT` в `presentation/base/properties/patch/TransactionsPatch.java`
- [ ] Добавить тест в `GetTransactionsByAccountTest.java` (создать новый файл)
- [ ] Добавить JSON schema `accountTransactionsSchema.json` в `schemas/transactionSchema/`
- [ ] Добавить проверку в БД: `TransactionRepository.findByAccountIdAndType()`

### 3.1.4 GET /transactions/account/{id} — позитивный (транзакции по аккаунту и типу withdraw)
- [ ] Переиспользовать модель из 3.1.3
- [ ] Переиспользовать эндпоинт из 3.1.3
- [ ] Добавить тест в `GetTransactionsByAccountTest.java`
- [ ] Переиспользовать JSON schema из 3.1.3
- [ ] Добавить проверку в БД: `TransactionRepository.findByAccountIdAndType()`

### 3.1.5 GET /transactions/account/{id} — позитивный (транзакции по аккаунту и типу transfer)
- [ ] Переиспользовать модель из 3.1.3
- [ ] Переиспользовать эндпоинт из 3.1.3
- [ ] Добавить тест в `GetTransactionsByAccountTest.java`
- [ ] Переиспользовать JSON schema из 3.1.3
- [ ] Добавить проверку в БД: `TransactionRepository.findByAccountIdAndType()`

### 3.1.6 GET /transactions/account/{id} — негативный (неизвестный тип транзакции)
- [ ] Переиспользовать модель из 3.1.3
- [ ] Переиспользовать эндпоинт из 3.1.3
- [ ] Добавить тест в `GetTransactionsByAccountTest.java`
- [ ] Добавить JSON schema `400invalidTransactionTypeSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку пустого списка в БД

---

# 4. Административные операции (API-Gateway)

## 4.1. Управление пользователями (Admin)

### 4.1.1 POST /admin/users — создание клиентского аккаунта (успех)
- [ ] Создать модель `CreateClientRequest` в `models/admin/`
- [ ] Создать модель `CreateAdminResponse` в `models/admin/` (String)
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_CREATE_CLIENT` в `presentation/base/properties/patch/AdminPatch.java` (создать новый файл)
- [ ] Создать тестовый класс `AdminCreateClientTest.java` в `test/admin/` (создать новую директорию)
- [ ] Добавить JSON schema `createClientSuccessSchema.json` в `schemas/adminSchema/` (создать новую директорию)
- [ ] Создать хелпер `AdminHelper.createAdminToken()` для получения токена ADMIN
- [ ] Добавить проверку в БД: `UserRepository.findByLogin()` — роль CLIENT
- [ ] Добавить проверку события в Kafka: `ClientEvent`

### 4.1.2 POST /admin/admins — создание админского аккаунта (успех)
- [ ] Создать модель `CreateAdminRequest` в `models/admin/`
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_CREATE_ADMIN` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Создать тестовый класс `AdminCreateAdminTest.java` в `test/admin/`
- [ ] Добавить JSON schema `createAdminSuccessSchema.json` в `schemas/adminSchema/`
- [ ] Добавить проверку в БД: `UserRepository.findByLogin()` — роль ADMIN

### 4.1.3 GET /admin/users/filter — фильтрация пользователей (успех)
- [ ] Создать модель `UserFilterByHairColorAndGenderRequest` в `models/admin/`
- [ ] Создать модель `FilteredUsersResponse` в `models/admin/` (список UserDTO)
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_FILTER_USERS` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Добавить тест в `AdminFilterUsersTest.java` (создать новый файл)
- [ ] Добавить JSON schema `filteredUsersSchema.json` в `schemas/adminSchema/`
- [ ] Добавить проверку в БД: `UserRepository.findByHairColorAndGender()`

### 4.1.4 GET /admin/users/{userId} — получение пользователя по ID (успех)
- [ ] Создать модель `UserDTO` в `models/admin/`
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_GET_USER_BY_ID` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Добавить тест в `AdminGetUserTest.java` (создать новый файл)
- [ ] Добавить JSON schema `userDTO schema.json` в `schemas/adminSchema/`
- [ ] Добавить проверку в БД: `UserRepository.findById()`

### 4.1.5 GET /admin/users/{userId} — негативный (пользователь не найден)
- [ ] Переиспользовать модель из 4.1.4
- [ ] Переиспользовать эндпоинт из 4.1.4
- [ ] Добавить тест в `AdminGetUserTest.java`
- [ ] Добавить JSON schema `400userNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

---

## 4.2. Управление счетами (Admin)

### 4.2.1 GET /admin/accounts — список всех счетов (успех)
- [ ] Создать модель `AccountDTO` в `models/admin/`
- [ ] Создать модель `AllAccountsResponse` в `models/admin/` (список AccountDTO)
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_GET_ALL_ACCOUNTS` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Добавить тест в `AdminGetAccountsTest.java` (создать новый файл)
- [ ] Добавить JSON schema `allAccountsSchema.json` в `schemas/adminSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findAll()`

### 4.2.2 GET /admin/user/accounts/{userLogin} — счета пользователя по логину (успех)
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_GET_USER_ACCOUNTS` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Добавить тест в `AdminGetUserAccountsTest.java` (создать новый файл)
- [ ] Переиспользовать JSON schema из 4.2.1
- [ ] Добавить проверку в БД: `AccountRepository.findByUserLogin()`

### 4.2.3 GET /admin/user/accounts/{userLogin} — негативный (пользователь не найден)
- [ ] Переиспользовать модель из 4.2.2
- [ ] Переиспользовать эндпоинт из 4.2.2
- [ ] Добавить тест в `AdminGetUserAccountsTest.java`
- [ ] Добавить JSON schema `400userNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку пустого списка в БД

### 4.2.4 GET /admin/accounts/{accountId} — детали счёта (успех)
- [ ] Добавить эндпоинт `ENDPOINT_ADMIN_GET_ACCOUNT_DETAILS` в `presentation/base/properties/patch/AdminPatch.java`
- [ ] Добавить тест в `AdminGetAccountTest.java` (создать новый файл)
- [ ] Добавить JSON schema `accountDetailsSchema.json` в `schemas/adminSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()`

### 4.2.5 GET /admin/accounts/{accountId} — негативный (счёт не найден)
- [ ] Переиспользовать модель из 4.2.4
- [ ] Переиспользовать эндпоинт из 4.2.4
- [ ] Добавить тест в `AdminGetAccountTest.java`
- [ ] Добавить JSON schema `400accountNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

---

## 4.3. Проверка прав доступа

### 4.3.1 POST /admin/users — негативный (доступ без роли ADMIN)
- [ ] Создать модель `UnauthorizedRequest` в `models/admin/`
- [ ] Добавить тест в `AdminAuthorizationTest.java` (создать новый файл)
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия создания пользователя в БД

### 4.3.2 GET /admin/accounts — негативный (доступ без роли ADMIN)
- [ ] Переиспользовать модель из 4.3.1
- [ ] Добавить тест в `AdminAuthorizationTest.java`
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения данных в БД

---

# 5. Операции пользователя через Gateway (API-Gateway)

## 5.1. Личный кабинет

### 5.1.1 GET /user/me — позитивный (получение своей информации)
- [ ] Создать модель `UserInfoResponse` в `models/user/`
- [ ] Добавить эндпоинт `ENDPOINT_USER_ME` в `presentation/base/properties/patch/UserPatch.java` (создать новый файл)
- [ ] Создать тестовый класс `GetUserInfoTest.java` в `test/user/` (создать новую директорию)
- [ ] Добавить JSON schema `userInfoSchema.json` в `schemas/userSchema/` (создать новую директорию)
- [ ] Создать хелпер `UserHelper.createClientToken()` для получения токена CLIENT
- [ ] Добавить проверку в БД: `UserRepository.findByLogin()`

### 5.1.2 GET /user/me — негативный (без токена авторизации)
- [ ] Переиспользовать модель из 5.1.1
- [ ] Переиспользовать эндпоинт из 5.1.1
- [ ] Добавить тест в `GetUserInfoTest.java`
- [ ] Добавить JSON schema `401unauthorizedSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения данных в БД

### 5.1.3 GET /user/me/accounts — позитивный (список своих счетов)
- [ ] Создать модель `UserAccountsResponse` в `models/user/` (список AccountDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USER_ME_ACCOUNTS` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `GetUserAccountsTest.java` (создать новый файл)
- [ ] Добавить JSON schema `userAccountsSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findByUserLogin()`

### 5.1.4 GET /user/me/accounts — негативный (без токена авторизации)
- [ ] Переиспользовать модель из 5.1.3
- [ ] Переиспользовать эндпоинт из 5.1.3
- [ ] Добавить тест в `GetUserAccountsTest.java`
- [ ] Добавить JSON schema `401unauthorizedSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения данных в БД

---

    ## 5.2. Управление друзьями (через Gateway)

### 5.2.1 POST /user/friends/{friendLogin} — позитивный (добавление друга)
- [ ] Создать модель `AddFriendGatewayResponse` в `models/user/`
- [ ] Добавить эндпоинт `ENDPOINT_USER_ADD_FRIEND` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `AddFriendGatewayTest.java` (создать новый файл)
- [ ] Добавить JSON schema `addFriendGatewaySuccessSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `FriendshipRepository.existsByUserAndFriend()`
- [ ] Добавить проверку события в Kafka: `ClientEvent` (addFriend)

### 5.2.2 POST /user/friends/{friendLogin} — негативный (друг не найден)
- [ ] Переиспользовать модель из 5.2.1
- [ ] Переиспользовать эндпоинт из 5.2.1
- [ ] Добавить тест в `AddFriendGatewayTest.java`
- [ ] Добавить JSON schema `400friendNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД таблицы друзей

### 5.2.3 GET /user/friends/get — позитивный (список друзей)
- [ ] Создать модель `UserFriendsResponse` в `models/user/` (список String)
- [ ] Добавить эндпоинт `ENDPOINT_USER_GET_FRIENDS` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `GetUserFriendsTest.java` (создать новый файл)
- [ ] Добавить JSON schema `userFriendsSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `FriendshipRepository.findFriendsByUserId()`

### 5.2.4 GET /user/friends/get — негативный (без токена авторизации)
- [ ] Переиспользовать модель из 5.2.3
- [ ] Переиспользовать эндпоинт из 5.2.3
- [ ] Добавить тест в `GetUserFriendsTest.java`
- [ ] Добавить JSON schema `401unauthorizedSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения данных в БД

---

## 5.3. Операции со счетами (через Gateway)

### 5.3.1 GET /user/accounts/{accountId} — позитивный (получение своего счёта)
- [ ] Создать модель `MyAccountResponse` в `models/user/` (CheckAccountDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USER_GET_MY_ACCOUNT` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `GetMyAccountTest.java` (создать новый файл)
- [ ] Добавить JSON schema `myAccountSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()`

### 5.3.2 GET /user/accounts/{accountId} — негативный (счёт не принадлежит пользователю)
- [ ] Переиспользовать модель из 5.3.1
- [ ] Переиспользовать эндпоинт из 5.3.1
- [ ] Добавить тест в `GetMyAccountTest.java`
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия доступа к данным в БД

### 5.3.3 POST /user/{id}/deposit — позитивный (пополнение своего счёта)
- [ ] Создать модель `DepositGatewayRequest` в `models/user/` (BigDecimal amount)
- [ ] Создать модель `DepositGatewayResponse` в `models/user/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USER_DEPOSIT` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `DepositGatewayTest.java` (создать новый файл)
- [ ] Добавить JSON schema `depositGatewaySuccessSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — увеличение баланса
- [ ] Добавить проверку события в Kafka: `AccountEvent` (deposit)

### 5.3.4 POST /user/{id}/deposit — негативный (счёт не принадлежит пользователю)
- [ ] Переиспользовать модель из 5.3.3
- [ ] Переиспользовать эндпоинт из 5.3.3
- [ ] Добавить тест в `DepositGatewayTest.java`
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 5.3.5 POST /user/{id}/withdraw — позитивный (снятие со своего счёта)
- [ ] Создать модель `WithdrawGatewayRequest` в `models/user/` (BigDecimal amount)
- [ ] Создать модель `WithdrawGatewayResponse` в `models/user/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USER_WITHDRAW` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `WithdrawGatewayTest.java` (создать новый файл)
- [ ] Добавить JSON schema `withdrawGatewaySuccessSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — уменьшение баланса
- [ ] Добавить проверку события в Kafka: `AccountEvent` (withdraw)

### 5.3.6 POST /user/{id}/withdraw — негативный (счёт не принадлежит пользователю)
- [ ] Переиспользовать модель из 5.3.5
- [ ] Переиспользовать эндпоинт из 5.3.5
- [ ] Добавить тест в `WithdrawGatewayTest.java`
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

### 5.3.7 POST /user/accounts/{fromAccountId}/{toAccountId}/transfer — позитивный (перевод между своими счетами)
- [ ] Создать модель `TransferGatewayRequest` в `models/user/` (BigDecimal amount)
- [ ] Создать модель `TransferGatewayResponse` в `models/user/` (OperationResponseDTO)
- [ ] Добавить эндпоинт `ENDPOINT_USER_TRANSFER` в `presentation/base/properties/patch/UserPatch.java`
- [ ] Добавить тест в `TransferGatewayTest.java` (создать новый файл)
- [ ] Добавить JSON schema `transferGatewaySuccessSchema.json` в `schemas/userSchema/`
- [ ] Добавить проверку в БД: `AccountRepository.findById()` — изменение баланса обоих счетов
- [ ] Добавить проверку событий в Kafka: `AccountEvent` (transfer) для обоих счетов

### 5.3.8 POST /user/accounts/{fromAccountId}/{toAccountId}/transfer — негативный (отправляющий счёт не принадлежит пользователю)
- [ ] Переиспользовать модель из 5.3.7
- [ ] Переиспользовать эндпоинт из 5.3.7
- [ ] Добавить тест в `TransferGatewayTest.java`
- [ ] Добавить JSON schema `403forbiddenSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия изменения баланса в БД

---

# 6. События (Storage)

## 6.1. События счетов

### 6.1.1 GET /api/events/account/{id} — позитивный (событие счета по ID)
- [ ] Создать модель `AccountEventResponse` в `models/events/`
- [ ] Добавить эндпоинт `ENDPOINT_GET_ACCOUNT_EVENT` в `presentation/base/properties/patch/EventsPatch.java` (создать новый файл)
- [ ] Создать тестовый класс `GetAccountEventsTest.java` в `test/events/` (создать новую директорию)
- [ ] Добавить JSON schema `accountEventSchema.json` в `schemas/eventSchema/` (создать новую директорию)
- [ ] Создать хелпер `EventHelper.createAccountEvent()` для подготовки тестовых данных
- [ ] Добавить проверку в БД: `AccountEventRepository.findById()`

### 6.1.2 GET /api/events/account/{id} — негативный (событие не найдено)
- [ ] Переиспользовать модель из 6.1.1
- [ ] Переиспользовать эндпоинт из 6.1.1
- [ ] Добавить тест в `GetAccountEventsTest.java`
- [ ] Добавить JSON schema `404eventNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

### 6.1.3 GET /api/events/account/key/{accountId} — позитивный (история событий счета)
- [ ] Создать модель `AccountEventsListResponse` в `models/events/` (список AccountEvent)
- [ ] Добавить эндпоинт `ENDPOINT_GET_ACCOUNT_EVENTS_BY_ID` в `presentation/base/properties/patch/EventsPatch.java`
- [ ] Добавить тест в `GetAccountEventsTest.java`
- [ ] Добавить JSON schema `accountEventsListSchema.json` в `schemas/eventSchema/`
- [ ] Добавить проверку в БД: `AccountEventRepository.findByAccountId()`

---

## 6.2. События клиентов

### 6.2.1 GET /api/events/client/{id} — позитивный (событие клиента по ID)
- [ ] Создать модель `ClientEventResponse` в `models/events/`
- [ ] Добавить эндпоинт `ENDPOINT_GET_CLIENT_EVENT` в `presentation/base/properties/patch/EventsPatch.java`
- [ ] Добавить тест в `GetClientEventsTest.java` (создать новый файл)
- [ ] Добавить JSON schema `clientEventSchema.json` в `schemas/eventSchema/`
- [ ] Добавить проверку в БД: `ClientEventRepository.findById()`

### 6.2.2 GET /api/events/client/{id} — негативный (событие не найдено)
- [ ] Переиспользовать модель из 6.2.1
- [ ] Переиспользовать эндпоинт из 6.2.1
- [ ] Добавить тест в `GetClientEventsTest.java`
- [ ] Добавить JSON schema `404eventNotFoundSchema.json` в `schemas/errorSchema/`
- [ ] Добавить проверку отсутствия записи в БД по ID

### 6.2.3 GET /api/events/client/key/{login} — позитивный (история событий клиента по логину)
- [ ] Создать модель `ClientEventsListResponse` в `models/events/` (список ClientEvent)
- [ ] Добавить эндпоинт `ENDPOINT_GET_CLIENT_EVENTS_BY_LOGIN` в `presentation/base/properties/patch/EventsPatch.java`
- [ ] Добавить тест в `GetClientEventsTest.java`
- [ ] Добавить JSON schema `clientEventsListSchema.json` в `schemas/eventSchema/`
- [ ] Добавить проверку в БД: `ClientEventRepository.findByLogin()`