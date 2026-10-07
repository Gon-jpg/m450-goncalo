# 07 Schnittstellen testen (Addressbook Backend, Mocking) — Antworten

## Setup

`addressbook-backend-v1-1.zip` entpackt (ohne `.idea`, `target`, `__MACOSX`), Java 21 (Temurin
21.0.10), Spring Boot 3.5.4. Problem beim ersten Testlauf: Lombok wurde nicht als Annotation
Processor ausgeführt, `Address` hatte dadurch weder Getter/Setter noch den All-Args-Konstruktor
("cannot find symbol"). Behoben, indem Lombok im `maven-compiler-plugin` explizit unter
`annotationProcessorPaths` eingetragen wurde.

Ausführung:

```
cd addressbook-backend
./mvnw test
```

## Aufgabe 1: Tests für alle Klassen

Alle Testklassen verwenden `@BeforeEach`, um vor jedem Test frische Objekte (Adressen,
Comparator, Mocks) zu erzeugen, damit kein Test vom Zustand eines anderen abhängt.

| Testklasse | Klasse | Getestet |
|---|---|---|
| `AddressTest` | `Address` | All-Args-Konstruktor speichert alle 5 Werte, Setter ändert nur das eine Feld, No-Args-Konstruktor (`id` = `0`, da primitiver `int`; `firstname` = `null`) |
| `AddressComparatorTest` | `AddressComparator` | Sortierreihenfolge in beide Richtungen, Gleichheit, `null`-Werte |
| `AddressServiceTest` | `AddressService` | `save`, `getAll` (sortiert), `getAddress` gefunden / nicht gefunden |
| `AddressControllerTest` | `AddressController` | Statuscodes 201 / 200 / 404 und Response-Body |

`AddressRepository` (Interface ohne eigenen Code, von Spring generiert) und
`AddressbookApplication` (nur Startklasse) haben keine eigene Logik und werden nicht direkt
getestet; das Repository ist genau die Abhängigkeit, die weggemockt wird.

### H2-Datenbank wegmocken (Mockito)

Im `AddressServiceTest` wird das `AddressRepository` mit `mock(AddressRepository.class)` ersetzt
und über den Konstruktor in den Service gegeben. Der Service merkt nicht, dass er mit keiner
echten Datenbank spricht; kein Test berührt H2.

* **Stubbing** (`when(...).thenReturn(...)`): Der Mock liefert vordefinierte Daten, z.B.
  `findById(99)` -> `Optional.empty()`.
* **Verifikation** (`verify(repo).save(meier)`): prüft, dass der Service den Aufruf korrekt an
  das Repository weitergibt (Behavioral Testing).

`assertSame` statt `assertEquals`, weil `Address` kein `equals()` hat; so wird geprüft, dass
genau das Objekt aus dem Repository zurückkommt.

Im `getAll`-Test liefert `findAll()` die Adressen absichtlich in falscher Reihenfolge (Zbinden,
Meier). Wäre der Input schon sortiert, würde der Test auch ohne jede Sortierung grün. Erkenntnis:
Der alte Comparator (`return -1`) hätte diesen Test trotzdem bestanden, weil Javas Sortierung bei
2 Elementen zufällig die richtige Reihenfolge ergibt. Deshalb testet `AddressComparatorTest`
immer beide Richtungen (`compare(a, b) < 0` und `compare(b, a) > 0`).

Der `AddressControllerTest` verwendet dasselbe Prinzip eine Schicht höher: `AddressService`
wird gemockt.

### Comparator korrekt implementieren

Die Vorlage gab immer `-1` zurück und verletzte damit den `Comparator`-Vertrag
(`compare(a, b)` und `compare(b, a)` waren beide "kleiner"). Erste Version: Vergleich nach
Nachname mit `String.compareTo`. Ein `null`-Nachname warf dabei eine `NullPointerException`,
was mit `assertThrows` dokumentiert wurde.

## Aufgabe 2: Comparator erweitern

Sortierung nach Nachname, bei Gleichheit nach Vorname, danach nach `id`. `null`-Werte bei
Nach- und Vorname werden mit `Comparator.nullsFirst` zuerst einsortiert statt eine Exception
zu werfen:

```java
Comparator.comparing(Address::getLastname, NULLS_FIRST)
        .thenComparing(Address::getFirstname, NULLS_FIRST)
        .thenComparingInt(Address::getId);
```

Angepasste Tests in `AddressComparatorTest`:

* `equalLastNamesReturnZero` ersetzt durch `sameLastnameSortsByFirstname` (+ umgekehrte
  Richtung) und `identicalAddressesReturnZero`, da "Hans Meier" und "Peter Meier" jetzt nicht
  mehr gleich sind.
* `nullLastNameThrowsException` ersetzt durch `nullLastnameSortsFirst`.
* Neu: `sameLastAndFirstnameSortsById`.

Bekannte Lücke: `null`-Vornamen werden durch `nullsFirst` ebenfalls behandelt, dafür existiert
aber noch kein eigener Test. `AddressServiceTest` bleibt unverändert grün (Meier vor Zbinden).

Ergebnis:

```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 -- in ch.tbz.m450.repository.AddressTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0 -- in ch.tbz.m450.util.AddressComparatorTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 -- in ch.tbz.m450.controller.AddressControllerTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 -- in ch.tbz.m450.service.AddressServiceTest
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
