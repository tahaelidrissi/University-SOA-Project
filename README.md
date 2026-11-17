# 🎓 University Platform — SOA

Plateforme universitaire construite selon une **architecture orientée services (SOA)** : des services métier **SOAP** (Apache CXF), des services **REST** (Spring MVC) et une **orchestration BPMN** (Camunda 7) qui enchaîne ces services pour traiter une demande d'inscription à un cours.

> Projet académique — INPT Rabat. Les données sont stockées **en mémoire** : tout est réinitialisé à chaque redémarrage.

---

## 📑 Sommaire

- [Architecture](#-architecture)
- [Stack technique](#-stack-technique)
- [Structure du projet](#-structure-du-projet)
- [Démarrage rapide](#-démarrage-rapide)
- [Points d'accès](#-points-daccès)
- [Services SOAP](#-services-soap)
- [Services REST](#-services-rest)
- [Processus BPMN d'inscription](#-processus-bpmn-dinscription)
- [Scénario de test complet](#-scénario-de-test-complet)
- [Limites connues](#-limites-connues)
- [Licence](#-licence)

---

## 🏗 Architecture

```
                   ┌──────────────────────────────┐
  Client / Postman │   Interface Web (index.html) │
         │         └──────────────┬───────────────┘
         ▼                        ▼
 ┌──────────────────────────────────────────────────────┐
 │          REST  /api/**   (Spring MVC)                │
 │  Inscriptions · Suivi académique · Notifications     │
 └───────────────┬──────────────────────────────────────┘
                 │ POST /api/inscriptions
                 ▼
 ┌──────────────────────────────────────────────────────┐
 │   Orchestration BPMN  (Camunda — processusInscription)│
 │   JavaDelegates qui appellent SOAP et REST            │
 └───────┬──────────────────────────────┬───────────────┘
         ▼                              ▼
 ┌──────────────────────┐    ┌──────────────────────────┐
 │ SOAP /services/**    │    │ REST /api/** (appels     │
 │ (Apache CXF / JAX-WS)│    │ techniques : save, suivi,│
 │ Étudiant · Enseignant│    │ notifications)           │
 │ Cours                │    └──────────────────────────┘
 └──────────────────────┘
```

| Couche | Rôle |
|---|---|
| **SOAP** | Services référentiels : étudiants, enseignants, cours (CRUD + vérifications). |
| **REST** | Services transactionnels : inscriptions, suivi académique, notifications. |
| **BPMN** | Orchestre le processus d'inscription en appelant les services SOAP et REST. |
| **Web** | Page HTML statique pour tester les endpoints REST depuis le navigateur. |

Tous les services tournent dans **une seule application Spring Boot** (port `8080`) ; ils communiquent néanmoins entre eux par HTTP (SOAP/REST), comme des services distincts.

---

## 🧰 Stack technique

| Composant | Version |
|---|---|
| Java | 11+ (image Docker : 17) |
| Spring Boot | 2.7.14 |
| Apache CXF (JAX-WS) | 3.5.5 |
| Camunda BPM | 7.19.0 |
| springdoc-openapi | 1.7.0 |
| Base H2 (moteur Camunda) | en mémoire |
| Maven | 3.6+ |

---

## 📂 Structure du projet

```
.
├── docker/
│   └── Dockerfile                  # build multi-étapes (Maven → JRE)
├── src/main/java/com/university/
│   ├── UniversityPlatformApplication.java
│   ├── config/WebServiceConfig.java    # publication des endpoints SOAP
│   ├── soap/                           # interfaces SEI + implémentations (impl/)
│   ├── rest/
│   │   ├── controller/                 # contrôleurs REST
│   │   ├── service/                    # logique métier REST
│   │   └── dto/                        # objets requête / réponse
│   ├── bpmn/                           # JavaDelegates du processus Camunda
│   └── model/                          # entités (Etudiant, Cours, Inscription…)
├── src/main/resources/
│   ├── application.properties
│   ├── processes/processus-inscription.bpmn
│   └── static/index.html               # interface de test
├── .dockerignore
├── pom.xml
└── README.md
```

---

## 🚀 Démarrage rapide

### Option 1 — Maven

```bash
mvn clean package
mvn spring-boot:run
```

### Option 2 — Docker (build local)

À lancer depuis la racine du projet :

```bash
docker build -t university-platform-soa -f docker/Dockerfile .
docker run --rm -p 8080:8080 university-platform-soa
```

### Option 3 — Image publique Docker Hub

```bash
docker pull tahaidrissi05/university-platform-soa:1.0
docker run --rm -p 8080:8080 tahaidrissi05/university-platform-soa:1.0
```

### Configuration

Les identifiants de l'admin Camunda peuvent être surchargés par variables d'environnement :

| Variable | Défaut |
|---|---|
| `CAMUNDA_ADMIN_USER` | `admin` |
| `CAMUNDA_ADMIN_PASSWORD` | `admin123` |

```bash
docker run --rm -p 8080:8080 -e CAMUNDA_ADMIN_PASSWORD=monMotDePasse university-platform-soa
```

> ⚠️ `admin / admin123` sont des identifiants de démonstration : changez-les avant toute exposition publique.

---

## 🌐 Points d'accès

| Ressource | URL |
|---|---|
| Interface web de test | http://localhost:8080/ |
| Liste des services SOAP | http://localhost:8080/services |
| Swagger UI (REST) | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Camunda (Cockpit / Tasklist) | http://localhost:8080/camunda/ — `admin` / `admin123` |
| API REST Camunda | http://localhost:8080/engine-rest |

---

## 🧼 Services SOAP

Namespace : `http://soap.university.com/` — WSDL : `http://localhost:8080/services/<Service>?wsdl`

### EtudiantService — `/services/EtudiantService`

| Opération | Paramètres | Retour |
|---|---|---|
| `creerEtudiant` | nom, prenom, matricule, filiere, annee, email | `Etudiant` |
| `getEtudiant` | matricule | `Etudiant` |
| `modifierEtudiant` | matricule, nom, prenom, filiere, annee, email | `Etudiant` |
| `supprimerEtudiant` | matricule | `boolean` |
| `listerEtudiants` | — | `List<Etudiant>` |
| `verifierExistence` | matricule | `boolean` |

### EnseignantService — `/services/EnseignantService`

| Opération | Paramètres | Retour |
|---|---|---|
| `ajouterEnseignant` | nom, prenom, specialite, email | `Enseignant` (disponible par défaut) |
| `getEnseignant` | id | `Enseignant` |
| `modifierEnseignant` | id, nom, prenom, specialite, disponible, email | `Enseignant` |
| `supprimerEnseignant` | id | `boolean` |
| `listerEnseignants` | — | `List<Enseignant>` |
| `verifierDisponibilite` | id | `boolean` |

### CoursService — `/services/CoursService`

| Opération | Paramètres | Retour |
|---|---|---|
| `creerCours` | titre, description, enseignantId, credits, horaires, capaciteMax | `Cours` |
| `getCours` | id | `Cours` |
| `modifierCours` | id, titre, description, enseignantId, credits, horaires, capaciteMax | `Cours` |
| `supprimerCours` | id | `boolean` |
| `listerCours` | — | `List<Cours>` |
| `verifierDisponibiliteCours` | id | `boolean` (disponible **et** places restantes) |
| `incrementerNombreInscrits` | id | `boolean` |
| `decrementerNombreInscrits` | id | `boolean` |

Exemple de requête (création d'un étudiant) :

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:soap="http://soap.university.com/">
  <soapenv:Body>
    <soap:creerEtudiant>
      <nom>Alami</nom>
      <prenom>Sara</prenom>
      <matricule>E2025001</matricule>
      <filiere>ASEDS</filiere>
      <annee>2</annee>
      <email>sara.alami@example.com</email>
    </soap:creerEtudiant>
  </soapenv:Body>
</soapenv:Envelope>
```

---

## 🔗 Services REST

La documentation interactive est disponible sur **Swagger UI**.

### Inscriptions — `/api/inscriptions`

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/inscriptions` | **Démarre le processus BPMN** d'inscription → `202 Accepted` + `processInstanceId` |
| `POST` | `/api/inscriptions/save` | Enregistrement technique (appelé par le BPMN, pas par le client) |
| `GET` | `/api/inscriptions` | Liste des inscriptions |
| `GET` | `/api/inscriptions/{id}` | Détail d'une inscription |
| `GET` | `/api/inscriptions/etudiant/{matricule}` | Inscriptions d'un étudiant |
| `DELETE` | `/api/inscriptions/{id}` | Annulation (statut `ANNULEE`) |

```json
POST /api/inscriptions
{ "matriculeEtudiant": "E2025001", "coursId": 1 }
```

### Suivi académique — `/api/suivi`

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/suivi/{matricule}` | Suivi complet (créé automatiquement s'il n'existe pas) |
| `POST` | `/api/suivi/{matricule}/init` | Initialise le suivi |
| `PUT` | `/api/suivi/{matricule}` | Ajoute / met à jour un cours dans l'historique |
| `GET` | `/api/suivi/{matricule}/cours` | Historique des cours |
| `GET` | `/api/suivi/{matricule}/notes` | Notes et moyenne |
| `GET` | `/api/suivi/{matricule}/attestations` | Attestations des cours réussis |

```json
PUT /api/suivi/E2025001
{ "coursId": 1, "titreCours": "Architecture SOA", "note": 15.5, "credits": 4 }
```

Règle de résultat : `note ≥ 10` → `REUSSI`, `0 ≤ note < 10` → `ECHOUE`, `note < 0` → `EN_COURS`.

### Notifications — `/api/notifications`

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/notifications/email` | Envoi d'un e-mail (simulé) |
| `POST` | `/api/notifications/sms` | Envoi d'un SMS (simulé) |
| `GET` | `/api/notifications/historique/{userId}` | Historique des notifications |

```json
POST /api/notifications/email
{ "destinataire": "sara.alami@example.com", "sujet": "Test", "message": "Bonjour" }
```

---

## 🔄 Processus BPMN d'inscription

Fichier : `src/main/resources/processes/processus-inscription.bpmn` — clé : `processusInscription`

```
Demande d'inscription
  → Vérifier l'étudiant      (SOAP EtudiantService.verifierExistence)
      ✗ → Notifier le rejet → Fin (rejet)
  → Vérifier le cours        (SOAP CoursService.verifierDisponibiliteCours)
      ✗ → Notifier le rejet → Fin (rejet)
  → Vérifier l'enseignant    (SOAP EnseignantService.verifierDisponibilite)
      ✗ → Notifier le rejet → Fin (rejet)
  → Créer l'inscription      (REST POST /api/inscriptions/save)
  → Incrémenter les inscrits (SOAP CoursService.incrementerNombreInscrits)
  → Envoyer la confirmation  (REST POST /api/notifications/email)
  → Mettre à jour le suivi   (REST PUT /api/suivi/{matricule})
  → Fin (succès)
```

| Variable de processus | Source |
|---|---|
| `matriculeEtudiant`, `coursId` | requête initiale |
| `etudiantValide`, `nomEtudiant`, `emailEtudiant` | VerifierEtudiantTask |
| `coursDisponible`, `titreCours`, `enseignantId`, `creditsCours` | VerifierCoursTask |
| `enseignantDisponible`, `nomEnseignant` | VerifierEnseignantTask |
| `inscriptionId`, `inscriptionCreee` | CreerInscriptionTask |

Les instances peuvent être suivies dans **Camunda Cockpit** (`/camunda/`).

---

## 🧪 Scénario de test complet

Les données étant en mémoire, il faut **créer les référentiels via SOAP avant d'inscrire** un étudiant (SoapUI, Postman ou `curl`).

1. **Créer un enseignant** (`EnseignantService.ajouterEnseignant`) → id `1`
2. **Créer un cours** (`CoursService.creerCours`) avec `enseignantId = 1` → id `1`
3. **Créer un étudiant** (`EtudiantService.creerEtudiant`) avec matricule `E2025001`
4. **Lancer l'inscription** :
   ```bash
   curl -X POST http://localhost:8080/api/inscriptions \
        -H "Content-Type: application/json" \
        -d '{"matriculeEtudiant":"E2025001","coursId":1}'
   ```
5. **Vérifier le résultat** :
   ```bash
   curl http://localhost:8080/api/inscriptions/etudiant/E2025001
   curl http://localhost:8080/api/suivi/E2025001
   curl http://localhost:8080/api/notifications/historique/sara.alami@example.com
   ```

Exemple `curl` pour un appel SOAP :

```bash
curl -X POST http://localhost:8080/services/EnseignantService \
     -H "Content-Type: text/xml; charset=utf-8" \
     -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.university.com/">
           <soapenv:Body>
             <soap:ajouterEnseignant>
               <nom>Bennani</nom><prenom>Youssef</prenom>
               <specialite>Génie logiciel</specialite><email>y.bennani@example.com</email>
             </soap:ajouterEnseignant>
           </soapenv:Body>
         </soapenv:Envelope>'
```

---

## ⚠️ Limites connues

- **Stockage en mémoire** (`ConcurrentHashMap`) : aucune persistance des données métier ; H2 sert uniquement au moteur Camunda.
- **URLs codées en dur** (`http://localhost:8080`) dans les JavaDelegates : l'application doit tourner sur le port 8080.
- **Notifications simulées** : aucun e-mail ni SMS réel n'est envoyé.
- **Pas d'authentification** sur les API SOAP/REST et CORS ouvert (`*`) — usage pédagogique uniquement.
- **Pas de tests automatisés** pour l'instant.

---

## 📄 Licence

Distribué sous licence MIT — voir [LICENSE](LICENSE).
