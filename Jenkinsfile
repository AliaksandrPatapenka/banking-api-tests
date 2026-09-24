/**
 * ==================================================================================
 * ПАЙПЛАЙН ЗАПУСКА API-АВТОТЕСТОВ (Maven + RestAssured + JUnit 5) В JENKINS
 * ==================================================================================
 *
 * 1. Блок parameters — параметры, которые Jenkins показывает в форме запуска сборки:
 *    - REPO_URL    — URL репозитория с тестами;
 *    - ENVIRONMENT — стенд (окружение), на котором запускаются тесты;
 *    - BRANCH_NAME — ветка репозитория;
 *    - TESTS       — что запускать, формат "<сервис>/<пакет>":
 *      "all/all"          — все тесты всех сервисов;
 *      "<сервис>/all"     — все тесты одного сервиса;
 *      "<сервис>/<пакет>" — один пакет тестов сервиса (accounts, users, kafka).
 *
 * 2. Блок tools — инструменты сборки, настроенные в Jenkins: Maven "maven3", JDK "jdk21".
 *
 * 3. Блок stages (stage "Run") — основная логика. Сначала вычисляются переменные:
 *    buildUrl (ссылка на сборку), repoName (имя репозитория для сообщений в Telegram),
 *    testsFailed (флаг "тесты упали"). Дальше всё выполняется внутри withCredentials,
 *    которая подключает учётные данные из Jenkins:
 *    - "db-password-<ENVIRONMENT>" — пароль БД текущего стенда;
 *    - "telegram.token"            — токен Telegram-бота для уведомлений;
 *    - "user-credentials"          — логин/пароль для тестируемого API.
 *    3.1. Уведомление в Telegram о старте сборки.
 *    3.2. Клонирование выбранной ветки выбранного репозитория (шаг git).
 *    3.3. Запуск тестов:
 *         - параметр TESTS разбирается на "сервис" и "пакет";
 *         - если сервис = "all" — список сервисов берётся из папки
 *           src/test/resources/config/<ENVIRONMENT>/ (один .properties-файл = один
 *           сервис) и mvn test запускается отдельно для каждого сервиса;
 *         - иначе mvn test запускается один раз для выбранного сервиса;
 *         - свойства -Dservice и -Dprofile указывают тестам, какой конфигурационный
 *           файл загрузить: src/test/resources/config/<profile>/<service>.properties;
 *    3.4. Генерация Allure-отчёта (mvn allure:report).
 * ==================================================================================
 */

