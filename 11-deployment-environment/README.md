# 11 Deployment Environments (Docker Compose, Kubernetes, Vagrant, Terraform) — Antworten

## Aufgabe 1: Welche Lösung für welche Umgebung?

### Kurzrecherche

| Tool | Was es macht | Stärken | Grenzen |
|---|---|---|---|
| **Docker Compose** | Startet mehrere Container aus einer YAML-Datei auf einem Host | Ein Befehl (`docker compose up`), schnell, leichtgewichtig, läuft lokal und in CI | Nur ein Host, kein Failover, kein automatisches Skalieren |
| **Kubernetes** | Orchestriert Container über einen Cluster mit mehreren Nodes | Skalierung, Self-Healing, Rolling Updates, hält den Soll-Zustand | Komplex, hoher Betriebs- und Lernaufwand |
| **Vagrant** (HashiCorp) | Erstellt reproduzierbare VMs aus einem `Vagrantfile` | Ganze Betriebssysteme lokal, ideal für Entwicklungsumgebungen | Schwer (VM statt Container), kaum für Produktion gedacht |
| **Terraform** (HashiCorp) | Provisioniert Infrastruktur als Code (Server, Netzwerk, DB, Cluster) bei Cloud-Providern | Deklarativ, versioniert, viele Provider, Plan vor Änderung | Stellt Infrastruktur bereit, deployt selbst keine Applikation |

Vagrant vs. Terraform (gemäss HashiCorp): Vagrant ist für lokale Entwicklungsumgebungen gedacht,
Terraform für Infrastruktur bei Cloud-Providern und damit eher für Produktivumgebungen.

### Zuordnung

| Umgebung | Lösung | Begründung |
|---|---|---|
| **Development** | Docker Compose (Vagrant, wenn eine ganze VM nötig ist) | Jeder Entwickler startet mit einem Befehl denselben Stack lokal. Vagrant nur, wenn ein anderes OS, ein eigener Kernel oder mehrere Maschinen nötig sind. |
| **Testing** | Docker Compose | Schnell aufgebaut und wieder weggeworfen, jeder Testlauf startet sauber. Läuft direkt in CI-Pipelines (GitHub Actions hat Docker). |
| **Staging** | Kubernetes (+ Terraform) | Soll die Produktion möglichst genau abbilden, also dieselbe Orchestrierung und Infrastruktur wie Production, nur kleiner. |
| **Production** | Kubernetes + Terraform | Kubernetes für Skalierung, Self-Healing und Rolling Updates ohne Downtime. Terraform stellt die Infrastruktur darunter (Cluster, Netzwerk, DB) reproduzierbar bereit. |

Die Tools schliessen sich nicht aus: In Staging und Production baut Terraform die Infrastruktur auf
und Kubernetes betreibt darauf die Container. Dieselben Docker-Images laufen dabei in allen vier
Umgebungen, nur die Orchestrierung ändert sich.

## Aufgabe 2: Testing-Umgebung mit Docker Compose

**Gewählt:** Docker Compose, um eine Testing-Umgebung für den Recipe Planner (Spring-Boot-Backend
+ React-Frontend) automatisiert aufzusetzen. Backend aus Block 10, Frontend aus Block 09, kopiert
nach `recipe-planner/`.

| Datei | Inhalt |
|---|---|
| `recipe-planner/backend/Dockerfile` | Multi-Stage: `maven:3.9-eclipse-temurin-21` baut das Jar, `eclipse-temurin:21-jre` führt es aus |
| `recipe-planner/frontend/Dockerfile` | Multi-Stage: `node:20-alpine` baut die React-App (`npm ci`, `npm run build`), `nginx:alpine` liefert sie aus |
| `recipe-planner/frontend/nginx.conf` | Fallback `try_files $uri /index.html` für die React-Routen |
| `recipe-planner/*/.dockerignore` | `target`, `node_modules`, `build`, `.git` nicht in den Build-Kontext kopieren |
| `docker-compose.yml` | Services `backend` (8080:8080) und `frontend` (3000:80), Frontend startet nach Backend |

Starten und stoppen:

