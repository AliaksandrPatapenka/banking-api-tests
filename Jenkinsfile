pipeline {
    agent any

    // ====================================================
    // 1. ПАРАМЕТРЫ СБОРКИ
    // ====================================================
    parameters {
        string(name: 'REPO_URL', defaultValue: 'https://github.com/AliaksandrPatapenka/banking-api-tests', description: 'URL репозитория с кодом. По умолчанию https://github.com/AliaksandrPatapenka/banking-api-tests')
        choice(name: 'ENVIRONMENT', choices: ['teststand'], description: 'Стенд')
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Название ветки. По умолчанию "master"')
        choice(name: 'TESTS', choices: [
               'all/all',
               'presentation/all',
               'presentation/accounts',
               'presentation/users',
               'presentation/kafka'], description: 'Сервис и пакет тестов. По умолчанию "all"')
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