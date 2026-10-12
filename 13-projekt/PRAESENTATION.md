# Block 13 – Präsentation (10 Minuten)

| Min | Folie | Inhalt | Zeigen |
|-----|-------|--------|--------|
| 0:00–1:00 | 1. Ausgangslage + Produkt | Recipe-Planner (React + Spring Boot). Ziel: Einkaufsliste aus mehreren Rezepten. | Laufende App: Planer-Seite mit Rezept-Checkboxen und der erzeugten Einkaufsliste (Screenshot oder Live-Demo) |
| 1:00–2:30 | 2. Architektur | Diagramm; Logik im `ShoppingListService` ohne DB. | Mermaid-Diagramm aus `README.md` |
| 2:30–3:30 | 3. Planung | 4 PRs, Reihenfolge und Grund. | Tabelle Planung |
| 3:30–6:30 | 4. TDD (Kern) | Regeln R1–R7 → Tests. Red 1 → Green 1 → Red 2 (3 Lücken) → Green 2 → Refactor `Map.merge`. | `git log --oneline` und Test-Output rot/grün |
| 6:30–8:00 | 5. Testkonzept und Pipeline | Ebenen (Unit / Controller / Mockito / Integration / Frontend), bekannte Lücken, CI, Actions-Screenshot | Actions-Log, JaCoCo-Report |
| 8:00–9:00 | 6. Reviews | Was Jan in den Reviews fand und was ich geändert habe; Reviews zu #4–#7 stehen noch aus. | 1–2 PR-Kommentare |
| 9:00–10:00 | 7. KI und Fazit | Wofür KI, was ich geprüft habe; Lehren aus TDD. | Deklaration (README, Abschnitt 4), Reflexion |

Live-Demo (1 Min.): Frontend + Backend starten, auf der Planer-Seite Rezepte auswählen und die Einkaufsliste zeigen. Zusätzlich `./mvnw test -Dtest=ShoppingListServiceTest` → 10 grün.

Mögliche Fragen:
- Warum Name und Einheit als Schlüssel? → Keine Einheitenumrechnung, sonst wäre 1 l + 2 dl falsch oder raten.
- Warum brauchte es überhaupt einen 404? → Eine unbekannte ID ist ein Fehler, kein leeres Rezept; vorher kam 200 mit leerem Body.
- Warum ein zusätzlicher Integrationstest? → Die Controller-Tests mocken alles; erst `@SpringBootTest` prüft Controller, Service, Mapper und Repository zusammen gegen H2.
- Was fehlt noch? → Test «Original nicht verändert», Zutat ohne Namen, echter Browser-E2E-Test.
