# 05 Unit Testing (JUnit) — Antworten

## Aufgabe 1: Simpler Rechner

`Calculator`-Klasse in `calculator/src/main/java/ch/tbz/m450/calculator/Calculator.java`
(add/subtract/multiply/divide, `divide` wirft `IllegalArgumentException` bei Division durch
0). JUnit-5-Testklasse in `calculator/src/test/java/ch/tbz/m450/calculator/CalculatorTest.java`
(12 Testfälle). Ausführung:

- In der Entwicklungsumgebung: über den Test-Runner der IDE
- Mit Maven auf der Kommandozeile: `mvn test` (im Ordner `calculator/`) — alle 12 Tests grün

## Aufgabe 2: JUnit Zusammenfassung

### Annotations

| Annotation | Zweck |
|---|---|
| `@Test` | Markiert eine Methode als auszuführenden Testfall. |
| `@BeforeEach` | Läuft vor jeder Testmethode, typischerweise für Setup. |
| `@AfterEach` | Läuft nach jeder Testmethode, typischerweise für Cleanup. |
| `@DisplayName` | Gibt einem Test/einer Klasse einen lesbaren Namen in Reports. |
| `@ParameterizedTest` | Führt dieselbe Testlogik mehrfach mit unterschiedlichen Eingabewerten aus (kombiniert mit einer Quelle wie `@ValueSource` oder `@CsvSource`). |

### Gängige Assertions

| Methode | Zweck |
|---|---|
| `assertEquals(expected, actual)` | Prüft, dass zwei Werte gleich sind. |
| `assertNotEquals(expected, actual)` | Prüft, dass zwei Werte ungleich sind. |
| `assertTrue(condition)` | Prüft, dass eine Bedingung `true` ist. |
| `assertFalse(condition)` | Prüft, dass eine Bedingung `false` ist. |
| `assertNull(object)` | Prüft, dass ein Objekt `null` ist. |
| `assertNotNull(object)` | Prüft, dass ein Objekt nicht `null` ist. |
| `assertThrows(Exception.class, executable)` | Prüft, dass ein Codeblock eine bestimmte Exception wirft. |
| `assertAll(...)` | Gruppiert mehrere Assertions, sodass alle geprüft werden, auch wenn einzelne fehlschlagen. |

### Beispiel: `@BeforeEach` + `@Test`

```java
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("2 + 3 should equal 5")
    void addsTwoNumbers() {
        assertEquals(5, calculator.add(2, 3));
    }
}
```

### Beispiel: `@ParameterizedTest` + `@ValueSource`

```java
@ParameterizedTest
@ValueSource(strings = {"racecar", "level", "noon"})
void isPalindrome_shouldReturnTrue(String input) {
    assertTrue(StringUtils.isPalindrome(input));
}
```

### Beispiel: `assertThrows`

```java
@Test
void divideByZero_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> calculator.divide(10, 0));
}
```

### Referenz

[JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/) — die offizielle
Dokumentation; die Abschnitte "Writing Tests" und "Parameterized Tests" decken alles oben
Genannte vertieft ab.

## Aufgabe 3: Banken Simulation studieren

Projekt: `bank-simulation/` (separates Vorgabe-Projekt von der Block-02-Banking-App, Java/
Maven, Package `ch.schule`).

### Class Overview

**`Account`** (abstract base class)
- Hält gemeinsamen State: `id`, `balance`, sowie eine Liste von `Booking`-Einträgen
  (`bookings`).
- `deposit(date, amount)`: lehnt negative Beträge ab, prüft `canTransact(date)`, addiert dann
  zum Saldo und erstellt eine positive `Booking`.
- `withdraw(date, amount)`: gleiche Guards, erstellt aber eine `Booking` mit **negativem**
  Betrag (Kommentar im Code weist selbst darauf hin: `// Achtung: hier Buchung mit negativem
  Betrag!`). Diese Basisversion prüft **nicht** auf ausreichendes Guthaben — das überlassen
  die Subklassen sich selbst.
- `canTransact(date)`: erzwingt, dass Transaktionen chronologisch sind — ein neues
  Transaktionsdatum muss ≥ dem Datum der letzten Buchung sein. Die erste Transaktion ist immer
  erlaubt.
- `print()` / `print(year, month)`: geben den Kontoauszug aus (vollständig oder auf einen
  Monat gefiltert), berechnen dabei laufend den Saldo.
- Hat ein ungenutztes `getBooking()`/`setBooking()`-Paar, gestützt auf ein einzelnes privates
  `Booking booking`-Feld — sieht wie Dead Code aus, unabhängig von der eigentlich genutzten
  `bookings`-Liste.

**`SavingsAccount extends Account`**
- Überschreibt `withdraw()` um eine Regel zu ergänzen: Abhebung wird abgelehnt, falls
  `balance < amount` (kein Minus-Saldo möglich). Sonst Delegation an `super.withdraw()`.

