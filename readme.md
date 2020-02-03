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


## Конвертер

Требует для работы: Libre Office или Open Office?

Брать: ""

Форматы:
PDF = ("pdf");
SWF = ("swf");
HTML = ("html");
ODT = ("odt");
OTT = ("ott");
FODT = ("fodt");
SXW = ("sxw");
DOC = ("doc");
DOCX = ("docx");
RTF = ("rtf");
WPD = ("wpd");
TXT = ("txt");
ODS = ("ods");
OTS = ("ots");
FODS = ("fods");
SXC = ("sxc");
XLS = ("xls");
XLSX = ("xlsx");
CSV = ("csv");
TSV = ("tsv");
ODP = ("odp");
OTP = ("otp");
FODP = ("fodp");
SXI = ("sxi");
PPT = ("ppt");
PPTX = ("pptx");
ODG = ("odg");
OTG = ("otg");
FODG = ("fodg");
SVG = ("svg");
PNG = ("png");
JPEG = ("jpg");
TIFF = ("tif");
GIF = ("gif");
BMP = ("bmp");
