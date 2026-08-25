# 01 Grundlagen Antworten

## Aufgabe 1: Testformen aus der Praxis

### 1. Unit Test

- **Was:** Testet eine einzelne Methode isoliert, ohne echte Abhängigkeiten (DB, Netzwerk, andere Klassen). Beispiel: die Methode `calculatePrice` mit mehreren Eingabe-Kombinationen testen.
- **Wie:** Läuft komplett im Speicher, über ein Test-Framework der jeweiligen Programmiersprache (z.B. xUnit/NUnit für C#). Ausführung via `dotnet test`, dauert Millisekunden, läuft bei jedem Build.

### 2. Integrationstest

- **Was:** Testet, ob mehrere Komponenten korrekt zusammenarbeiten — z.B. ob ein Order-Service Daten korrekt in eine (Test-)Datenbank schreibt.
- **Wie:** Läuft gegen einen In-Memory-Testserver, oft kombiniert mit einer echten Test-Datenbank. Dauert einige Sekunden.

### 3. End-to-End (E2E) Test

- **Was:** Testet den kompletten Ablauf aus Anwendersicht, von Frontend bis Backend/Datenbank — z.B. ein Nutzer wählt ein Auto + Zubehör aus, schliesst die Bestellung ab und sieht den korrekten Endpreis.
- **Wie:** Automatisiert über einen echten Browser (Browser-Automation). Dauert zehn bis mehrere zehn Sekunden pro Test, läuft seltener (z.B. vor einem Release) statt bei jedem Commit.

## Aufgabe 2: Fehler, Mangel, hoher Schaden

- **SW-Fehler:** Ein User kann einen anderen User löschen, obwohl er dazu keine Berechtigung haben sollte. Die Anforderung (nur eigene Daten löschen dürfen) wird nicht erfüllt — klare Abweichung IST vs. SOLL, vermutlich ein Autorisierungsfehler.
- **SW-Mangel:** Daten werden in einem Chart korrekt berechnet, aber so dargestellt, dass sie leicht falsch interpretiert werden (z.B. irreführende Skalierung/Achsenbeschriftung). Die Berechnung selbst ist korrekt, die Darstellung erfüllt aber nicht den eigentlichen Bedarf der Nutzer.
- **Hoher Schaden:** Das Jahr-2000-Problem (Y2K) — durch zweistellige Jahresfelder in vielen Systemen drohte beim Wechsel von 31.12.1999 23:59 auf 01.01.2000 00:00 eine Fehlinterpretation als Jahr 1900. Weltweit wurden Milliarden für die Behebung ausgegeben.

## Aufgabe 3

Implementierung in `PriceCalculator.cs`, Testtreiber in `Main.cs` (kein Unit-Test-Framework,
`RunTest`/`AssertEqual` prüfen die Resultate direkt). `dotnet run` führt alle 9 Testfälle aus.

`AssertEqual` vergleicht mit einer `tolerance` statt exaktem `==`: `double`-Werte sind
binäre Gleitkommazahlen und können die meisten Dezimalbrüche nicht exakt darstellen
(z.B. ist `0.1 + 0.2` in C# nicht bitgenau `0.3`). Nach mehreren Rechenschritten können sich
so winzige Rundungsabweichungen ansammeln, die bei exaktem `==` einen eigentlich korrekten
Test fälschlich fehlschlagen liessen. Die Toleranz erlaubt "nahe genug" statt "bitidentisch".

## Bonus: Logikfehler im gegebenen Code

Im gegebenen Code wird zuerst `extras >= 3` geprüft und erst danach `extras >= 5`:

```csharp
if (extras >= 3)
    addonDiscount = 10;
else if (extras >= 5)
    addonDiscount = 15;
else
    addonDiscount = 0;
```

Da eine `if`/`else if`-Kette bei der ersten zutreffenden Bedingung stehen bleibt, deckt
`extras >= 3` bereits alle Werte ab 3 aufwärts ab — inklusive 5, 6, 100 usw. Der `else if
(extras >= 5)`-Zweig wird dadurch nie erreicht und ist effektiv toter Code. Ein Auftrag mit
5 oder mehr Extras erhält so fälschlicherweise nur 10% statt der vorgesehenen 15% Rabatt
(siehe Testfall "Extras above threshold (5)" in `Main.cs`, der genau dieses Verhalten belegt).

**Korrektur:** Die Reihenfolge der Bedingungen muss vertauscht werden, sodass der höhere
Schwellenwert zuerst geprüft wird:

```csharp
if (extras >= 5)
    addonDiscount = 15;
else if (extras >= 3)
    addonDiscount = 10;
else
    addonDiscount = 0;
```
