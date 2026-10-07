# 08 Automation Testing (Angular/Spring Boot, E2E, Load Testing) — Antworten

## Vorbereitung

`spring-boot-angular-basic-lw.zip` entpackt nach `spring-boot-angular-basic-lw2/` (ohne `.idea`,
`.iml`, `__MACOSX`). Zwei Setup-Probleme behoben:

* Im Zip fehlte der Ordner `.mvn/` des Maven Wrappers (`MavenWrapperMain` nicht gefunden) -> aus
  Block 07 übernommen.
* Spring Boot 3.1.2 bringt Lombok 1.18.28 mit, das unter Java 21 abstürzt
  (`NoSuchFieldError: JCTree$JCImport ... qualid`) -> `lombok.version` im `pom.xml` auf 1.18.38
  gesetzt.

Starten:

| | Befehl | URL |
|---|---|---|
| Backend | `./mvnw spring-boot:run` | `http://localhost:8081/students` |
| Frontend | `npm install` + `npm start` in `src/main/js/my-app` | `http://localhost:4200` |

Die App: `GET /students` liefert alle Studenten, `POST /students` speichert einen neuen (Antwort
200 ohne Body). H2 in-memory, beim Start mit 5 Studenten befüllt (Jonas, Patrick, Yves, Peter,
Ann). Das Frontend hat die Routen `/students` (Tabelle) und `/addstudents` (Formular).

## Übung 1: Backend REST-Schnittstelle automatisiert testen

**Tool:** JUnit 5 + `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate`. Die
Tests starten die ganze Applikation auf einem zufälligen Port und schicken echte HTTP-Requests an
die REST-Schnittstelle. Läuft ohne Zusatztool mit `./mvnw test` und damit auch in einer
CI-Pipeline.

Datei: `spring-boot-angular-basic-lw2/src/test/java/ch/tbz/m450/testing/tools/StudentApplicationTests.java`
(ersetzt den leeren `contextLoads`-Test; das Package muss dem der Main-Klasse entsprechen, sonst
findet `@SpringBootTest` die Konfiguration nicht).

| Test | Prüft |
|---|---|
| `getReturns200AndContainsSeedStudents` | Status 200, alle 5 Seed-Studenten enthalten |
| `postThenGetContainsNewStudent` | POST -> 200, ein folgendes GET enthält den neuen Studenten |
| `postWithEmptyBodyIsNotRejected` | POST mit `{}` wird akzeptiert (dokumentiert das aktuelle Verhalten) |

Da alle Tests denselben Spring-Kontext und damit dieselbe Datenbank verwenden, prüfen sie mit
`>=` und "enthält" statt mit exakten Anzahlen, und neue Studenten bekommen einen eindeutigen
Namen (UUID). So sind die Tests unabhängig von der Reihenfolge.

**Befund:** Das Backend hat keine Validierung. `POST /students` mit leerem Body `{}` liefert
`200 OK` und speichert einen Studenten ohne Namen und E-Mail. Sobald eine Validierung existiert,
muss dieser Test auf einen 4xx-Status umgestellt werden.

```
POST /students with {} -> 200 OK
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Übung 2: Frontend End-to-End Test

**Tool:** Playwright (`@playwright/test`, Chromium). Die Tests steuern einen echten Browser gegen
das laufende Frontend (4200) und Backend (8081). Bei jedem Test wird ein Screenshot erstellt, der
HTML-Report dient als Nachweis der Browser-Ausführung.

Dateien: `src/main/js/my-app/playwright.config.ts`, `src/main/js/my-app/e2e/students.spec.ts`

Ausführen (Backend und Frontend müssen laufen):

```
cd src/main/js/my-app
npx playwright install chromium
npx playwright test            # headless
npx playwright test --headed   # sichtbarer Browser
npx playwright show-report     # HTML-Report mit Screenshots
```

| Test | Prüft |
|---|---|
| submit button is disabled while the fields are empty | Button deaktiviert, bis Name und E-Mail ausgefüllt sind |
| adding a student navigates to /students and shows the student | Formular ausfüllen, absenden -> URL `/students`, Tabelle enthält genau eine Zeile mit dem neuen Namen |
| "Name is required" is not shown on an untouched form | bekannter Bug, mit `test.fail()` markiert |

**Befund (Bug):** Im Formular steht `[hidden]="!name.pristine"`. Die Logik ist umgekehrt: "Name
is required" wird angezeigt, solange das Feld unberührt ist, und verschwindet, sobald man tippt
(gleiches Muster beim E-Mail-Feld). Der Test ist mit `test.fail()` markiert: Er hält die Suite
grün, dokumentiert den Bug aber. Wird der Bug behoben, schlägt der Test als "unerwartet bestanden"
an und `test.fail()` muss entfernt werden.

```
Running 3 tests using 1 worker
  ✓  1 submit button is disabled while the fields are empty (750ms)
  ✓  2 adding a student navigates to /students and shows the student (696ms)
  ✘  3 "Name is required" is not shown on an untouched form (5.4s)   (expected failure)
  3 passed (9.7s)
```

Mögliche Verbesserung: Mit der Option `webServer` in `playwright.config.ts` könnte Playwright
Backend und Frontend selbst starten, aktuell müssen sie manuell laufen.

## Übung 3: Load Testing des Backends

**Tool:** k6. Szenario, Resultate (10 / 50 / 200 virtuelle User, 0 % Fehler, p95 max. 20 ms),
Befunde und Fazit: siehe [`load-test/LOAD_TEST.md`](load-test/LOAD_TEST.md), Skript:
[`load-test/students.js`](load-test/students.js).

Kurzfazit: Bis 200 gleichzeitige User ist das Backend schnell und stabil, ein echtes Limit wurde
nicht erreicht, weil das `sleep(1)` die Last auf etwa 1 Request pro User und Sekunde begrenzt. Das
grössere Risiko ist das ungepagte `GET /students` auf einer stetig wachsenden Tabelle.

## Manual vs. Automation Testing

Alle drei Übungen sind wiederholbar mit einem Befehl ausführbar und eignen sich als
Regressionstests. Der Bug im Formular (Übung 2) zeigt aber auch die Grenze: Gefunden wurde er beim
manuellen Anschauen der App, der automatisierte Test hält ihn danach nur fest. Exploratives Testen
bleibt Aufgabe des Menschen, Automation sichert ab, dass Bekanntes nicht wieder kaputtgeht.
