# Block 09 – Code Reviews (Recipe Planner)

Wir haben zu zweit je dasselbe Feature unabhängig implementiert, je einen Pull Request erstellt und uns gegenseitig reviewt. Dieses Dokument hält die Review-Kommentare beider Richtungen fest und zieht die Erkenntnisse aus dem Vergleich.

Paar: Gonçalo (`Gon-jpg`) und Jan (`BusyJan`). Beide PRs laufen über GitHub.

## Aufgabe 1 – „Recipe hinzufügen" (Frontend)

### Unser PR #1 „Add Recipe" (`feature/add-recipe` → `main`)

Review durch Jan am 12.10.2026. Betroffene Datei: `AddRecipe.js`.

| Stelle | Kommentar von Jan | Unser Entscheid | Begründung |
|--------|-------------------|-----------------|------------|
| `AddRecipe.js:85` | Bei einem 400 steht trotzdem «is the backend running?». Nur wenn gar keine Response da ist, ist das Backend tot, sonst den Status anzeigen. | Umsetzen | Ein 400 ist kein Netzwerkfehler. Der Catch muss `err.response` prüfen und sonst den Status zeigen. |
| `AddRecipe.js:127` | Den `type="button"`-Punkt nimmt Jan zurück: react-bootstrap setzt das selbst, der Klick submitted nicht. | Zur Kenntnis | Reviewer korrigiert sich selbst; keine Änderung nötig. |
| `AddRecipe.js:127` | Ein Rezept ganz ohne Zutat kommt durch: die For-Schleife läuft nicht, `validate` sagt nichts. Gewollt? Sonst „mindestens eine Zutat". | Umsetzen | Ein leeres Rezept ist im Formular selten gewollt. Eine Zeile „mindestens eine Zutat" reicht. |
| `AddRecipe.js:83` | Submit zweimal hintereinander → zwei POSTs, Rezept liegt doppelt in der Liste. Button bleibt klickbar bis die Antwort da ist. | Umsetzen | Button während des Requests sperren. |
| `AddRecipe.js:30` | `name.trim()` ja, `imageUrl` und `description` nein. Ein Leerzeichen vom Pasten bleibt dran und das Bild geht nicht auf. | Umsetzen | Alle Textfelder gleich behandeln. |
| `AddRecipe.js:20` | `3000000000` kommt durch (`Number.isInteger` und Browser-`step=1`), aber `amount` ist im Backend ein `int` (max. 2147483647). `validate` ist grün, der POST stirbt trotzdem. | Umsetzen | Obergrenze im Frontend prüfen, sonst schlägt der POST erst im Backend fehl. |

### Jans PR #8 „feat: add recipe formular"

Unser Review (Gonçalo) am Code:

| Stelle | Unser Kommentar | Empfehlung |
|--------|-----------------|------------|
| `AddRecipe.js:67` | `Number(ing.amount) || 0`: bei „abc" oder leerem Feld wird still 0 gespeichert, ohne Fehlermeldung. | `type="number"` mit `min` plus Prüfung auf leere Menge. |

### Erkenntnisse Aufgabe 1

