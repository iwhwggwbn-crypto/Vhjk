# BitPack 24/24/16 (Fabric 1.20.1)

Сборка (Java 17):
    gradle wrapper --gradle-version 8.5
    ./gradlew build
Готовый мод: build/libs/bitpack-1.0.0.jar (положить в mods/ на клиенте И на сервере, нужен Fabric API).

Проверка: в latest.log должна быть строка "BitPack: упаковка BlockPos 24/24/16 работает".

## Сборка через GitHub
Залить содержимое папки проекта в репозиторий (файлы должны лежать в корне, рядом с build.gradle),
вкладка Actions → build → последний запуск → Artifacts → bitpack-jar.
