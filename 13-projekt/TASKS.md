# Abschlussprojekt (eigene Applikation, Testkonzept, CI/CD, Code Reviews)

## Lernziele

* Ich kann das in den Theorie-Blöcken erarbeitete Wissen in einem eigenen Projekt anwenden
* Ich kann Scope und Ausmass meines Projekts realistisch mit meiner verfügbaren Zeit abgleichen
* Ich kann meine KI-Nutzung im Projekt transparent deklarieren
* Ich schreibe während des Projekts laufend kleine, nachvollziehbare Commits

## Zusammenfassung der Theorie

* Scope und Zeitinvestition müssen realistisch abgeglichen werden — lieber sauber getestetes, kleineres Projekt als viel Funktionalität mit dünner Testabdeckung.
* KI-Nutzung im Projekt muss transparent im README deklariert werden (Rolle: Nachschlagewerk, Code-Schreiben mit KI-Unterstützung, automatisches Erstellen von Code durch Agent, Reviewen/Optimieren). Bei KI-Einsatz wird stärker nach Aufwand/Verständnis als nach reiner Endprodukt-Qualität geprüft.
* Es wird laufend in kleinen, nachvollziehbaren Schritten committet (nicht alles kurz vor Abgabe).
* TDD-Zyklus: Red (fehlschlagender Test für Business-Logik) → Green (Code schreiben, bis Test grün ist) → Refactor (Code/Tests verbessern) → iterativ wiederholen.
* CI/CD-Pipeline mit den Stages Build (Gradle/Maven), Test (automatisierte Unit-Tests als Artifacts), Deploy (Testresultate publizieren, z.B. GitLab Pages).
* Code Reviews laufen über Pull Requests im Team (Feature Branch → PR → Review/Diskussion via Kommentare → Approval → Merge in main/master) nach Clean-Code-Konventionen.
* Am Projektende erfolgt eine Auswertung/Reflexion: gefundene Fehler nach Kritikalität priorisieren, Ideen für die Zukunft festhalten, und rückblickend bewerten, wie Code Reviews und TDD erlebt wurden.

## Aufgaben (aus README.md, grundaufbau.md, ci-cd.md, code-reviews.md)

- [ ] Eigenes Projekt wählen: ein Projekt, das Spass macht (neu oder ein bestehendes Projekt mit wenig Testabdeckung; bei vorhandener Abdeckung eher zusätzliche Features entwickeln)
- [ ] Scope und Ausmass des Projekts kurz mit der effektiven Zeitinvestition abgleichen, bevor losgelegt wird
- [ ] KI-Nutzung im Projekt im README.md des eigenen Projekts deklarieren (welche Rolle(n) KI eingenommen hat)
- [ ] Laufend in kleinen, nachvollziehbaren Schritten committen (nicht erst kurz vor Abgabe)
- [ ] Unit / Component Tests im Projekt sichtbar machen
- [ ] Ein Mocking Framework verwenden, um Schnittstellen für gewisse Komponenten wegzumocken
- [ ] Code Coverage einsehbar machen (z.Bsp. via Sonar) und als Report darstellen
- [ ] Automatisiert Testresultate/Reports darstellen (nach Testausführung reportbar machen)
- [ ] TDD (Red/Green/Refactor) mindestens einmal im Projekt anwenden ([tdd.md](../m450-source/Unterlagen/projekt/tdd.md))
- [ ] CI/CD-Pipeline erstellen (YAML-File) mit den Stages:
  - [ ] **Build**: Code builden mit Gradle oder Maven
  - [ ] **Test**: Unit-Tests automatisiert ausführen und als Artifacts ablegen
  - [ ] **Deploy**: Testresultate publizieren (z.Bsp. GitLab Pages)
  - [ ] Sicherstellen, dass Pipeline und Reports sichtbar sind; Link auf das Repository im Miro-Board publizieren oder der Lehrperson Zugriff geben
- [ ] Im Team mit Code Reviews über Pull Requests arbeiten (Feature Branch → PR → Review gem. Clean-Code-Konventionen → Feedback einbauen → Approval → Merge)
- [ ] Pro Team-Mitglied aktiv 3 Pull Requests einsehen, kommentieren, challengen und konstruktiv Beiträge leisten (von allen Gruppenmitgliedern)
- [ ] Kleine Dokumentation erstellen:
  - [ ] Kurze Planung (ohne Gantt)
  - [ ] Grobe Architektur visualisieren
  - [ ] Kleines Testkonzept vorweisen
  - [ ] Kleine Reflexion über TDD und Code Reviews
- [ ] Kleine Teststrategie-Beschreibung im README.md des Projekts verfassen
- [ ] Reflexion am Ende des Projekts festhalten: gefundene Fehler nach Kritikalität priorisieren/einordnen, Ideen für die Zukunft festhalten, Erkenntnisse zu Code Reviews und TDD für den Berufsalltag festhalten
- [ ] Präsentation vorbereiten (10 Minuten):
  - [ ] Endprodukt kurz zeigen
  - [ ] Auf das Thema Testing eingehen
  - [ ] Reports vorzeigen
  - [ ] Reflexion über Code Reviews und TDD präsentieren
  - [ ] Kurzes Fazit ziehen

## Deliverables

* Eigenes Projekt-Repository mit README.md (inkl. KI-Nutzungs-Deklaration und Teststrategie-Beschreibung)
* Unit- und Integration-/Component-Tests inkl. gemockter Schnittstellen (Mocking Framework)
* Code-Coverage-Report (z.Bsp. Sonar)
* CI/CD-Pipeline-Konfiguration (YAML) mit Build-, Test- und Deploy-Stage inkl. veröffentlichtem Testreport
* Nachweis von mindestens 3 aktiv kommentierten Pull Requests pro Team-Mitglied
* Kurze Dokumentation: Planung, Architekturskizze, Testkonzept
* Reflexion über TDD und Code Reviews
* Präsentation (10 Minuten) mit Fazit

## Referenz

- Quelle: m450-source/Unterlagen/projekt/ (Theorie + Originaltext der Aufträge: README.md, grundaufbau.md, tdd.md, ci-cd.md, code-reviews.md, auswertungen.md)
