# CI/CD Pipeline (Build/Test/Deploy, Reports)

## Lernziele

- Sie verstehen das Konzept der Pipeline mit Stages und Jobs
- Sie können in Ihrem Projekt die einzelnen Schritte einer Pipeline erstellen und konfigurieren
- Sie können Ihr Projekt automatisiert builden, testen und deployen

## Zusammenfassung der Theorie

- Eine **Pipeline** besteht aus **Stages** (z.B. build, test, deploy), die wiederum aus einem oder mehreren **Jobs** bestehen; ein **Runner** führt die Jobs aus.
- GitLab-Pipelines werden über `.gitlab-ci.yml` im Hauptverzeichnis des Repos definiert (GitHub Actions funktioniert nach ähnlichem Prinzip).
- Mit `image` legt man das Docker-Image des Runners fest (z.B. `maven:latest`), mit `variables` konfiguriert man Build-Tool-Optionen (z.B. Maven CLI/Repo-Pfad).
- Im `script`-Block eines Jobs werden die eigentlichen Befehle ausgeführt (z.B. `mvn test`).
- Mit `artifacts` (inkl. `reports: junit: ...`) werden Testresultate/Reports als Pipeline-Artefakte abgelegt und sichtbar gemacht.
- **GitLab Pages** kann genutzt werden, um generierte Reports (z.B. `mvn site`) als Webseite bereitzustellen.

## Aufgaben (aus UEBUNGEN.md)

Arbeiten Sie zu zweit an diesen Aufgaben. Lösungen im Repository ablegen und der Lehrperson zeigen.

Basis: [Frontend sowie Backend](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/projects/recipe-planner-fronend-and-backend.zip) in Betrieb nehmen. Nur Änderungen im Backend sind für die folgenden Aufgaben nötig, das Frontend dient nur dem Verständnis (wird in einer späteren Aufgabe erweitert).

- [ ] **Aufgabe 1 - Unit Testing**
  - [ ] Alle Controller-Methoden via MockMvc, RestTemplate, REST-assured oder einer alternativen Methode testen
  - [ ] Die Mapper-Klasse für die zwei vorhandenen Domänen-Klassen testen und dabei [SoftAssertions](https://joel-costigliola.github.io/assertj/core/api/org/assertj/core/api/SoftAssertions.html) verwenden
  - [ ] Sich bewusst sein/erklären können, was der Vorteil von SoftAssertions ist
- [ ] **Aufgabe 2 - Reports**
  - [ ] Sichtbare, automatisiert erstellte Reports für die Unit Tests einrichten
  - [ ] Dafür z.B. [Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/), [JaCoCo](https://github.com/jacoco/jacoco) oder eine Alternative verwenden
- [ ] **Aufgabe 3 - Pipeline**
  - [ ] Eine Build-Pipeline wie in der [Theorie](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/ci-cd-pipeline/README.md) beschrieben aufsetzen (GitLab, GitHub oder Alternative)
  - [ ] Sicherstellen, dass ein Commit/Push die Pipeline und damit die Unit Tests triggert
  - [ ] Pro Pipeline-Durchlauf einen einsehbaren Report generieren

## Deliverables

- Backend-Projekt mit Unit-Tests für alle Controller-Methoden (MockMvc/RestTemplate/REST-assured o.ä.)
- Unit-Tests für die Mapper-Klasse mit SoftAssertions, inkl. kurzer Begründung des Vorteils von SoftAssertions
- Konfiguration für automatisierte Testreports (Surefire/JaCoCo o.ä.)
- CI/CD-Konfigurationsfile (z.B. `.gitlab-ci.yml`) mit Stages build/test/deploy
- Nachweis/Screenshot, dass Push die Pipeline triggert und ein Report pro Durchlauf einsehbar ist

## Referenz

- Quelle: m450-source/Unterlagen/ci-cd-pipeline/ (theory + original exercise text)
