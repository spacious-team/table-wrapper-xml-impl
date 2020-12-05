![java-version](https://img.shields.io/badge/Java-11-brightgreen?style=flat-square)
![jitpack-last-release](https://jitpack.io/v/spacious-team/table-wrapper-xml-impl.svg?style=flat-square)

### Назначение
Предоставляет реализацию `Table Wrapper API` для удобного доступа к табличным данным, сохраненным в файлах формата
Microsoft Office Excel 2003 [SpreadsheetML](https://en.wikipedia.org/wiki/SpreadsheetML) (xml).

Пример создания фабрики таблиц
```java
ExcelReader reader = new ExcelReader();
Workbook book = reader.getWorkbook(Files.newInputStream(Path.of("1.xml")));
ReportPage reportPage = new XmlReportPage(book.getWorksheetAt(0));
TableFactory tableFactory = new XmlTableFactory();

Table table1 = tableFactory.create(reportPage, "Table 1 description", ...);
...
Table tableN = tableFactory.create(reportPage, "Table N description", ...);
```
Объекты `table`...`tableN` используются для удобного доступа к строкам и к значениям ячеек.

Больше подробностей в документации [Table Wrapper API](https://github.com/spacious-team/table-wrapper-api).

### Как использовать в своем проекте
Необходимо подключить репозиторий open source библиотек github [jitpack](https://jitpack.io/#spacious-team/table-wrapper-xml-impl),
например для Apache Maven проекта
```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
и добавить зависимость
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-xml-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
В качестве версии можно использовать:
- версию [релиза](https://github.com/spacious-team/table-wrapper-xml-impl/releases) на github;
- паттерн `<branch>-SNAPSHOT` для сборки зависимости с последнего коммита выбранной ветки;
- короткий 10-ти значный номер коммита для сборки зависимости с указанного коммита.
