# 04 Test Levels — Antworten

## Aufgabe 1: Testing im eigenen Praktikumsbetrieb

Erfahrung aus einem internen Projekt während eines Praktikums.

- **Test Levels**: Unit- und Integrationstests mit JUnit, End-to-End-Tests zunächst mit
  Cypress, später migriert auf Playwright. Die Konventionen dazu waren in einem
  Guidelines-Markdown-Dokument im Repository festgehalten.
- **Zeitpunkt der Testausführung**: bei jedem Branch/Push (lokal vor dem Push sowie in der
  Pipeline), sowie beim Erstellen und Mergen von Pull Requests. Zusätzlich erneut in der
  Staging-Umgebung nach dem Merge, und nochmals beim Release-Merge nach `main`.
- **Dediziertes Testing-/QA-Team**: nein, es gab kein separates QA-Team — die Entwickler
  haben ihre Tests selbst geschrieben und ausgeführt.
- **Testing Life Cycle**:
  1. Code lokal entwickeln, Tests lokal ausführen (grün vor dem Push)
  2. Push -> Pipeline führt Tests in einem Docker-Container aus
  3. Bei Erfolg: Pull Request erstellen -> Tests laufen erneut -> bei Erfolg Merge
  4. Nach Merge: Tests laufen nochmals in der Staging-Umgebung
  5. Bei einem Release: Merge nach `main`, wo derselbe Testzyklus nochmals durchlaufen wird

## Aufgabe 2: Testing Approach, Levels, Techniques — Zusammenhang

**Testing Approach** ist die strategische Grundsatzentscheidung, die zu Beginn eines Projekts
getroffen wird und von den Projektanforderungen abhängt — "depends on the project". Braucht
ein Projekt hohe Qualitätssicherung (z. B. sicherheitskritische Software), wählt man einen
anspruchsvolleren Ansatz wie TDD oder risikobasiertes Testen (Fokus auf die Bereiche mit dem
grössten potenziellen Schaden). Reicht ein einfacher Proof-of-Concept, genügt ein
schlankerer, automatisierter Ansatz mit weniger Aufwand. Der Approach bestimmt also, wie viel
und wie strukturiert im weiteren Verlauf getestet wird.

**Testing Levels** bilden die strukturelle "Leiter", auf der man sich durch den
Entwicklungsprozess nach oben arbeitet: Unit-Tests unten (klein, günstig, schnell, einzelne
Methoden), dann Component-, Integration-, System-Tests, bis hin zu Acceptance-Tests oben
(gross, teuer, das gesamte System aus Anwendersicht). Je weiter oben, desto komplexer und
umfangreicher die Testsuiten — man "klettert die Leiter hoch", bis am Ende ein vollständiges
Geflecht aus vielen Testsuiten über alle Ebenen hinweg entsteht.

**Testing Types/Techniques/Tactics** sind die konkreten, handwerklichen Methoden, die
innerhalb eines Levels angewendet werden, um tatsächlich Testfälle zu bauen — z. B.
Äquivalenzklassenbildung und Grenzwertanalyse (wie in Block 02 bei den
abstrakten/konkreten Testfall-Tabellen verwendet), Black-Box vs. White-Box, Lasttests,
explorative Tests oder Regressionstests.

**Abhängigkeit**: Der Approach wird zuerst festgelegt (strategisch, projektgetrieben) → er
bestimmt, wie die Levels angewendet werden (die strukturelle Leiter von Unit bis Acceptance)
→ innerhalb jedes Levels werden konkrete Techniques gewählt, um die eigentlichen Testfälle zu
erstellen.
