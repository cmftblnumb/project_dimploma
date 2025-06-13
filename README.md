# Процедура запуска автотестов


1. Открыть проект в IntelliJ IDEA

2. В терминале запустить контейнеры с помощью команды:

>docker-compose up -d

3. Запустить целевое приложение с помощью команды в терминале:

>для mySQL: 
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar 

 >для postgresgl:
 java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
 
4. Во втором терминале запустить тесты командой:

>для mySQL:
./gradlew clean test -DurlDB="jdbc:mysql://localhost:3306/app"

>для postgresgl: 
./gradlew clean test -DurlDB="jdbc:postgresql://localhost:5432/app"

5. Создать отчёт Allure и открыть в браузере с помощью команды в терминале:

>./gradlew allureServe