**`SalaryAccount extends Account`**
- Hat ein zusätzliches Feld `creditLimit` (repräsentiert einen erlaubten Kreditrahmen, i.d.R.
  eine negative Zahl).
- Überschreibt `withdraw()`: lehnt ab, falls der Saldo nach der Abhebung unter `creditLimit`
  fallen würde.

**`PromoYouthSavingsAccount extends SavingsAccount`**
- Überschreibt `deposit()`: addiert 1% Bonus (`amount / 100`, Ganzzahldivision) zu jeder
  Einzahlung, bevor sie an die Kette weitergegeben wird.
- Erbt die Abhebungs-Einschränkung von `SavingsAccount` (kein Minus-Saldo).

**`Booking`**
- Unveränderliches Value-Objekt: `date` (ein `int`, kein echter Datumstyp) und `amount`
  (positiv bei Einzahlungen, negativ bei Abhebungen).
- `print(balance)` formatiert eine Kontoauszugszeile über `BankUtils`.

**`Bank`**
- Fungiert als Registry/Facade über alle Konten, indiziert nach Kontonummer in einer
  `TreeMap<String, Account>`.
- Konto-Erstellungsmethoden (`createSavingsAccount`, `createPromoYouthSavingsAccount`,
  `createSalaryAccount`) generieren IDs mit typspezifischem Präfix (`S-`, `Y-`, `P-`) plus
  einem hochzählenden Zähler (`nextAccountId`, Start bei 1000).
- Delegiert `deposit`/`withdraw`/`getBalance`/`print`-Aufrufe an das entsprechende `Account`
  per ID, gibt einen sicheren Default zurück (`false`/`0`/no-op), falls die ID nicht existiert.
- `printTop5()` / `printBottom5()`: sortieren alle Konten nach Saldo (über
  `AccountBalanceComparator`/`AccountInverseBalanceComparator`) und geben die Top/Bottom 5 aus.
- Hat ebenfalls ein ungenutztes `getAccount()`/`setAccount()`-Paar, gleiches Dead-Code-Muster
  wie bei `Account`.

### Beziehungen (Klassendiagramm)

- `Account` ist abstrakt; `SavingsAccount` und `SalaryAccount` erben direkt davon.
- `PromoYouthSavingsAccount` erbt von `SavingsAccount` (nicht direkt von `Account`) — eine
  3-stufige Hierarchie.
- `Account` hat eine Liste von `Booking` (Komposition).
- `Bank` hat eine Map von `Account` (Aggregation/Komposition) und erzeugt sie über
  Factory-artige Methoden.

### Auffälligkeiten (relevant für Aufgabe 4)

- `Bank.getBalance()` (ohne Argument) verwendet `balance -= aa[i].getBalance();` — gibt also
  die **negative** Summe aller Kontostände zurück. Fast sicher ein Bug, keine bewusste
  Vorzeichenkonvention.
- `createSalaryAccount(creditLimit)` lehnt die Anfrage ab, wenn `creditLimit > 0` — nur null
  oder negative Kreditlimiten werden akzeptiert. Ergibt Sinn, wenn `creditLimit` den
  "niedrigsten erlaubten Saldo" repräsentiert, aber potenzielle Verwechslungsquelle.
- Daten sind reine `int`-Werte (z.B. `(year - 1970) * 360 + (month - 1) * 30`) — der
  "Kalender" nutzt 360-Tage-Jahre und 30-Tage-Monate, keine echte Kalenderarithmetik.
- `Account.withdraw()` (Basisklasse) erlaubt beliebig negativen Saldo; die Absicherung liegt
  komplett bei den Subklassen-Overrides (`SavingsAccount`, `SalaryAccount`) — passend dazu,
  dass `Account` abstrakt ist.

## Aufgabe 4: Unit-Tests implementieren

Tests für `bank-simulation/` implementiert in
`src/test/java/ch/schule/bank/junit5/{AccountTests,BankTests,SavingsAccountTests,
SalaryAccountTests,PromoYouthSavingsAccountTests,BookingTests}.java` — die ursprünglich
gegebenen `fail("toDo")`-Stubs wurden durch echte Testfälle ersetzt. 38 Tests, alle grün
(`mvn test`).

Code Coverage (via JaCoCo, `mvn test` generiert den Report unter `target/site/jacoco/`):

| Klasse | Coverage |
|---|---|
| `Bank` | 100% |
| `SavingsAccount` | 100% |
| `SalaryAccount` | 100% |
| `PromoYouthSavingsAccount` | 100% |
| `Booking` | 100% |
| `AccountBalanceComparator` / `AccountInverseBalanceComparator` | 100% (indirekt über `printTop5`/`printBottom5` abgedeckt) |
| `Account` | 98% |
| `BankUtils` | 93% |
| `Main` | 0% (manueller Demo-Runner, nicht Teil der Testtreiber-Logik) |
