# Парсинг Excell files to xml.

    ### Старт сервера:
    ```
mvn cargo:run
```
Перезаливка приложения без рестарта:
```
mvn package
mvn cargo:deploy
```

Проверять из maven:

    http://localhost:9191/pdftool/compresspdf

        ### Установка

1. Сборка проекта:
    ```
mvn install
```

2. Копирование файла **target/web-xls.war** в папку **webapps** контейнера или сервера (например, tomcat).


