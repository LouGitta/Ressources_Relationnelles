# 🌟 Ressources Relationnelles

Projet collaboratif de plateforme d'échange de ressources relationnelles, structuré avec un backend **Spring Boot** et une application mobile **Ionic / Angular**.

---

## 🛠️ Architecture du Projet

Le projet est divisé en deux parties principales :
1. **[RessourceRelationnelles](file:///c:/Users/alexi/Desktop/CESI/BACHELOR_CDA/PROJET_COLLABORATIF/Ressources_Relationnelles/RessourceRelationnelles)** (Backend Java / Spring Boot 3) : API REST robuste, contrôles d'accès métiers et base de données relationnelle.
2. **[MobileApp/RessourcesRelationnelles.MobilesApp](file:///c:/Users/alexi/Desktop/CESI/BACHELOR_CDA/PROJET_COLLABORATIF/Ressources_Relationnelles/MobileApp/RessourcesRelationnelles.MobilesApp)** (Frontend Mobile Ionic / Angular) : Application mobile hybride.

---

## 🚀 Guide de Démarrage Rapide

### 🔙 Backend (Spring Boot)

#### Prérequis
- Java 17+
- Maven 3.6+
- Base de données H2 (par défaut pour les tests) ou MySQL (pour le profil de production/développement)

#### Commandes utiles
- **Lancer l'application** :
  ```bash
  cd RessourceRelationnelles
  .\mvnw.cmd spring-boot:run
  ```
- **Lancer tous les tests (Intégration + Unitaires)** :
  ```bash
  cd RessourceRelationnelles
  .\mvnw.cmd test
  ```

### 🎨 Frontend Mobile (Ionic / Angular)

#### Prérequis
- Node.js 18+
- Ionic CLI (`npm install -g @ionic/cli`)

#### Commandes utiles
- **Installer les dépendances** :
  ```bash
  cd MobileApp/RessourcesRelationnelles.MobilesApp
  npm install
  ```
- **Démarrer en mode développement** :
  ```bash
  ionic serve
  ```

---

## 💎 Travail de Refactoring & Clean Code (Phase 1-4)

### 1. Robustesse du Backend (SOLID & Clean Code)
- **Constantes Centralisées** : Création de `AppConstants` et `Routes` pour éviter les valeurs codées en dur.
- **DTOs & JSR-303 Validation** : Toutes les entrées/sorties de l'API REST utilisent désormais des DTOs validés (ex : `UserDTO`, `RessourceDTO`, `CommentDTO`). Les entités JPA ne sont plus exposées directement.
- **Gestion des Exceptions** : Mise en place d'un `@ControllerAdvice` global pour standardiser le format de retour des erreurs HTTP (`ApiExceptionResponse`).
- **Permissions centralisées** : Création d'un `PermissionService` pour encapsuler et sécuriser les vérifications de droits (ex : propriétaire vs modérateur vs administrateur).

### 2. Frontend TypeScript Strict
- **Modèles Robustes** : Création d'interfaces TypeScript complètes pour tous les DTOs backend (`user.model.ts`, `ressource.model.ts`, etc.) supprimant ainsi l'utilisation du type `any`.
- **Session & AuthService** : Gestion centralisée de la connexion utilisateur et des rôles.
- **Intercepteurs & Guards** :
  - `authInterceptor` : Ajoute automatiquement le jeton Bearer aux requêtes HTTP sortantes.
  - `errorInterceptor` : Attrape les erreurs globales (ex: 401/403) pour déconnecter proprement l'utilisateur.
  - `authGuard` & `roleGuard` : Sécurisation des routes Angular standalone.

### 3. Tests & Couverture
- **Tests d'Intégration** : 19 tests Spring Boot s'exécutant sur une base de données H2 en mémoire avec chargement initial via `data.sql`.
- **Tests Unitaires isolés (Mockito)** : 19 nouveaux tests unitaires isolés couvrant les services métier (`UserServiceTest`, `RessourceServiceTest`, `CommentServiceTest`, `PermissionServiceTest`).
- **Tests Frontend** : Mise à jour des fichiers `.spec.ts` pour supporter le typage HttpClient et les mocks de tests.