- **Beide Seiten verschlucken den HTTP-Status:** Sowohl unser als auch Jans Add-Formular werfen jeden Fehler in einen einzigen Catch („Backend nicht erreichbar"). Nur unser Edit-Formular (PR #3) unterscheidet schon, was Jan ausdrücklich gelobt hat. Lehre: `err.response` vom Netzwerkfehler trennen.
- **Beide Seiten akzeptieren ungültige Mengen still:** Jan speichert bei „abc"/leer eine 0, wir machen mit `Number(...) || 0` dasselbe. Lehre: Eingaben validieren statt auf einen Default zurückzufallen.
- Review ist keine Einbahnstrasse: Jan hat einen eigenen Punkt (`type="button"`) selbst zurückgenommen — konstruktiv bleiben heisst auch, sich zu korrigieren.

## Aufgabe 2 – „Recipe editieren" (Frontend + Backend)

### Unser Backend-PR #2 „Edit Recipe (backend)" (`feature/edit-recipe-backend` → `main`)

| Stelle | Kommentar von Jan | Unser Entscheid | Begründung |
|--------|-------------------|-----------------|------------|
| `RecipeService.java:42` | `existing` wird nur als Existenzcheck gebraucht; gespeichert wird ein neues Objekt aus dem Body, die ID kommt aus dem Pfad. | Bewusst nicht | Es funktioniert und ist lesbar; reine Stilfrage, kein Fehler. |
| `RecipeService.java:42` | Eine Zutat wegnehmen ist ungetestet: an der Entity steht `orphanRemoval`, aber kein Test prüft, ob die Zeile wirklich verschwindet. | Umsetzen | Test ergänzen, der eine Zutat entfernt und das Löschen prüft. |
| `RecipeControllerUpdateTest.java:70` | Der 404-Test ruft `create()` auf und legt eine Suppe an, die der Test nie anfasst. Ein leeres Rezept im Body reicht. | Umsetzen | Test soll keine Daten anlegen, die er nicht prüft. |
| `RecipeService.java:42` | PUT ohne `ingredients` (Feld fehlt oder `null`) gibt 500: der Mapper setzt die Liste auf `null`, Hibernate wirft beim Flush. Vor dem Save `null` auf eine leere Liste setzen. | Umsetzen | Ein fehlendes Feld darf keinen 500 auslösen. |
| `RecipeService.java:42` | Gehört die Zutat-ID im Body zu einem anderen Rezept, hängt der Save die Zeile um (das andere Rezept verliert die Zutat, Status trotzdem 200). | Umsetzen | Fremde IDs ignorieren und Zutaten als neue Zeilen anlegen. |

### Unser Frontend-PR #3 „Edit Recipe (frontend)" (`feature/edit-recipe-frontend` → `feature/add-recipe`)

| Stelle | Kommentar von Jan | Unser Entscheid | Begründung |
|--------|-------------------|-----------------|------------|
| `AddRecipe.js:56` | `{}`-Einwand zurückgenommen. Echtes Problem: kein Ladezustand — ein spätes GET macht `setFormData` und überschreibt, was man schon getippt hat. | Umsetzen | Ladezustand einbauen, damit das GET die Eingabe nicht platt macht. |
| `AddRecipe.js:114` | Beim PUT eines gelöschten Rezepts kommt 404, der Catch sagt trotzdem «is the backend running?». 404 ist nicht «Spring ist aus». | Umsetzen | Gleiche Statusbehandlung wie im Add. |
| `AddRecipe.js:110` | Der PUT hängt nur an der ID aus der URL. Solange das GET läuft, ist das Formular leer und Submit trotzdem klickbar — ein Name reicht, `ingredients` ist `[]`, das Backend wirft die Zutaten weg. | Umsetzen | Submit sperren, solange nicht geladen ist. |
| `App.js:23` | Navbar-Links sind relativ (`planer`, `new-menues`); von `/edit/:id` aus wird daraus `/edit/planer` und die Route matcht sich selbst. | Umsetzen | Führenden Slash ergänzen. |

### Jans PR #9 „feat: edit recipe"

Unser Review (Gonçalo) am Code:

| Stelle | Unser Kommentar | Empfehlung |
|--------|-----------------|------------|
| `EditRecipe.js:63` | Beim Laden wird `ing.comment` nie gelesen, beim Speichern geht immer `comment: ''` raus. Wer editiert, überschreibt vorhandene Zutaten-Kommentare mit leer. | Kommentar mitladen und wieder mitschicken. |
| `RecipeService.java:30` | `findById(...).orElse(null)` und im Controller immer `HttpStatus.OK`: bei unbekannter ID kommt 200 mit leerem Body oder 500, kein 404. Beim PUT ist das 404 sauber gelöst, beim GET fehlt es; im Frontend landet das im Catch mit «Backend auf :8080?». | GET ebenfalls auf 404 umstellen. |

### Erkenntnisse Aufgabe 2

- **Der Lade-Pfad ist bei beiden die schwächste Stelle.** Bei uns überschreibt ein spätes GET die Eingabe (PR #3), bei Jan gehen beim Speichern nicht geladene Felder verloren (`comment`, PR #9). Beide Male steckt dieselbe Ursache dahinter: es gibt keinen sauberen Ladezustand und nicht alle Felder werden durchgereicht. Lehre: Zustand vor dem Absenden einfrieren und alle Felder laden *und* wieder senden.
- **404 bei unbekannter ID fehlte unabhängig auf derselben Seite:** Jan hat es im PUT sauber (und lobt es bei uns), aber im GET fehlt es — genauso wie bei uns im Block-13-GET. Wir haben dasselbe Muster also auf beiden Seiten unabhängig gefunden.
- **`setId` vor dem Save** ist der eigentliche Unterschied zwischen Update und Insert (JPA). Jan hat es selbst hervorgehoben; in unserem Review war es der erste geprüfte Punkt.

## Zusammengetragene Erkenntnisse

1. **HTTP-Status und Netzwerkfehler trennen.** Wirft der `catch` alles in einen Topf, sieht ein 400/404 gleich aus wie ein totes Backend. Beide Seiten hatten das Problem mindestens einmal.
2. **Eingaben validieren statt Defaults setzen.** `Number(x) || 0` und `amount` ungeprüft durchreichen verschlucken Fehler, die erst im Backend (oder gar nicht) auffallen.
3. **Der Ladezustand ist ein eigenes Feature.** Ein später GET, der die Eingabe überschreibt, ist der erste sichtbare Bug bei langsamem Backend — bei uns und bei Jan.
4. **Gegenseitiges Review findet die gleichen Muster auf beiden Seiten.** Add vs. Edit, GET vs. PUT: dieselben Fehler tauchen unabhängig voneinander auf, was die Reviews umso wertvoller macht.
5. **Review ist konstruktiv und korrigiert sich selbst.** Jan hat einen eigenen Punkt zurückgenommen; wir haben Punkte als „reine Stilfrage" bewusst nicht umgesetzt.

## Auswertung der Review-Punkte

- **Umzusetzen (13):** Add: 5 · Edit-Backend: 4 · Edit-Frontend: 4.
- **Bewusst nicht (1):** `existing` nur als Existenzcheck (Stil, funktioniert).
- **Zur Kenntnis (1):** zurückgezogener `type="button"`-Punkt.
- Reine Stil-/Randpunkte (eigene frühere Notizen: `min=1 step=1` blockiert 0/2.5, fehlendes `prop-types`, unbenutzter `UNITS`-Export, doppeltes `mb-1 mb-5`) gehen als Follow-up in einen eigenen PR, nicht in diesen Review-Umfang.
