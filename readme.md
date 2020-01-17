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
http://localhost:9191/pdftool/convertpdf

### Установка

1. Сборка проекта:

```
mvn install
```

2. Копирование файла **target/pdftool.war** в папку **webapps** контейнера или сервера (например, tomcat).


