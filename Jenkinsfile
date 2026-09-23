pipeline {
    agent any

    // ====================================================
    // 1. ПАРАМЕТРЫ СБОРКИ
    // ====================================================
    parameters {
        string(name: 'REPO_URL', defaultValue: 'https://github.com/AliaksandrPatapenka/banking-api-tests', description: 'URL репозитория с кодом. По умолчанию https://github.com/AliaksandrPatapenka/banking-api-tests')
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Название ветки. По умолчанию "master"')
        choice(name: 'TEST_SUITE', choices: ['all', 'accounts', 'users', 'kafka'], description: 'Пакет тестов. По умолчанию "all"')
        string(name: 'BASE_URL', defaultValue: 'http://presentation:8081', description: 'Базовый URL API. По умолчанию http://presentation:8081')
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
                                -d "text=🚀 Тесты <b>ЗАПУЩЕНЫ!</b>\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${env.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
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
                                def testPattern = params.TEST_SUITE == 'all' ? '' : params.TEST_SUITE + '/*'

                                sh '''
                                    mvn clean test -e \
                                    -Ddb.url=jdbc:postgresql://postgres:5432/postgres \
                                    -Dkafka.bootstrap.servers=kafka:9092 \
                                    -Dbase.url=''' + params.BASE_URL + ''' \
                                    -Dtest=''' + testPattern
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
                                -d "text=❌ Тесты <b>НЕ ЗАПУСТИЛИСЬ</b>!\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${env.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
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
                            -d "text=${statusIcon} ${statusText}\n       -\n       Проект: <code>${repoName}</code>\n       Ветка: <code>${env.BRANCH_NAME}</code>\n       -\n       Тесты: <code>[${JOB_NAME}]</code>\n       Номер запуска: <code>${BUILD_NUMBER}</code>\n       Запустил: <code>${env.BUILD_USER}</code>\n\n<code>${buildUrl}</code>" \
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
            publishHTML([
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'target/site/allure-maven-plugin',
                    reportFiles          : 'index.html',
                    reportName           : 'Allure Report'
            ])
        }
    }
}