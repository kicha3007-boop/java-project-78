# Валидатор данных (Java)

[![hexlet-check](https://github.com/kicha3007-boop/java-project-78/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/kicha3007-boop/java-project-78/actions)
[![Java CI](https://github.com/kicha3007-boop/java-project-78/actions/workflows/main.yml/badge.svg)](https://github.com/kicha3007-boop/java-project-78/actions/workflows/main.yml)

Создание собственной библиотеки для проверки корректности (валидации) данных – отличный способ прокачать навыки проектирования кода, в особенности, объектно-ориентированной архитектуры. Создание правильных иерархий классов, расширяемая архитектура, применение принципов SOLID, использование fluent-интерфейса – все это предстоит делать в проекте

Учебный проект Хекслета: https://ru.hexlet.io/programs/java
Как это должно работать: https://asciinema.org/a/NtQ6xBownxYFN2H8WEffvtcS1

## Стек

- Java 21, Gradle 8.14 (Kotlin DSL, зависимости в `app/gradle/libs.versions.toml`)
- JUnit 5, JaCoCo (порог покрытия 90% в `check`), Spotless (google-java-format)

## Установка

Нужны JDK 21 и make.

```bash
git clone https://github.com/kicha3007-boop/java-project-78.git
cd java-project-78
make build   # тесты + линтер + порог покрытия
make test
make lint
```

## Использование

Библиотека: фабрика `Validator` выдаёт схему, схема настраивается цепочкой правил,
`isValid()` возвращает `true` или `false`.

```java
import hexlet.code.Validator;
import hexlet.code.schemas.BaseSchema;
import java.util.HashMap;
import java.util.Map;

var v = new Validator();

// Строки: required(), minLength(n), contains(s)
var name = v.string().required().minLength(2);
name.isValid("Anna"); // true
name.isValid("A");    // false
name.isValid("");     // false

// Числа: required(), positive(), range(min, max) — границы включены
var age = v.number().positive().range(18, 99);
age.isValid(null); // true — без required() пустое значение валидно
age.isValid(17);   // false

// Map: required(), sizeof(n), shape(схемы значений по ключам)
Map<String, BaseSchema<String>> schemas = new HashMap<>();
schemas.put("firstName", v.string().required());
schemas.put("lastName", v.string().required().minLength(2));
var human = v.map().sizeof(2).shape(schemas);

human.isValid(Map.of("firstName", "John", "lastName", "Smith")); // true
human.isValid(Map.of("firstName", "Anna", "lastName", "B"));     // false
```

Правила для всех схем:
- пока не вызван `required()`, отсутствующее значение (`null`, для строк ещё и `""`) валидно,
  и остальные правила к нему не применяются;
- правила разных видов действуют вместе, повторный вызов правила того же вида заменяет прежнее.

Устройство: `BaseSchema<T>` хранит правила по имени (`Predicate<T>`) и запускает проверку;
`StringSchema`, `NumberSchema`, `MapSchema` добавляют только свои правила. Вложенная проверка в
`shape()` — это те же схемы, применённые к значениям ключей.

---

<details>
<summary>Автоматические тесты Хекслета</summary>

Тесты запускаются на каждый коммит. За запуск отвечает файл `.github/workflows/hexlet-check.yml` — не удаляйте и не переименовывайте ни его, ни репозиторий.

</details>

## О Хекслете

[Хекслет](https://ru.hexlet.io/) — школа программирования: авторские программы обучения с практикой, поддержкой наставников и реальными проектами, которые остаются в резюме. Этот репозиторий — один из таких проектов.