pipeline {
    agent any

    // ====================================================
    // 1. ПАРАМЕТРЫ СБОРКИ
    // ====================================================
    parameters {
        string(name: 'REPO_URL', defaultValue: 'https://github.com/AliaksandrPatapenka/banking-api-tests', description: 'URL репозитория с кодом. По умолчанию /banking-api-tests')
        choice(name: 'ENVIRONMENT', choices: ['teststand'], description: 'Стенд')
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Название ветки. По умолчанию "master"')
        choice(name: 'TESTS', choices: [
                'all/all',
                'presentation/all',
                'presentation/accounts',
                'presentation/users',
                'presentation/kafka'
        ], description: 'Сервис и пакет тестов. По умолчанию "all"')
    }

    // ====================================================
    // 2. ИНСТРУМЕНТЫ (Maven и Java)
    // ====================================================
    tools {
        maven 'maven3'
        jdk 'jdk21'
    }

    // ====================================================
    // 3. ОСНОВНАЯ ЛОГИКА СБОРКИ
    // ====================================================
    stages {
        stage('Run') {
            steps {
                script {
                    def buildUrl = "http://localhost:8085/job/${JOB_NAME}/${BUILD_NUMBER}/"
                    def repoName = params.REPO_URL.tokenize('/')[-1].replace('.git', '')
                    boolean testsFailed = false

                    try {
                        withCredentials([
                                string(credentialsId: "db-password-${params.ENVIRONMENT}", variable: 'DB_PASSWORD'),
                                string(credentialsId: 'telegram.token', variable: 'TOKEN'),
                                usernamePassword(credentialsId: 'user-credentials',
                                        usernameVariable: 'USERNAME',
                                        passwordVariable: 'PASSWORD')
                        ]) {
                            // --------------------------------------------
                            // 3.1. Уведомление о СТАРТЕ сборки
                            // --------------------------------------------

                            // TODO: перенести отправку сообщений в тг в отдельную библиотеку , когда проект разрастётся
                            sh """
                                curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
                                -d "chat_id=-1004366972797" \
                                -d "text=🚀 Тесты <b>ЗАПУЩЕНЫ!</b>\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${params.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
                                -d "parse_mode=HTML"
                            """

                            // --------------------------------------------
                            // 3.2. Клонирование ВЫБРАННОЙ ВЕТКИ
                            // --------------------------------------------
                            git branch: "${params.BRANCH_NAME}",
                                    url: "${params.REPO_URL}"

                            // --------------------------------------------
                            // 3.3. ЗАПУСК ТЕСТОВ с параметрами
                            // --------------------------------------------
                            try {
                                def parts = params.TESTS.split('/')
                                def service = parts[0]
                                def suite = parts[1]
                                def testArg = suite == 'all' ? '' : "-Dtest=${suite}/*"

                                sh 'mvn clean'

                                if (service == 'all') {
                                    def services = sh(
                                        script: 'ls src/test/resources/config/' + params.ENVIRONMENT + '/',
                                        returnStdout: true
                                    ).trim().split('\n').collect { it.replace('.properties', '') }

                                    services.each { svc ->
                                        def exitCode = sh(
                                            script: """
                                                mvn test -e \
                                                -Dservice=${svc} \
                                                -Dprofile=${params.ENVIRONMENT} \
                                                -Ddb.password=\$DB_PASSWORD \
                                                ${testArg}
                                            """,
                                            returnStatus: true
                                        )
                                        if (exitCode != 0) {
                                            testsFailed = true
                                            currentBuild.result = 'UNSTABLE'
                                            echo "Сервис ${svc} упал с кодом ${exitCode}"
                                        }
                                    }
                                } else {
                                    def exitCode = sh(
                                        script: """
                                            mvn test -e \
                                            -Dservice=${service} \
                                            -Dprofile=${params.ENVIRONMENT} \
                                            -Ddb.password=\$DB_PASSWORD \
                                            ${testArg}
                                        """,
                                        returnStatus: true
                                    )
                                    if (exitCode != 0) {
                                        testsFailed = true
                                        currentBuild.result = 'UNSTABLE'
                                    }
                                }
                            } catch (Exception e) {
                                testsFailed = true
                                currentBuild.result = 'UNSTABLE'
                                echo "Error in test execution: ${e.message}"
                            }

                            // --------------------------------------------
                            // 3.4. Генерация ALLURE-ОТЧЁТА
                            // --------------------------------------------
                            sh 'mvn allure:report'
                        }

                    } catch (Exception e) {
                        // --------------------------------------------
                        // 3.5. Если сборка УПАЛА (ошибка в пайплайне)
                        // --------------------------------------------
                        currentBuild.result = 'FAILURE'
                        withCredentials([string(credentialsId: 'telegram.token', variable: 'TOKEN')]) {
                            sh """
                                curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
                                -d "chat_id=-1004366972797" \
                                -d "text=❌ Тесты <b>НЕ ЗАПУСТИЛИСЬ</b>!\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${params.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
                                -d "parse_mode=HTML"
                            """
                        }
                        throw e
                    }

                    // --------------------------------------------
                    // 3.6. ИТОГОВОЕ сообщение (УСПЕШНА / НЕСТАБИЛЬНА)
                    // --------------------------------------------
                    withCredentials([string(credentialsId: 'telegram.token', variable: 'TOKEN')]) {
                        def statusIcon = testsFailed ? "⚠️" : "✅"
                        def statusText = testsFailed ? "Тесты <b>УПАЛИ!</b>" : "Тесты отработали <b>УСПЕШНО!</b>"
                        sh """
                            curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
                            -d "chat_id=-1004366972797" \
                            -d "text=${statusIcon} ${statusText}\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${params.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
                            -d "parse_mode=HTML"
                        """
                    }
                }
            }
        }
    }

    // ====================================================
    // 4. ДЕЙСТВИЯ ПОСЛЕ СБОРКИ (всегда)
    // ====================================================
    post {
        always {
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}