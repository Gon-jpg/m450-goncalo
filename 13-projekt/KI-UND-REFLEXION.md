# Block 13 – Reflexion

Die KI-Deklaration steht im [README.md](README.md) (Abschnitt 4).

## TDD

**Rot vor Grün zeigt, dass der Test etwas prüft.** Beim Service-Test schlugen 5 von 7 Tests mit einem Stub fehl; nur die zwei Leer-Tests waren trivial grün.

**Der erste grüne Code war zu einfach.** Alle 7 Tests liefen, aber Trimmen des Namens, Sortieren ohne Gross-/Kleinschreibung und ingredients == null fehlten. Erst die Durchsicht des Codes brachte das auf. Daraus wurde ein zweiter Zyklus (3 neue Tests rot, dann minimaler Fix). TDD findet nur Lücken, an die man beim Testschreiben denkt.

**Refactor mit grüner Absicherung.** Die Tests blieben beim Umbau grün, deshalb musste das Verhalten nicht von Hand nachgeprüft werden.

**Ein Rot kann aus dem falschen Grund rot sein.** Beim Controller-Test lautete die Fehlermeldung `cannot find symbol: class ShoppingListController`. Der eigentliche Fehler (fehlender @MockBean RecipeEntityMapper) zeigte sich erst beim Grün. Merke: Nach dem Rot die Testklasse einmal mit einem Stub-Controller laufen lassen.

## KI-Antworten überprüfen

Beispiel: Die vorgeschlagene `Map.merge`-Zeile habe ich gegen die Java-Dokumentation zur Signatur geprüft — die `BiFunction` läuft nur bei einem doppelten Schlüssel, genau das braucht das Zusammenfassen. Danach übernommen.

Weiteres: `verifyNoInteractions` gegen die Mockito-Doku abgeglichen (der Mock darf in dem Fall gar nicht berührt werden) und im Controller-Test umgesetzt. Die Jest-Warnung `ReactDOMTestUtils.act is deprecated` als reine Bibliotheks-Deprecation von React Testing Library eingeordnet, nicht als Testfehler — die Tests laufen grün.

## Reviews

Jan hat die Block-09-PRs des gleichen Projekts reviewt (Add Recipe, Edit Recipe Frontend/Backend). Die zentralen Punkte betreffen auch diese Arbeit:

- Bei 400/404 stand pauschal «is the backend running?» statt den HTTP-Status zu zeigen.
- Ein Ladezustand fehlte: ein spätes GET überschreibt bereits getippte Eingaben.
- `imageUrl` und `description` wurden nicht getrimmt.
- Ein doppelter Submit schickte zwei POSTs raus.

Ein Punkt, den ich allein nicht gesehen hätte: dass ein Rezept mit einer fremden Zutat-ID ein anderes Rezept still umhängt (kein Fehler, Status 200). Für Block 13 (PR #4–#7) steht das Review noch aus; die oben genannten Muster habe ich an den Stellen, an denen sie auftraten, selbst beachtet.

## Was ich nächstes Mal anders mache

Vor dem Grün eine Randfall-Liste schreiben (null, leer, Leerzeichen, Gross-/Kleinschreibung) und selbst umsetzen.

Kleine PRs mit einem Zweck, damit das Review schnell geht.
