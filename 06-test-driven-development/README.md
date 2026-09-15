# 06 Test-Driven Development (PHPUnit) — Antworten

## Aufgabe 1: Setup environment

PHP 8.5 (Homebrew) und PHPUnit 11 (phpunit.phar) lokal installiert. `Testcases/Test.php`
auf die moderne PHPUnit-Syntax umgestellt (`use PHPUnit\Framework\TestCase;`, `extends
TestCase` statt der veralteten `PHPUnit_Framework_TestCase`).

Ausführung:

```
cd FancyApp/Testcases
php ../phpunit.phar Test.php
```

Vor der Implementierung (Red-Phase) schlugen alle 3 Tests fehl, wie erwartet:

```
FFF                                                                 3 / 3 (100%)
There were 3 failures:
1) Test::testShortString — Failed asserting that null matches expected '...'
2) Test::testCalcAverage — Failed asserting that null matches expected 5.
3) Test::testGetOpposite — Failed asserting that null matches expected '4.5,5,5.5'.
```

## Aufgabe 2: Methoden implementieren

Alle drei Methoden in `FancyApp/Application/MyFancyClass.php` implementiert, ausschliesslich
anhand der Assertions in `Test.php` hergeleitet (Tests als Spezifikation, echtes TDD
Red→Green).

**`calcAverage($values)`**: leeres Array -> `0`; Werte (auch numerische Strings) werden
summiert und durch die Anzahl geteilt; enthält das Array einen nicht-numerischen Wert ->
`false`.

**`shortString($text, $length, $ending = '...')`**: Ist `$ending` länger als `$length`, wird
`false` zurückgegeben (unabhängig von `$text`). Ist `$text` kürzer/gleich `$length`, wird er
unverändert zurückgegeben. Sonst wird `$text` auf `$length - strlen($ending)` Zeichen gekürzt
und `$ending` angehängt. Wichtiger Bugfix während der Implementierung: `strlen()`/`substr()`
zählen Bytes statt Zeichen — der Umlaut in `$text35Char` ("Länge") führte mit `strlen()` zu
36 statt 35 und löste fälschlicherweise eine Kürzung aus. Behoben durch `mb_strlen()`/
`mb_substr()`.

**`getOpposite($value, $separator = ',')`**: Ist `$value` ein Array, wird es mit `implode()`
zu einem String zusammengefügt; ist es ein String, wird er mit `explode()` in ein Array
aufgeteilt; alles andere (z.B. ein Objekt) ergibt `false`. Die beiden Zweige sind zueinander
invers (Roundtrip-Assertions in `Test.php` bestätigen dies).

Nach der Implementierung (Green-Phase), alle Tests bestanden:

```
PHPUnit 11.5.56 by Sebastian Bergmann and contributors.
Runtime:       PHP 8.5.10
...                                                                 3 / 3 (100%)
Time: 00:00.001, Memory: 25.47 MB
OK (3 tests, 27 assertions)
```

## Aufgabe 3: Auswerten (Reflexion)

- `strlen()`/`substr()` zählen Bytes, nicht Zeichen. Der Umlaut in `$text35Char` ("Länge")
  ist in UTF-8 2 Bytes lang, wodurch `strlen()` 36 statt 35 meldete und fälschlicherweise
  eine Kürzung auslöste. Behoben durch Wechsel auf `mb_strlen()`/`mb_substr()`.
- Kniffliger Grenzfall in `shortString()`: `$ending`-Länge *gleich* `$length` ist ok (Text
  wird unverändert zurückgegeben); erst *grösser als* löst `false` aus. Diese Prüfung musste
  zuerst und unbedingt erfolgen, vor dem Längenvergleich mit `$text`.
  `getOpposite()` verzweigt nach Eingabetyp (Array -> String via `implode`, String -> Array
  via `explode`, sonst `false`). Die Roundtrip-Assertion bestätigte, dass die beiden Zweige
  tatsächlich zueinander invers sind.
- Gruppenvergleich: nicht anwendbar, da solo gearbeitet.
