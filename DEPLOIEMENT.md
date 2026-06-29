# Déploiement AWS — World Cup Predictor

Deux options sont décrites : **EC2 + RDS PostgreSQL** (recommandée, isolation propre) ou **EC2 seul** (PostgreSQL installé sur l'instance, plus simple/économique).

---

## Option A — EC2 + RDS PostgreSQL (recommandée)

### 1. Créer la base RDS

Console AWS → RDS → *Create database* :

- **Engine** : PostgreSQL 16
- **Template** : Free tier
- **DB instance identifier** : `worldcup-db`
- **Master username** : `worldcup`
- **Master password** : (à conserver, ex. `MotDePasseFort123!`)
- **DB instance class** : `db.t4g.micro` (free tier)
- **Storage** : 20 GiB gp3
- **VPC** : default
- **Public access** : *Yes* (pour simplicité du test — sinon il faut créer un peering / l'EC2 dans le même VPC)
- **VPC security group** : créer `worldcup-db-sg`
- **Initial database name** : `worldcup`

Une fois créée, noter le **endpoint** RDS : `worldcup-db.xxxxx.eu-west-3.rds.amazonaws.com`.

### 2. Créer l'instance EC2

Console AWS → EC2 → *Launch instance* :

- **Name** : `worldcup-api`
- **AMI** : Amazon Linux 2023
- **Type** : `t3.micro` (free tier)
- **Key pair** : créer/sélectionner une paire SSH
- **Network settings** :
  - Créer un security group `worldcup-api-sg`
  - Autoriser **SSH (22)** depuis votre IP
  - Autoriser **HTTP (8080)** depuis `0.0.0.0/0` (ou un port 80 si vous mettez un reverse proxy)

### 3. Connecter EC2 → RDS via security groups

Sur le SG de RDS (`worldcup-db-sg`), ajouter une règle entrante :
- **Type** : PostgreSQL (5432)
- **Source** : `worldcup-api-sg` (le SG de l'EC2)

### 4. Préparer l'instance EC2

```bash
ssh -i votre-cle.pem ec2-user@<IP_PUBLIQUE_EC2>

# Installer Docker
sudo dnf update -y
sudo dnf install -y docker git
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
exit   # se reconnecter pour appliquer le groupe
```

### 5. Construire et lancer l'application

```bash
ssh -i votre-cle.pem ec2-user@<IP_PUBLIQUE_EC2>

git clone <URL_DU_DEPOT> worldcup-predictor
cd worldcup-predictor

docker build -t worldcup-predictor:latest .

docker run -d \
  --name worldcup-api \
  --restart unless-stopped \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL='jdbc:postgresql://worldcup-db.xxxxx.eu-west-3.rds.amazonaws.com:5432/worldcup' \
  -e DB_USER='worldcup' \
  -e DB_PASSWORD='MotDePasseFort123!' \
  -e JWT_SECRET='un-secret-de-production-au-moins-64-caracteres-aleatoires-changez-moi-svp' \
  worldcup-predictor:latest

docker logs -f worldcup-api
```

### 6. Tester depuis l'extérieur

```bash
curl -X POST http://<IP_PUBLIQUE_EC2>:8080/api/utilisateurs/inscription \
  -H "Content-Type: application/json" \
  -d '{"pseudo":"demo","email":"demo@test.com","motDePasse":"secret123"}'
```

L'API est accessible à `http://<IP_PUBLIQUE_EC2>:8080`. La documentation Swagger est disponible à `http://<IP_PUBLIQUE_EC2>:8080/swagger-ui.html`.

---

## Option B — EC2 seul (PostgreSQL local)

Plus simple, plus économique, mais base et application sur la même machine.

### 1. Créer l'EC2

Identique à l'option A, étape 2.

### 2. Installer PostgreSQL et Docker

```bash
ssh -i votre-cle.pem ec2-user@<IP_PUBLIQUE_EC2>

sudo dnf update -y
sudo dnf install -y docker git postgresql15-server
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user

# Init Postgres
sudo postgresql-setup --initdb
sudo systemctl enable --now postgresql

# Créer base et utilisateur
sudo -u postgres psql <<SQL
CREATE USER worldcup WITH PASSWORD 'MotDePasseFort123!';
CREATE DATABASE worldcup OWNER worldcup;
SQL

# Autoriser les connexions locales par mot de passe
sudo sed -i 's/^host.*all.*all.*127.0.0.1.*ident/host all all 127.0.0.1\/32 md5/' /var/lib/pgsql/data/pg_hba.conf
sudo systemctl restart postgresql

exit  # se reconnecter pour appliquer le groupe docker
```

### 3. Construire et lancer l'application

```bash
git clone <URL_DU_DEPOT> worldcup-predictor
cd worldcup-predictor
docker build -t worldcup-predictor:latest .

docker run -d \
  --name worldcup-api \
  --restart unless-stopped \
  --network host \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL='jdbc:postgresql://127.0.0.1:5432/worldcup' \
  -e DB_USER='worldcup' \
  -e DB_PASSWORD='MotDePasseFort123!' \
  -e JWT_SECRET='un-secret-de-production-au-moins-64-caracteres-aleatoires-changez-moi-svp' \
  worldcup-predictor:latest
```

---

## Promouvoir un utilisateur en ADMIN

L'endpoint `/api/utilisateurs/inscription` crée un compte avec le rôle `USER`. Pour qu'un compte puisse valider les scores et créer des matchs, il faut le promouvoir en `ADMIN` directement en base :

```sql
UPDATE utilisateurs SET role = 'ADMIN' WHERE pseudo = 'votre-pseudo';
```

(En profil `dev` un compte `admin / admin123` est créé automatiquement via `DataInitializer`.)

---

## Variables d'environnement utiles

| Variable | Défaut | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` | `dev` (H2) ou `prod` (PostgreSQL) |
| `DB_URL` | `jdbc:postgresql://localhost:5432/worldcup` | URL JDBC PostgreSQL |
| `DB_USER` | `worldcup` | Utilisateur DB |
| `DB_PASSWORD` | `worldcup` | Mot de passe DB |
| `JWT_SECRET` | (clé par défaut) | **À changer en prod** — au moins 64 caractères |
| `JWT_EXPIRATION_MS` | `86400000` | Durée de vie d'un token (24h par défaut) |
| `SERVER_PORT` | `8080` | Port HTTP |
