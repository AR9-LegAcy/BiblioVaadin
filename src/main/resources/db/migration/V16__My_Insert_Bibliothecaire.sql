INSERT INTO biblio.BIBLIOTHECAIRE
(PSEUDO_BIBLIOTHECAIRE, ID_BIBLIOTHEQUE, NOM_BIBLIOTHECAIRE, PRENOM_BIBLIOTHECAIRE,
 ADRESSE_RUE_BIBLIOTHECAIRE, ADRESSE_VILLE_BIBLIOTHECAIRE, ADRESSE_CODEPOSTAL_BIBLIOTHECAIRE,
 EMAIL_BIBLIOTHECAIRE, DATE_NAISSANCE_BIBLIOTHECAIRE, MOT_DE_PASSE,
 CREATED_AT_BIBLIOTHECAIRE)
VALUES
('admin_annecy', 1, 'Martin', 'Claire',
 '1 rue Royale', 'Annecy', '74000',
 'claire.annecy@biblio.fr', '1990-05-12', 'admin123', CURRENT_DATE),

('admin_lyon', 2, 'Durand', 'Paul',
 '10 rue Bellecour', 'Lyon', '69002',
 'paul.lyon@biblio.fr', '1985-11-20', 'admin123', CURRENT_DATE),

('admin_grenoble', 3, 'Bernard', 'Luc',
 '5 rue Centrale', 'Grenoble', '38000',
 'luc.grenoble@biblio.fr', '1992-03-15', 'admin123', CURRENT_DATE);