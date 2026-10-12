# 12 Auswertungen / Reflexion — Antworten

## Aufgabe 1: Reflexion zum Modul

### Was habe ich Neues gelernt?

* **Testlevels und Teststrategie (01–04):** welche Tests auf welche Ebene gehören.
* **Unit Tests und TDD (05–06):** Die Tests legen die Anforderungen fest, der Code folgt.
* **Mockito (07):** einen Service ohne echte Datenbank testen.
* **Testautomation (08):** API-Tests, Playwright für den Browser, k6 für Last.
* **Code Reviews (09):** Eine zweite Person findet, was man selbst übersieht.
* **CI (10):** MockMvc, SoftAssertions, JaCoCo und eine GitHub-Actions-Pipeline mit Testreport.
* **Docker Compose (11):** eine reproduzierbare Testing-Umgebung.

Konkrete Momente, in denen ein Test etwas gezeigt hat:

* **Block 06:** `strlen()` zählt Bytes, nicht Zeichen. Das ä in "Länge" ergab 36 statt 35, der
  Test hat es gefunden, behoben mit `mb_strlen()`.
* **Block 07:** Der alte Comparator mit `return -1;` sortierte zwei Elemente zufällig richtig. Gute
  Tests brauchen genug Fälle.
* **Block 08:** Der Playwright-Test hat den umgekehrten "Name is required"-Bug gefunden.
* **Block 10:** Eine Anfrage mit unbekannter Rezept-ID liefert 200 mit leerem Body statt 404.

### Was bringt mir das im Berufsalltag?

* Tests sind ein Sicherheitsnetz beim Refactoring.
* Die CI-Pipeline fängt Fehler vor dem Merge ab.
* Reviews finden Fehler, die ich selbst nicht sehe.

### Was möchte ich selbstständig weiter vertiefen?

* Playwright in der CI, damit Browser-Tests bei jedem Push laufen.

### Was war Zeitverschwendung?

* Setup-Probleme: der fehlende `.mvn`-Ordner, Lombok unter Java 21, das Docker-Setup. Sie haben
  mehr Zeit gekostet als die eigentlichen Testing-Themen.

## Aufgabe 2: Blick in die Zukunft

### Was würde ich im Berufsalltag verändern?

* CI in jedem Repository einrichten, damit nichts mit einem roten Build gemergt wird.
* Vor jedem Merge ein Review verlangen.

### Würde ich alles gleich beibehalten?

Nein. Soweit ich weiss, fehlen beide Punkte bei uns im Moment.

### Nächste Schritte

1. GitHub Actions in meinem eigenen Projekt einrichten, mit Build und Tests bei jedem Push.
2. Playwright-Tests in diese Pipeline aufnehmen.
3. Mit meinem Team die Regel "kein Merge ohne Review" vereinbaren.
