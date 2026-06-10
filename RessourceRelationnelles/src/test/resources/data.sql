-- Seeding des tables de base pour les tests

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

INSERT INTO `User` (`username`, `email`, `password`, `role`, `is_active`) VALUES
('Alice', 'alice@mail.com', '1234', 'CITIZEN', true),
('Bob', 'bob@mail.com', '1234', 'CITIZEN', true),
('Charlie', 'charlie@mail.com', '1234', 'MODERATOR', true),
('AdminSuper', 'admin@mail.com', 'admin', 'ADMINISTRATOR', true);

INSERT INTO `Friend` (`user_1`, `user_2`, `status`) VALUES
(1, 2, 'accepted');

INSERT INTO `Friend` (`user_1`, `user_2`, `status`) VALUES
(2, 3, 'pending');

INSERT INTO `Ressource` (`user_id`, `title`, `content`, `views`, `relation_id`, `type_id`, `category_id`, `visibility`, `status`) VALUES
(1, 'Comment mieux communiquer avec ses enfants', 'Voici quelques astuces pour instaurer un dialogue sain à la maison. Premièrement, l''écoute active...', 142, 3, 2, 7, 'public_visibility', 'published');

INSERT INTO `Ressource` (`user_id`, `title`, `content`, `views`, `relation_id`, `type_id`, `category_id`, `visibility`, `status`) VALUES
(2, 'Le jeu des 7 familles des émotions', 'Un petit jeu à imprimer pour apprendre à reconnaître les émotions primaires.', 12, 5, 1, 4, 'shared', 'pending');

INSERT INTO `Ressource` (`user_id`, `title`, `content`, `views`, `relation_id`, `type_id`, `category_id`, `visibility`, `status`) VALUES
(3, 'Mes réflexions sur le monde pro', 'Aujourd''hui j''ai remarqué que...', 0, 4, 2, 6, 'private_visibility', 'pending');

INSERT INTO `Comment` (`ressource_id`, `parent_id`, `content`, `user_id`) VALUES
(1, NULL, 'Super article, je vais essayer de mettre ça en place avec ma fille ce soir !', 2);

INSERT INTO `Comment` (`ressource_id`, `parent_id`, `content`, `user_id`) VALUES
(1, 1, 'Merci Bob ! N''hésite pas à me dire comment ça s''est passé.', 1);

INSERT INTO `Comment` (`ressource_id`, `parent_id`, `content`, `user_id`) VALUES
(1, NULL, 'Très pertinent, je valide en tant que modérateur.', 3);

INSERT INTO `Progression` (`user_id`, `ressource_id`, `is_favorite`, `is_saved`, `is_viewed`) VALUES
(2, 1, true, false, true);

INSERT INTO `Progression` (`user_id`, `ressource_id`, `is_favorite`, `is_saved`, `is_viewed`) VALUES
(3, 2, false, true, false);
