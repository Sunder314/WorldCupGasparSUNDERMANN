# World Cup Predictor

API REST Spring Boot pour pronostiquer la Coupe du Monde : matchs, paris, ligues privées et classements.

## Stack
- **Java 17** / Spring Boot 3.3
- **Spring Security + JWT** (HS512)
- **JPA / Hibernate**, **H2** (dev) / **PostgreSQL** (prod)
- **Maven**, **Docker**, **Swagger UI**

## Démarrage local (profil `dev`, H2 en mémoire)

```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`.

- Swagger UI : `http://localhost:8080/swagger-ui.html`
- Console H2 : `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:worldcup`, user `sa`, mot de passe vide)

Un compte admin est créé automatiquement au démarrage en dev :
- **pseudo** `admin` / **mot de passe** `admin123`

Trois matchs de démo sont également insérés.

## Endpoints

| Méthode | URL | Auth | Description |
|---|---|---|---|
| POST | `/api/utilisateurs/inscription` | public | Inscription d'un utilisateur |
| POST | `/api/auth/login` | public | Connexion → renvoie le JWT |
| GET | `/api/matchs` | USER | Liste des matchs (filtre `?statut=A_VENIR\|EN_COURS\|TERMINE`) |
| POST | `/api/matchs` | ADMIN | Création d'un match (bonus) |
| PUT | `/api/matchs/{id}/score` | ADMIN | Validation du score final + calcul automatique des points |
| POST | `/api/pronostics` | USER | Enregistre/modifie un pronostic (verrouillé au coup d'envoi) |
| GET | `/api/pronostics/mes-pronostics` | USER | Historique des paris du joueur connecté |
| POST | `/api/ligues` | USER | Crée une ligue privée (code à 8 caractères généré) |
| POST | `/api/ligues/rejoindre` | USER | Rejoindre une ligue via son code |
| GET | `/api/ligues/mes-ligues` | USER | Ligues du joueur connecté |
| GET | `/api/classements/general` | USER | Classement mondial trié par points |
| GET | `/api/classements/ligue/{id}` | USER | Classement filtré sur les membres d'une ligue |

L'authentification se fait via header `Authorization: Bearer <token>`.

## Règle de calcul des points

À chaque `PUT /api/matchs/{id}/score`, tous les pronostics du match sont rejoués :
- **Score exact** (ex. prono 2-1, résultat 2-1) → **150 points** (100 + 50 bonus)
- **Bonne issue** (victoire A / nul / victoire B) → **100 points**
- **Mauvaise issue** → **0 point**

Le total est ajouté au compteur `points` du joueur. Si l'admin réenregistre un score (correction), les points sont d'abord retirés puis recalculés.

## Architecture

```
src/main/java/com/fifa/worldcuppredictor/
├── WorldCupPredictorApplication.java
├── config/         DataInitializer (admin + matchs de démo en dev)
├── controller/     AuthController, UtilisateurController, MatchController,
│                   PronosticController, LigueController, ClassementController
├── dto/            Records DTO (auth, match, pronostic, ligue, classement)
├── entity/         Utilisateur, MatchRencontre, Pronostic, Ligue + enums
├── exception/      ApiException + GlobalExceptionHandler
├── repository/     UtilisateurRepository, MatchRepository, PronosticRepository, LigueRepository
├── security/       JwtService, JwtAuthenticationFilter, CustomUserDetailsService,
│                   UserPrincipal, SecurityConfig
└── service/        AuthService, MatchService, PronosticService, LigueService,
                    ClassementService, PointsService
```

Choix d'architecture :
- **DTOs en `record` Java** : immuables, concis, pas de boilerplate
- **Stateless** (JWT, pas de session)
- **Profils Spring** : `dev` (H2 + données de démo) / `prod` (PostgreSQL)
- **Moteur de points isolé** dans `PointsService` (testable, réutilisable)
- **Idempotence** de la validation de score : la re-validation soustrait les anciens points avant d'appliquer les nouveaux

## Déploiement AWS

Voir [DEPLOIEMENT.md](./DEPLOIEMENT.md) — deux options décrites :
- **EC2 + RDS PostgreSQL** (recommandé)
- **EC2 seul** avec PostgreSQL local

## Tester avec curl

```bash
# Inscription
curl -X POST http://localhost:8080/api/utilisateurs/inscription \
  -H "Content-Type: application/json" \
  -d '{"pseudo":"bob","email":"bob@test.com","motDePasse":"secret123"}'

# Connexion
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"pseudo":"bob","motDePasse":"secret123"}' | jq -r .token)

# Pronostiquer sur le match 1
curl -X POST http://localhost:8080/api/pronostics \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"matchId":1,"scorePredA":2,"scorePredB":1}'
```
# WorldCupGasparSUNDERMANN
