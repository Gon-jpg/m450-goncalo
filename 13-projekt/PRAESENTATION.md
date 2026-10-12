# Block 13 – Präsentation (10 Minuten)

| Min | Folie | Inhalt | Zeigen |
|-----|-------|--------|--------|
| 0:00–1:00 | 1. Ausgangslage | Recipe-Planner (React + Spring Boot). Ziel: Einkaufsliste aus mehreren Rezepten. | — |
| 1:00–2:30 | 2. Architektur | Diagramm; Logik im `ShoppingListService` ohne DB. | Mermaid-Diagramm aus `README.md` |
| 2:30–3:30 | 3. Planung | 3 PRs, Reihenfolge und Grund. | Tabelle Planung |
| 3:30–6:30 | 4. TDD (Kern) | Regeln R1–R7 → Tests. Red 1 → Green 1 → Red 2 (3 Lücken) → Green 2 → Refactor `Map.merge`. | `git log --oneline` und Test-Output rot/grün |
| 6:30–8:00 | 5. Testkonzept und Pipeline | Ebenen (Unit / Controller / Mockito), bekannte Lücken, CI, Actions-Screenshot | Actions-Log, JaCoCo-Report |
| 8:00–9:00 | 6. Reviews | Was Jan fand, was ich geändert habe. | 1–2 PR-Kommentare (nach dem Review ergänzen) |
| 9:00–10:00 | 7. KI und Fazit | Wofür KI, was ich geprüft habe; 3 Lehren aus TDD. | Deklaration, Reflexion |

Live-Demo (optional, 1 Min. aus Folie 4): `./mvnw test -Dtest=ShoppingListServiceTest` → 10 grün.

Mögliche Fragen:
- Warum Name und Einheit als Schlüssel? → Keine Einheitenumrechnung, sonst wäre 1 l + 2 dl falsch oder raten.
- Warum wird eine unbekannte ID erst in PR #5 ein 404? → Kleine PRs; PR #4 ist bewusst vorläufig und minimal.
- Was fehlt noch? → Test «Original nicht verändert», Zutat ohne Namen.
