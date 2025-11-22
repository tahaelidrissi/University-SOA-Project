# 🎓 University Platform SOA

Plateforme universitaire en ligne basée sur une architecture orientée services (SOA).

## 📋 Description

Système d'information intégré permettant la gestion des étudiants, des cours et des inscriptions.

## 🚀 Démarrage Rapide

### Prérequis
- Java 11+
- Maven 3.6+

### Installation

```bash
mvn clean install
mvn spring-boot:run
```

### Accès
- Services SOAP: http://localhost:8080/services/
- Services REST: http://localhost:8080/api/
- Interface Web: http://localhost:8080/index
- Camunda: http://localhost:8080/camunda/ (admin/admin123)

### Docker

```bash
docker build -t university-platform-soa -f docker/Dockerfile .
```


```powershell
docker run --rm -p 8080:8080 university-platform-soa
```
- **Utiliser l'image publique**: l'image a été poussée sur Docker Hub sous le tag `tahaidrissi05/university-platform-soa:1.0`. Vous pouvez la récupérer et la lancer directement sans la reconstruire :

```powershell
docker pull tahaidrissi05/university-platform-soa:1.0
docker run --rm -p 8080:8080 tahaidrissi05/university-platform-soa:1.0
```
```
