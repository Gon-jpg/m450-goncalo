# 03 Testkonzept — PriceCalculator

Testkonzept nach IEEE 829 (Auszug der wichtigsten Elemente), angewendet auf das
`PriceCalculator`-Projekt aus 01-grundlagen.

## Introduction

`PriceCalculator` ist eine Konsolenapplikation, die basierend auf Grundpreis,
Sondermodellaufschlag, Zubehörpreis und Anzahl Extras den finalen Verkaufspreis inklusive
gestaffeltem Zubehörrabatt berechnet und entscheidet, ob und wie stark ein Rabatt angewendet
wird.

## Big Picture / Test Items

Aktueller Umfang: eine einzelne Klasse (`PriceCalculator`) mit einer Methode
(`CalculatePrice`) sowie ein Test-Treiber (`Main.cs`, `Program.cs`), der diese Methode über
mehrere Testfälle prüft.

```
PriceCalculator (Test Item)
  └─ CalculatePrice(basePrice, specialPrice, extraPrice, extras, discount)
Main.cs (Testtreiber)
  └─ ruft CalculatePrice mit mehreren Fällen auf und prüft das Resultat
```

Bei zukünftigem Ausbau (z. B. mehrstufige Berechnungen über mehrere Klassen) würden weitere
Test Items dazukommen: Unit-Tests pro neuer Methode sowie Integrationstests für das
Zusammenspiel mehrerer Methoden/Klassen.

## Test Features (zu testen)

- Korrekte Berechnung von Grundpreis abzüglich Händlerrabatt
- Sondermodellaufschlag wird korrekt addiert, ohne selbst rabattiert zu werden
- Gestaffelter Zubehörrabatt (0%/10%/15%) abhängig von der Anzahl Extras
- Grenzfälle: Händlerrabatt grösser als Zubehörrabatt, Zubehörrabatt-Schwellenwerte bei
  genau 3 bzw. 5 Extras

## Features not to be tested

- Konsolen-Ein-/Ausgabe (UI/Formatierung)
- Performance / Last
- Mehrbenutzerbetrieb (nicht relevant für eine einzelne Berechnungsmethode)

## Testvorgehen

`PriceCalculator` selbst wurde zuerst implementiert und danach mit dem Testtreiber geprüft
(nicht nach TDD). Ab jetzt werden neue Funktionalitäten in diesem Projekt nach TDD entwickelt:
ein kleiner, zunächst fehlschlagender Test pro Zyklus (Red), dann die minimal notwendige
Implementierung, um ihn zu bestehen (Green), anschliessend Refactoring ohne
Verhaltensänderung. Es wird mit dem einfachsten Testfall begonnen und pro Zyklus eine weitere
Anforderung als neuer Test ergänzt (inkl. Grenzwerttests, die einen entdeckten Bug gezielt
abdecken würden).

## Item Pass/Fail-Kriterien

Ein Testfall gilt als bestanden, wenn das berechnete Resultat innerhalb einer definierten
Toleranz (0.0001) mit dem erwarteten Resultat übereinstimmt — nötig, da `double`-Werte
binäre Gleitkommazahlen sind und exakte Gleichheit (`==`) nach mehreren Rechenschritten zu
falschen Fehlschlägen führen könnte.

## Testumgebung

- Sprache/Runtime: C# / .NET (dotnet CLI)
- Kein Testframework — einfacher Testtreiber (`Main.cs`) mit `RunTest`/`AssertEqual`
- Ausführung via `dotnet run`

## Kurze Planung

Für die Umsetzung von `PriceCalculator` inkl. TDD-Vorgehen wurden ca. 1–1.5 Stunden
aufgewendet: ca. 15 Minuten für die Übersetzung der ursprünglichen Methode nach C# und das
Erkennen der unerreichbaren Bedingung (`extras >= 5`), ca. 30–45 Minuten für das schrittweise
Schreiben der Tests nach dem Red-Green-Refactor-Zyklus (inkl. des Tests, der den
Branch-Order-Bug aufgedeckt hätte), sowie ca. 15–20 Minuten für Refactoring und Dokumentation
der Ergebnisse.
