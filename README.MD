# Projet de Gestion Médicale - Microservices

## Services disponibles

### 1. Patient Service (Port: 8001)
Endpoints :
- `GET /patients` - Liste tous les patients
- `GET /patients/{id}` - Récupère un patient par ID
- `GET /patients/email/{email}` - Récupère un patient par email
- `POST /patients` - Crée un nouveau patient
- `PUT /patients/{id}` - Met à jour un patient
- `DELETE /patients/{id}` - Supprime un patient

### 2. Practitioner Service (Port: 8002)
Endpoints :
- `GET /practitioners` - Liste tous les praticiens
- `GET /practitioners/{id}` - Récupère un praticien par ID
- `GET /practitioners/email/{email}` - Récupère un praticien par email
- `POST /practitioners` - Crée un nouveau praticien
- `PUT /practitioners/{id}` - Met à jour un praticien
- `DELETE /practitioners/{id}` - Supprime un praticien

### 3. Appointment Service (Port: 8003)
Endpoints :
- `GET /appointments` - Liste tous les rendez-vous
- `GET /appointments/{id}` - Récupère un rendez-vous par ID
- `GET /appointments/patient/{email}` - Liste les rendez-vous d'un patient
- `GET /appointments/practitioner/{email}` - Liste les rendez-vous d'un praticien
- `POST /appointments` - Crée un nouveau rendez-vous
- `PUT /appointments/{id}` - Met à jour un rendez-vous
- `DELETE /appointments/{id}` - Supprime un rendez-vous

### 4. Medical Record Service (Port: 8004)
Endpoints :
- `GET /medical-records` - Liste tous les dossiers médicaux
- `GET /medical-records/{id}` - Récupère un dossier médical par ID
- `GET /medical-records/patient/{email}` - Liste les dossiers médicaux d'un patient
- `POST /medical-records` - Crée un nouveau dossier médical
- `PUT /medical-records/{id}` - Met à jour un dossier médical
- `DELETE /medical-records/{id}` - Supprime un dossier médical

### 5. Gateway Service (Port: 8000)
Point d'entrée unique pour tous les services :
- `/patients/**` - Redirige vers Patient Service
- `/practitioners/**` - Redirige vers Practitioner Service
- `/appointments/**` - Redirige vers Appointment Service
- `/medical-records/**` - Redirige vers Medical Record Service

## Configuration
Chaque service utilise une base de données H2 en mémoire avec :
- Console H2 accessible à `/h2-console`
- URL JDBC : `jdbc:h2:mem:{service}db`
- Utilisateur : `sa`
- Mot de passe : `password`

## Fonctionnalités
- Circuit Breaker avec Resilience4j
- Gestion des erreurs avec fallbacks
- Données de test générées automatiquement
- Intégration avec Google Calendar pour les rendez-vous
- Documentation Swagger/OpenAPI disponible pour chaque service

## Démarrage
1. Lancer les services dans l'ordre :
   - Patient Service
   - Practitioner Service
   - Appointment Service
   - Medical Record Service
   - Gateway Service

2. Créer des données via les endpoints POST de chaque service

3. Accéder aux services via le Gateway (port 8005) ou directement via leurs ports respectifs