```
cd 11-deployment-environment
docker compose up -d --build     # http://localhost:3000 (Frontend), http://localhost:8080/api/recipes (Backend)
docker compose down
```

**Resultat:** Beide Container laufen, die API antwortet nach rund 8 Sekunden. Im Browser zeigt die
Startseite alle 15 Seed-Rezepte ohne Konsolenfehler (CORS funktioniert), und ein Reload auf
`/planer` liefert dank `nginx.conf` die App statt 404.

![Recipe Planner in Docker Compose](compose-browse.png)

| Image | Grösse |
|---|---|
| Backend (JRE + Jar) | 575 MB |
| Frontend (nginx + statische Dateien) | 98 MB |

Dank Multi-Stage-Builds enthalten die Images nur das Laufzeit-Nötige, nicht Maven, Node oder
`node_modules`.

### Stolpersteine

1. **Hart codierte API-URL.** Das Frontend ruft fix `http://localhost:8080` auf. Das funktioniert
   nur, weil der **Browser** die Requests vom Host aus schickt, nicht der Container. Auf einem echten
   Server bräuchte es eine konfigurierbare URL (Variable zur Build-Zeit) oder einen Reverse Proxy in
   nginx.
2. **CORS nur für `http://localhost:3000`.** Das Frontend muss deshalb auf Host-Port 3000
   gemappt sein. Mit einem anderen Port schlägt jeder Request fehl.
3. **H2 in-memory.** Mit `docker compose down` sind alle Daten weg. Für Testing ist das gewollt
   (jeder Lauf startet sauber), Staging und Production bräuchten eine echte Datenbank mit Volume.
4. **React-Routen.** Ohne `try_files`-Fallback liefert nginx bei einem Reload auf `/planer` oder
   `/new-menues` einen 404, weil diese Pfade nur im Browser existieren.

### Reflexion

**Zeit:** Das Schreiben der Dateien ging schnell. Der erste Build dauerte gut 3.5 Minuten (Images
herunterladen, Maven- und npm-Abhängigkeiten), weitere Builds dank Layer-Cache nur Sekunden.

**Probleme:** Die lokale Docker-VM (Colima) startete zuerst nicht, weil nach einem früheren Absturz
noch eine Sperre auf ihrer Disk lag. Beim ersten Build brach ein Image-Download von Docker Hub mit
einem Netzwerkfehler (`EOF`) ab, der zweite Versuch lief durch. Beides sind Probleme der Umgebung,
nicht der Konfiguration. Die vier Stolpersteine oben waren absehbar, weil sie im Code stehen.

**Fazit:** Docker Compose lohnt sich überall, wo ein ganzer Stack schnell und identisch auf einem
einzelnen Host laufen soll: lokale Entwicklung, Testing-Umgebungen, Smoke-Tests in der CI-Pipeline
und kleine Deployments. Es ersetzt die Anleitung "installiere Java 21, Node 20 und starte zwei
Terminals" durch einen Befehl. Sobald mehrere Server, automatisches Skalieren oder Updates ohne
Downtime nötig sind, reicht Compose nicht mehr. Dann ist Kubernetes die passende Wahl, mit
Terraform für die Infrastruktur.

## Aufgabe 3 (Challenge): Vollautomatisches Setup in der Pipeline

Datei: [`.github/workflows/deploy-env.yml`](../.github/workflows/deploy-env.yml)

Jeder Push, der `11-deployment-environment/**` ändert, baut und testet die Testing-Umgebung
automatisch auf einem frischen GitHub-Runner:

1. `docker compose up -d --build` baut beide Images und startet die Umgebung.
2. Warten, bis Backend (`/api/recipes`) und Frontend antworten (maximal 150 Sekunden).
3. Smoke-Test: Die API liefert Rezepte, und `/planer` liefert `index.html` (prüft den nginx-Fallback).
4. Bei einem Fehler werden die Container-Logs ausgegeben, am Schluss immer `docker compose down`.

Ein eigener Workflow statt eines weiteren Jobs in `ci.yml` aus Block 10: So löst eine Änderung in
Block 11 nicht unnötig auch Build, Test und Deploy von Block 10 aus.
