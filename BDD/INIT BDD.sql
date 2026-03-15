CREATE DATABASE ressources_relationnelles;
USE ressources_relationnelles;

CREATE TABLE `User` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255) UNIQUE NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` ENUM ('citizen', 'moderator', 'administrator', 'super_admin'),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` bool DEFAULT true
);

CREATE TABLE `Friend` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_1` integer NOT NULL,
  `user_2` integer NOT NULL,
  `status` ENUM ('pending', 'accepted', 'rejected') DEFAULT 'pending',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Ressource` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `title` varchar(255),
  `content` text,
  `views` integer DEFAULT 0,
  `relation_id` integer NOT NULL,
  `type_id` integer NOT NULL,
  `category_id` integer NOT NULL,
  `visibility` ENUM ('private', 'shared', 'public') DEFAULT 'private',
  `status` ENUM ('pending', 'published', 'rejected') DEFAULT 'pending',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Comment` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `ressource_id` integer NOT NULL,
  `parent_id` integer,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_id` integer NOT NULL
);

CREATE TABLE `Progression` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `ressource_id` integer NOT NULL,
  `is_favorite` bool DEFAULT false,
  `is_saved` bool DEFAULT false,
  `is_viewed` bool DEFAULT false,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Activity` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `creator_id` integer NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `event_date` datetime,
  `location` varchar(255),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `ActivityParticipant` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `activity_id` integer NOT NULL,
  `user_id` integer NOT NULL,
  `joined_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Relation` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `Type` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `Category` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

ALTER TABLE `Friend` ADD FOREIGN KEY (`user_1`) REFERENCES `User` (`id`);

ALTER TABLE `Friend` ADD FOREIGN KEY (`user_2`) REFERENCES `User` (`id`);

ALTER TABLE `Ressource` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Ressource` ADD FOREIGN KEY (`relation_id`) REFERENCES `Relation` (`id`);

ALTER TABLE `Ressource` ADD FOREIGN KEY (`type_id`) REFERENCES `Type` (`id`);

ALTER TABLE `Ressource` ADD FOREIGN KEY (`category_id`) REFERENCES `Category` (`id`);

ALTER TABLE `Comment` ADD FOREIGN KEY (`ressource_id`) REFERENCES `Ressource` (`id`);

ALTER TABLE `Comment` ADD FOREIGN KEY (`parent_id`) REFERENCES `Comment` (`id`);

ALTER TABLE `Comment` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Progression` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Progression` ADD FOREIGN KEY (`ressource_id`) REFERENCES `Ressource` (`id`);

ALTER TABLE `Activity` ADD FOREIGN KEY (`creator_id`) REFERENCES `User` (`id`);

ALTER TABLE `ActivityParticipant` ADD FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`);

ALTER TABLE `ActivityParticipant` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);


INSERT INTO `Category` (`name`) VALUES 
('Communication'),
('Cultures'),
('Développement personnel'),
('Intelligence émotionnelle'),
('Loisirs'),
('Monde professionnel'),
('Parentalité'),
('Qualité de vie'),
('Recherche de sens'),
('Santé physique'),
('Santé psychique'),
('Spiritualité'),
('Vie affective');

INSERT INTO `Relation` (`name`) VALUES 
('Soi'),
('Conjoints'),
('Famille : enfants / parents / fratrie'),
('Professionnelle : collègues, collaborateurs et managers'),
('Amis et communautés'),
('Inconnus');

INSERT INTO `Type` (`name`) VALUES 
('Activité / Jeu à réaliser'),
('Article'),
('Carte défi'),
('Cours au format PDF'),
('Exercice / Atelier'),
('Fiche de lecture'),
('Jeu en ligne'),
('Vidéo');