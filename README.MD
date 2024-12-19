<!-- @format -->

# Projet de Gestion Médicale - Microservices

## Description

Système de gestion médicale basé sur une architecture microservices permettant la gestion des patients, praticiens, rendez-vous et dossiers médicaux.

> **Note importante** : La documentation Swagger 3 est utilisée dans ce projet via la dépendance springdoc-openapi-starter-webmvc-ui, version 2.3.0. Swagger 3 normal est incompatible avec Java 17 / Spring Boot 3.x.

## Prérequis

- Java 17
- Maven 3.8+
- Docker et Docker Compose (optionnel)
- Au moins 8GB de RAM disponible
- Ports 8000-8004, 8761 disponibles

## Architecture des Services

### Service Discovery (Eureka Server) - Port 8761

- Service de découverte et d'enregistrement des microservices
- Dashboard : http://localhost:8761
- Dashboard Admin : http://localhost:8761/admin/dashboard

### API Gateway - Port 8000

Point d'entrée unique pour tous les services avec routes :

- `/patients/**` → Patient Service
- `/practitioners/**` → Practitioner Service
- `/appointments/**` → Appointment Service
- `/medical-records/**` → Medical Record Service

### Services Métier

1. **Patient Service (Port: 8001)**

   - Gestion des patients
   - Base H2 : `jdbc:h2:mem:patientdb`
   - Swagger : http://localhost:8001/swagger-ui/index.html

2. **Practitioner Service (Port: 8002)**

   - Gestion des praticiens
   - Base H2 : `jdbc:h2:mem:practitionerdb`
   - Swagger : http://localhost:8002/swagger-ui/index.html

3. **Appointment Service (Port: 8003)**

   - Gestion des rendez-vous
   - Base H2 : `jdbc:h2:mem:appointmentdb`
   - Swagger : http://localhost:8003/swagger-ui/index.html

4. **Medical Record Service (Port: 8004)**
   - Gestion des dossiers médicaux
   - Base H2 : `jdbc:h2:mem:medicalrecorddb`
   - Swagger : http://localhost:8004/swagger-ui/index.html

## Installation et Démarrage

### Méthode 1 : Avec Docker

1. Construire les images Docker :

```bash
docker-compose build
```

2. Démarrer les services :

```bash
docker-compose up -d
```

3. Vérifier que tous les services sont opérationnels via Eureka : http://localhost:8761

### Méthode 2 : Sans Docker

1. Démarrer les services dans l'ordre suivant :

```bash
# 1. Service Discovery (Eureka)
cd eureka-server
mvn spring-boot:run

# 2. API Gateway
cd ../gateway-service
mvn spring-boot:run

# 3. Services métier (dans des terminaux séparés)
cd ../patient-service
mvn spring-boot:run

cd ../practitioner-service
mvn spring-boot:run

cd ../appointment-service
mvn spring-boot:run

cd ../medical-record-service
mvn spring-boot:run
```

## Tests des Services

### Via Swagger UI

Chaque service dispose de sa propre interface Swagger :

- Patient Service : http://localhost:8001/swagger-ui/index.html
- Practitioner Service : http://localhost:8002/swagger-ui/index.html
- Appointment Service : http://localhost:8003/swagger-ui/index.html
- Medical Record Service : http://localhost:8004/swagger-ui/index.html

### Via l'API Gateway

Toutes les requêtes peuvent être faites via l'API Gateway (port 8000) :

```bash
# Exemples de requêtes
curl http://localhost:8000/patients
curl http://localhost:8000/practitioners
curl http://localhost:8000/appointments
curl http://localhost:8000/medical-records
```

## Bases de données

Chaque service utilise sa propre base de données H2 en mémoire. Les consoles H2 sont accessibles aux URLs :

- Patient DB : http://localhost:8001/h2-console
- Practitioner DB : http://localhost:8002/h2-console
- Appointment DB : http://localhost:8003/h2-console
- Medical Record DB : http://localhost:8004/h2-console

Configuration de connexion pour chaque console H2 :

- JDBC URL : voir la section "Services Métier"
- Username : sa
- Password : password
