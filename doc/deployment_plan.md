# Plan de Deploiement : Ressources Relationnelles

Ce guide decrit pas a pas comment installer Docker et deployer l'application Ressources Relationnelles sur une machine virtuelle (VM) vierge, typiquement sous Ubuntu 22.04 LTS ou Ubuntu 24.04 LTS.

---

## Etape 1 : Preparation de la VM et Installation de Docker

Connectez-vous a votre VM par SSH, puis executez les commandes suivantes pour installer Docker et Docker Compose.

### 1. Mettre a jour le systeme
```bash
sudo apt update && sudo apt upgrade -y
```

### 2. Installer Docker via le script officiel de commodite
C'est la methode la plus rapide et fiable pour une VM toute fraiche :
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
```

### 3. Configurer les permissions utilisateur (Optionnel mais recommande)
Pour eviter de devoir taper sudo devant chaque commande docker :
```bash
sudo usermod -aG docker $USER
```
> [!IMPORTANT]
> Pour que ce changement de groupe soit pris en compte, vous devez vous deconnecter et vous reconnecter a votre session SSH :
> ```bash
> exit
> # puis reconnectez-vous a la VM
> ```

### 4. Verifier l'installation
Verifiez que Docker et Docker Compose fonctionnent :
```bash
docker --version
docker compose version
```

---

## Etape 2 : Preparation des fichiers sur la VM

Nous allons creer la structure necessaire et y copier les fichiers indispensables.

### 1. Creer les dossiers de l'application
Sur votre VM, creez un repertoire pour le projet :
```bash
mkdir -p ~/ressources_relationnelles/BDD
```

### 2. Copier les fichiers de configuration vers la VM (via SCP)
Depuis votre machine locale, ouvrez un terminal a la racine du projet et executez les commandes suivantes pour copier les fichiers (remplacez `user` et `IP_VM` par vos identifiants de VM) :

```bash
# 1. Copier le script SQL d'initialisation de la BDD
scp BDD/init_bdd.sql user@IP_VM:~/ressources_relationnelles/BDD/init_bdd.sql

# 2. Copier le fichier docker-compose
scp tools/docker-compose.yml user@IP_VM:~/ressources_relationnelles/docker-compose.yml

# 3. Copier le fichier .env (a partir du modele)
scp tools/.env.example user@IP_VM:~/ressources_relationnelles/.env
```

> [!NOTE]
> Une fois les fichiers copies, connectez-vous a la VM et editez le fichier `.env` pour y inserer vos propres mots de passe de production :
> ```bash
> nano ~/ressources_relationnelles/.env
> ```

---

## Etape 3 : Lancement de l'application

### 1. Connexion au GitHub Container Registry (GHCR)
Si le package Docker sur GitHub est configure comme prive, vous devez vous authentifier a l'aide d'un Personal Access Token (PAT) GitHub contenant la portee read:packages :
```bash
docker login ghcr.io -u <VOTRE_PSEUDO_GITHUB>
```
*Saisissez votre Personal Access Token lorsque le mot de passe est demande.*

### 2. Demarrer les conteneurs
Rendez-vous dans le repertoire de l'application sur la VM et lancez Docker Compose en arriere-plan :
```bash
cd ~/ressources_relationnelles
docker compose up -d
```

---

## Etape 4 : Verification et Diagnostics

### 1. Verifier l'etat des conteneurs
Assurez-vous que les conteneurs tournent correctement :
```bash
docker compose ps
```
Vous devriez voir mysql_RessourcesRelationnelles et le backend actifs avec le statut Up (et healthy pour la base de donnees).

### 2. Consulter les logs
En cas de probleme ou pour suivre l'initialisation de l'application :
```bash
docker compose logs -f
```

### 3. Tester l'acces a l'API
Depuis votre machine locale ou votre navigateur, vous devriez pouvoir acceder a la page d'accueil de la partie web (Thymeleaf) a l'adresse suivante (le port 80 etant le port par defaut, vous n'avez pas besoin de le preciser dans l'URL) :
`http://<IP_DE_VOTRE_VM>/app/home`

---

## Resolution des Problemes Courants (Troubleshooting)

| Probleme | Cause Possible | Solution |
| :--- | :--- | :--- |
| **Erreur de connexion BDD** | MySQL n'a pas fini de demarrer | Spring Boot attend que le healthcheck de MySQL soit valide avant de demarrer. Attendez 30 secondes et verifiez a nouveau. |
| **Port 80 deja utilise** | Un autre service utilise le port 80 sur la VM | Modifiez la ligne "- 80:8080" dans le fichier docker-compose.yml par un autre port (ex: "- 8080:8080"). |
| **Permission Denied** | Probleme de droits d'acces Docker | N'oubliez pas de lancer avec sudo devant la commande docker compose si vous n'avez pas configure le groupe docker a l'Etape 1.3. |
