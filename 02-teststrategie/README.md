# 02 Teststrategie Antworten

## Übung 1: Testfälle aus Rabattregeln ableiten

Spezifikation: "weniger als 15'000 CHF" ist die einzige exakte Regel im Text (0% Rabatt).
Daraus folgt: 15'000 CHF selbst ist *nicht* "weniger als 15'000" und fällt somit in die
nächste Klasse ("bis zu 20'000" -> 5%). Analog wird die Grenze bei 25'000 aufgelöst
("darüber" -> 8,5%, wodurch 25'000 selbst zur 8,5%-Klasse gehört). Damit ergeben sich vier
lückenlose, überschneidungsfreie Klassen:

- `amount < 15'000` -> 0%
- `15'000 <= amount <= 20'000` -> 5%
- `20'000 < amount < 25'000` -> 7%
- `amount >= 25'000` -> 8,5%

### Tabelle A — Abstrakte Testfälle (Äquivalenzklassen + Grenzwerte)

| ID | Testfall (Bereich) | Erwartetes Ergebnis |
|---|---|---|
| EQ1 | amount < 15'000 | 0% |
| BD1 | amount knapp unter 15'000 | 0% |
| BD2 | amount = 15'000 | 5% |
| EQ2 | 15'000 <= amount <= 20'000 | 5% |
| BD3 | amount = 20'000 | 5% |
| BD4 | amount knapp über 20'000 | 7% |
| EQ3 | 20'000 < amount < 25'000 | 7% |
| BD5 | amount knapp unter 25'000 | 7% |
| BD6 | amount = 25'000 | 8,5% |
| EQ4 | amount >= 25'000 | 8,5% |

### Tabelle B — Konkrete Testfälle (CHF-Werte)

| ID | Betrag (CHF) | Erwartetes Ergebnis |
|---|---|---|
| TC1 | 0 | 0% |
| TC2 | 14'999.99 | 0% |
| TC3 | 15'000.00 | 5% |
| TC4 | 15'000.01 | 5% |
| TC5 | 18'500.00 | 5% |
| TC6 | 20'000.00 | 5% |
| TC7 | 20'000.01 | 7% |
| TC8 | 22'000.00 | 7% |
| TC9 | 24'999.99 | 7% |
| TC10 | 25'000.00 | 8,5% |
| TC11 | 30'000.00 | 8,5% |

## Übung 2: Funktionale Black-Box-Tests — Autovermietung

Getestete Plattform: [Europcar.com](https://www.europcar.com)

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|---|---|---|---|---|---|
| TC01 | Fahrzeugsuche mit gültigem Abhol-/Rückgabeort und Datum durchführen | Liste verfügbarer Fahrzeuge wird angezeigt | Liste verfügbarer Fahrzeuge wird angezeigt | Ausgeführt | - |
| TC02 | Rückgabedatum liegt vor Abholdatum | Validierungsfehler wird angezeigt, Suche wird blockiert | Datumsauswahl wird im Datepicker blockiert (Datum nicht wählbar), zusätzlich roter Fehlerhinweis unter dem Datepicker | Ausgeführt | - |
| TC03 | Filter nach Fahrzeugkategorie (z.B. SUV) anwenden | Nur Fahrzeuge der Kategorie SUV werden angezeigt | Nur Fahrzeuge der Kategorie SUV werden angezeigt | Ausgeführt | - |
| TC04 | Login mit ungültigen Zugangsdaten | Fehlermeldung "falsche Zugangsdaten", kein Zugriff | Fehlermeldung "falsche Zugangsdaten", kein Zugriff | Ausgeführt | - |
| TC05 | Buchung abschliessen und Buchungsbestätigung prüfen | Bestätigungsseite zeigt korrekte Daten (Ort, Datum, Fahrzeug, Preis) | Bestätigungsseite zeigt korrekte Daten (Ort, Datum, Fahrzeug, Preis) | Ausgeführt | - |

## Übung 3: Bank-Software untersuchen

Untersuchtes Projekt: `bank-software-mvn` (Konsolen-Banking-Simulation, Java/Maven).
Lokal zum Laufen gebracht via `mvn compile org.codehaus.mojo:exec-maven-plugin:3.1.0:java
-Dexec.mainClass="ch.tbz.bank.software.Main"`.

### 1. Black-Box Tests (Benutzersicht)

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|---|---|---|---|---|---|
| BB01 | Alle Konten anzeigen ("a") | Liste aller 5 Konten mit Nummer, Name und Währung wird angezeigt | Liste wurde korrekt angezeigt (Rockefeller/USD, Gates/EUR, Musk/CHF, Bezos/EUR, Branson/USD) | Pass | — |
| BB02 | Negative Einzahlung ("Deposit -100") | Fehlermeldung, Betrag wird abgelehnt | Betrag wurde akzeptiert und vom Kontostand abgezogen (Guthaben sinkt statt zu steigen) | Fail | Fehlende Validierung `amount > 0` in `deposit()` — negative Werte werden nicht abgefangen |
| BB03 | Negative Auszahlung ("Withdraw -100") | Fehlermeldung, Betrag wird abgelehnt | Betrag wurde akzeptiert; Kontostand stieg von 1100 auf 1200 statt zu sinken | Fail (kritisch) | Fehlende Validierung in `withdraw()` — `balance -= amount` mit negativem `amount` erzeugt Geld ohne Limit |
| BB04 | Überweisung zwischen unterschiedlichen Währungen (Gates/EUR → Musk/CHF) | Betrag wird gemäss aktuellem Wechselkurs EUR→CHF umgerechnet | Betrag wurde 1:1 ohne Umrechnung übertragen; Konsole zeigte Hinweis "Es wurde keine Umrechnung vorgenommen." | Fail | `convertCurrency()` deckt nur USD↔CHF und USD↔EUR ab; EUR↔CHF fehlt komplett |
| BB05 | Wechselkurs abfragen ("w") mit korrektem Format (z. B. "CHF USD") | Aktueller Wechselkurs wird via API abgerufen und angezeigt | Bei korrektem Eingabeformat funktionierte die Abfrage und lieferte einen gültigen Kurs; bei falschem Format kam "Ungültige Eingabe" | Pass (API funktioniert grundsätzlich) | Ursprüngliche Vermutung eines toten API-Keys war falsch — Fehlerursache war fehlerhaftes Eingabeformat des Nutzers |

### 2. White-Box Tests (Code-Perspektive)

Kandidaten für Unit-Tests, basierend auf den im Code sichtbaren Methoden und fehlenden Verzweigungen:

| Methode | Testbare Fälle | Grund |
|---|---|---|
| `Account.deposit(double amount)` | `amount > 0` (Normalfall), `amount == 0` (Grenzfall), `amount < 0` (sollte Exception werfen, tut es aktuell nicht) | Keine Eingabevalidierung vorhanden — direkte Ursache von BB02 |
| `Account.withdraw(double amount)` | `amount <= balance` (Normalfall), `amount > balance` (sollte fehlschlagen, korrekt behandelt via `if (amount > balance)`), `amount < 0` (sollte Exception werfen, tut es aktuell nicht) | Direkte Ursache von BB03; die vorhandene Prüfung `amount > balance` greift bei negativen Beträgen nicht |
| `Counter.convertCurrency(...)` | Alle Kombinationen: USD↔CHF, USD↔EUR, EUR↔CHF, CHF↔EUR, gleiche Währung | Aktuell fehlen die Zweige EUR↔CHF (und vermutlich CHF↔EUR), was zu stillschweigend falscher Konvertierung führt |
| `ExchangeRateOkhttp` (API-Client) | Gültiges Währungspaar, ungültiges/leeres Eingabeformat, Netzwerkfehler/Timeout, API liefert Fehlerstatus | Aktuell wird nur der "glückliche Pfad" sauber unterstützt; Format-Validierung der Eingabe ist unklar/inkonsistent |
| `Counter.AccountExeption` | Wird die Exception tatsächlich überall dort geworfen, wo sie sollte? | Klasse wird aktiv genutzt für ungültige Kontonummer & unzureichenden Kontostand, aber **nicht** für negative Beträge in `deposit()`/`withdraw()` — direkte Erklärung für BB02/BB03 |

### 3. Verbesserungsvorschläge

- **Eingabevalidierung für Beträge**: `deposit()` und `withdraw()` sollten `amount <= 0` prüfen und eine aussagekräftige Exception werfen (z. B. `AccountException`), statt den Wert stillschweigend zu verarbeiten.
- **Vollständige Konvertierungsmatrix**: `convertCurrency()` sollte alle Währungspaare (USD, EUR, CHF in beide Richtungen) abdecken — idealerweise durch eine generische Lösung (z. B. Umrechnung über eine gemeinsame Basiswährung) statt einzelner hartcodierter Zweige, um das Problem strukturell statt punktuell zu lösen.
- **Klareres Feedback bei fehlender Konvertierung**: Die Meldung "Es wurde keine Umrechnung vorgenommen." ist leicht zu übersehen und sollte prominenter (z. B. als Warnung/Abbruch) dargestellt werden, statt die Transaktion trotzdem 1:1 durchzuführen.
- **API-Key nicht hartcodieren**: Der Wechselkurs-API-Key sollte über eine Umgebungsvariable oder Konfigurationsdatei geladen werden statt im Quellcode zu stehen (Sicherheits- und Wartungsrisiko).
- **Tippfehler beheben**: `AccountExeption` → `AccountException`, um Klarheit und Konsistenz im Code zu verbessern.
- **Eingabeformat-Validierung bei Wechselkursabfrage**: Klarere Fehlermeldungen und ggf. eine Eingabeaufforderung mit Beispiel (z. B. "Bitte im Format 'CHF USD' eingeben"), um Nutzerfehler wie in BB05 zu vermeiden.
