# m450 - Applikationen Testen

Gruppenarbeit-Repository für das Modul **m450 (Applikationen Testen)**, TBZ.

Modul-Quelle (offizielle Unterlagen, als Referenz lokal geklont, nicht Teil dieses Repos –
siehe `.gitignore`): https://gitlab.com/ch-tbz-it/Stud/m450/m450

## Struktur

Ein Ordner pro Themenblock, wie im [Modulstart](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/modulstart/README.md)
gefordert. Jeder Ordner enthält eine `TASKS.md` mit Lernzielen, den konkreten Aufgaben aus den
`UEBUNGEN.md` der Quelle und einer Checkliste. Lösungen (Code, Markdown-Tabellen, etc.) werden
direkt in den jeweiligen Ordner gelegt.

| Ordner | Thema |
|---|---|
| [01-grundlagen](01-grundlagen/TASKS.md) | Grundlagen zu Testing & Vorgehensmodelle |
| [02-teststrategie](02-teststrategie/TASKS.md) | Teststrategie |
| [03-testkonzept](03-testkonzept/TASKS.md) | Testkonzept (IEEE 829) |
| [04-test-levels](04-test-levels/TASKS.md) | Test Levels |
| [05-unit-testing](05-unit-testing/TASKS.md) | Unit Testing (JUnit) |
| [06-test-driven-development](06-test-driven-development/TASKS.md) | TDD (PHPUnit, Red/Green/Refactor) |
| [07-schnittstellen](07-schnittstellen/TASKS.md) | Schnittstellen testen / Mocking |
| [08-automation-testing](08-automation-testing/TASKS.md) | Automation Testing (E2E, Load Testing) |
| [09-code-reviews](09-code-reviews/TASKS.md) | Code Reviews (Pull Requests) |
| [10-ci-cd-pipeline](10-ci-cd-pipeline/TASKS.md) | CI/CD Pipeline |
| [11-deployment-environment](11-deployment-environment/TASKS.md) | Deployment Environments |
| [12-auswertungen](12-auswertungen/TASKS.md) | Auswertungen / Reflexion |
| [13-projekt](13-projekt/TASKS.md) | Abschlussprojekt |

> Hinweis: Der offizielle Modulstart nennt eine kürzere Ordnerliste
> (`grundlagen`, `teststrategie`, `test-levels-unit-testing`, `schnittstellen`, `automation-testing`).
> Diese Struktur hier ist feiner aufgeteilt (ein Ordner pro tatsächlichem Themenblock im Repo,
> inkl. später hinzugekommener Blöcke wie testkonzept, code-reviews, ci-cd-pipeline,
> deployment-environment und dem Projekt) — das erfüllt die Vorgabe inhaltlich, ist aber
> übersichtlicher für zwei Personen, die parallel an unterschiedlichen Blöcken arbeiten.

## Team

Beide Gruppenmitglieder benötigen Schreibzugriff auf dieses Repository (siehe
[Modulstart](m450-source/Unterlagen/modulstart/README.md) bzw. offizielle Quelle oben).

## KI-Nutzung

Siehe [ki-nutzungsrahmen](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/ki-nutzungsrahmen/README.md):
KI-Werkzeuge dürfen nur als Nachschlagewerk für einzelne Detailfragen verwendet werden — **nicht**,
um ganze Testklassen, Testfälle oder das Projekt generieren zu lassen. Im Projektordner
([13-projekt](13-projekt/TASKS.md)) muss die KI-Nutzung zusätzlich explizit im Projekt-README
deklariert werden.

## Bewertung

* **Übungen** (10-20%): Engagement bei den Aufgaben in den Themenblöcken
* **Theorieprüfung** (30-40%): siehe [Prüfungsstoff](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/pruefung/README.md)
* **Projekt**: Testkonzept, Unit-/Integrationstests, Mocking, automatisierte Testreports in einer
  Pipeline — siehe [13-projekt/TASKS.md](13-projekt/TASKS.md)

## Gesamt-Checkliste

- [ ] Repo erstellt, beide Teammitglieder haben Zugriff, Link der Lehrperson mitgeteilt
- [ ] `ki-nutzungsrahmen` gelesen und verstanden
- [ ] 01-grundlagen erledigt
- [ ] 02-teststrategie erledigt
- [ ] 03-testkonzept erledigt
- [ ] 04-test-levels erledigt
- [ ] 05-unit-testing erledigt
- [ ] 06-test-driven-development erledigt
- [ ] 07-schnittstellen erledigt
- [ ] 08-automation-testing erledigt
- [ ] 09-code-reviews erledigt
- [ ] 10-ci-cd-pipeline erledigt
- [ ] 11-deployment-environment erledigt
- [ ] Projekt: Scope mit Zeitinvestition abgeglichen
- [ ] Projekt: KI-Nutzung im Projekt-README deklariert
- [ ] Projekt: laufend kleine Commits
- [ ] Projekt: Unit-, Integrationstests + gemockte Schnittstellen vorhanden
- [ ] Projekt: Tests automatisiert ausgeführt, reportet, in Pipeline auf `main` geprüft
- [ ] Projekt: 3 aktiv kommentierte Pull Requests pro Teammitglied
- [ ] Projekt: 10-Minuten-Präsentation vorbereitet
- [ ] 12-auswertungen erledigt (Reflexion)
- [ ] Theorieprüfung vorbereitet (alle Übungen mit Lehrperson besprochen)
