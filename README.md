Представлен утилитный класс PomParser, который имеет статический метод findPlugins(String path).
На вход он принимает ненулевой путь pom.xml файла.

Метод возвращает List<String> плагинов ("groupId:artifactId") не только самого pom-файла, но и его подмодулей, если такие имеются.
