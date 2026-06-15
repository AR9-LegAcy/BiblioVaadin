/*==============================================================*/
/* Schema : biblio                                              */
/*==============================================================*/
CREATE SCHEMA IF NOT EXISTS biblio;


/*==============================================================*/
/* DROP TABLES                                                  */
/*==============================================================*/

DROP TABLE IF EXISTS biblio.CLASSER CASCADE;
DROP TABLE IF EXISTS biblio.ECRIRE CASCADE;
DROP TABLE IF EXISTS biblio.EMPRUNTER CASCADE;
DROP TABLE IF EXISTS biblio.STOCKER CASCADE;

DROP TABLE IF EXISTS biblio.LIVRE CASCADE;
DROP TABLE IF EXISTS biblio.DOCUMENT CASCADE;
DROP TABLE IF EXISTS biblio.EVENEMENT CASCADE;

DROP TABLE IF EXISTS biblio.BIBLIOTHECAIRE CASCADE;
DROP TABLE IF EXISTS biblio.EMPRUNTEUR CASCADE;

DROP TABLE IF EXISTS biblio.AUTEUR CASCADE;
DROP TABLE IF EXISTS biblio.EDITEUR CASCADE;
DROP TABLE IF EXISTS biblio.BIBLIOTHEQUE CASCADE;

DROP TABLE IF EXISTS biblio.TYPE_AUTEUR CASCADE;
DROP TABLE IF EXISTS biblio.TYPE_DOCUMENT CASCADE;
DROP TABLE IF EXISTS biblio.TYPE_EVENEMENT CASCADE;


/*==============================================================*/
/* Table : AUTEUR                                               */
/*==============================================================*/
CREATE TABLE biblio.AUTEUR (
    ID_AUTEUR            SERIAL NOT NULL,
    NOM_AUTEUR           VARCHAR(100) NOT NULL,
    PRENOM_AUTEUR        VARCHAR(100) NOT NULL,
    DATE_NAISSANCE       DATE,
    DATE_MORT            DATE,
    PAYS_AUTEUR          VARCHAR(100),
    VILLE_AUTEUR         VARCHAR(100),
    NATIONNALITE_AUTEUR  VARCHAR(100),
    WIKIPEDIA_AUTEUR     VARCHAR(150),
    CREATED_AT_AUTEUR    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_AUTEUR    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_AUTEUR PRIMARY KEY (ID_AUTEUR),
    CONSTRAINT UQ_AUTEUR_IDENTITE UNIQUE (NOM_AUTEUR, PRENOM_AUTEUR, DATE_NAISSANCE),
    CONSTRAINT CK_AUTEUR_DATES_COMPLETES CHECK (DATE_MORT IS NULL OR (DATE_NAISSANCE IS NULL OR DATE_MORT >= DATE_NAISSANCE))
);

/* Index : AUTEUR_PK */
CREATE UNIQUE INDEX AUTEUR_PK ON biblio.AUTEUR (ID_AUTEUR);


/*==============================================================*/
/* Table : TYPE_AUTEUR                                          */
/*==============================================================*/
CREATE TABLE biblio.TYPE_AUTEUR (
    ID_TYPE_AUTEUR       SERIAL NOT NULL,
    NOM_TYPE_AUTEUR      VARCHAR(100),
    CREATED_AT_TYPE_AUTEUR TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_TYPE_AUTEUR TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_TYPE_AUTEUR PRIMARY KEY (ID_TYPE_AUTEUR),
    CONSTRAINT UQ_TYPE_AUTEUR_NOM UNIQUE (NOM_TYPE_AUTEUR)
);

/* Index : TYPE_AUTEUR_PK */
CREATE UNIQUE INDEX TYPE_AUTEUR_PK ON biblio.TYPE_AUTEUR (ID_TYPE_AUTEUR);


/*==============================================================*/
/* Table : TYPE_DOCUMENT                                        */
/*==============================================================*/
CREATE TABLE biblio.TYPE_DOCUMENT (
    ID_TYPE_DOCUMENT     SERIAL NOT NULL,
    NOM_TYPE_DOCUMENT    VARCHAR(100) NOT NULL,
    CREATED_AT_TYPE_DOCUMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_TYPE_DOCUMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_TYPE_DOCUMENT PRIMARY KEY (ID_TYPE_DOCUMENT),
    CONSTRAINT UQ_TYPE_DOCUMENT_NOM UNIQUE (NOM_TYPE_DOCUMENT)
);

/* Index : TYPE_DOCUMENT_PK */
CREATE UNIQUE INDEX TYPE_DOCUMENT_PK ON biblio.TYPE_DOCUMENT (ID_TYPE_DOCUMENT);


/*==============================================================*/
/* Table : TYPE_EVENEMENT                                       */
/*==============================================================*/
CREATE TABLE biblio.TYPE_EVENEMENT (
    ID_TYPE_EVENEMENT    SERIAL NOT NULL,
    NOM_TYPE_EVENT       VARCHAR(100),
    CREATED_AT_TYPE_EVENEMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_TYPE_EVENEMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_TYPE_EVENEMENT PRIMARY KEY (ID_TYPE_EVENEMENT)
);

/* Index : TYPE_EVENEMENT_PK */
CREATE UNIQUE INDEX TYPE_EVENEMENT_PK ON biblio.TYPE_EVENEMENT (ID_TYPE_EVENEMENT);


/*==============================================================*/
/* Table : BIBLIOTHEQUE                                         */
/*==============================================================*/
CREATE TABLE biblio.BIBLIOTHEQUE (
   ID_BIBLIOTHEQUE      SERIAL NOT NULL,
   NOM_BIBLIOTHEQUE     VARCHAR(150) NOT NULL,
   ADRESSE_RUE_BIBLIOTHEQUE VARCHAR(200) NOT NULL,
   ADRESSE_VILLE_BIBLIOTHEQUE VARCHAR(100) NOT NULL,
   ADRESSE_CODEPOSTAL_BIBLIOTHEQUE VARCHAR(5) NOT NULL,
   HORAIRES_BIBLIOTHEQUE VARCHAR(200),
   CREATED_AT_BIBLIOTHEQUE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   UPDATED_AT_BIBLIOTHEQUE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   CONSTRAINT PK_BIBLIOTHEQUE PRIMARY KEY (ID_BIBLIOTHEQUE),
   CONSTRAINT CK_CODEPOSTAL CHECK (ADRESSE_CODEPOSTAL_BIBLIOTHEQUE ~ '^[0-9]{5}$')
);

/* Index : BIBLIOTHEQUE_PK */
CREATE UNIQUE INDEX BIBLIOTHEQUE_PK ON biblio.BIBLIOTHEQUE (ID_BIBLIOTHEQUE);


/*==============================================================*/
/* Table : EDITEUR                                              */
/*==============================================================*/
CREATE TABLE biblio.EDITEUR (
    ID_EDITEUR           SERIAL NOT NULL,
    NOM_SOCIETE          VARCHAR(100),
    ADRESSE_EDITEUR      VARCHAR(150),
    SITEWEB_EDITEUR      VARCHAR(150),
    WIKIPEDIA_EDITEUR    VARCHAR(200),
    CREATED_AT_EDITEUR   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_EDITEUR   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_EDITEUR PRIMARY KEY (ID_EDITEUR)
);

/* Index : EDITEUR_PK */
CREATE UNIQUE INDEX EDITEUR_PK ON biblio.EDITEUR (ID_EDITEUR);


/*==============================================================*/
/* Table : BIBLIOTHECAIRE                                       */
/*==============================================================*/
CREATE TABLE biblio.BIBLIOTHECAIRE (
   PSEUDO_BIBLIOTHECAIRE VARCHAR(50) NOT NULL,
   ID_BIBLIOTHEQUE       INTEGER NOT NULL,
   NOM_BIBLIOTHECAIRE    VARCHAR(100) NOT NULL,
   PRENOM_BIBLIOTHECAIRE VARCHAR(100) NOT NULL,
   ADRESSE_RUE_BIBLIOTHECAIRE VARCHAR(200) NOT NULL,
   ADRESSE_VILLE_BIBLIOTHECAIRE VARCHAR(100) NOT NULL,
   ADRESSE_CODEPOSTAL_BIBLIOTHECAIRE VARCHAR(5) NOT NULL,
   EMAIL_BIBLIOTHECAIRE VARCHAR(250) NOT NULL,
   DATE_NAISSANCE_BIBLIOTHECAIRE DATE,
   MOT_DE_PASSE         VARCHAR(255) NOT NULL,
   CREATED_AT_BIBLIOTHECAIRE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   UPDATED_AT_BIBLIOTHECAIRE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   CONSTRAINT PK_BIBLIOTHECAIRE PRIMARY KEY (PSEUDO_BIBLIOTHECAIRE),
   CONSTRAINT UQ_BIBLIOTHECAIRE_EMAIL UNIQUE (EMAIL_BIBLIOTHECAIRE),
   CONSTRAINT CK_EMAIL_BIBLIOTHECAIRE CHECK (EMAIL_BIBLIOTHECAIRE ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

/* Index : BIBLIOTHECAIRE_PK */
CREATE UNIQUE INDEX BIBLIOTHECAIRE_PK ON biblio.BIBLIOTHECAIRE (PSEUDO_BIBLIOTHECAIRE);

/* Index FK : BIBLIOTHECAIRE_BIBLIOTHEQUE_FK */
CREATE INDEX BIBLIOTHECAIRE_BIBLIOTHEQUE_FK ON biblio.BIBLIOTHECAIRE (ID_BIBLIOTHEQUE);


/*==============================================================*/
/* Table : EMPRUNTEUR                                           */
/*==============================================================*/
CREATE TABLE biblio.EMPRUNTEUR (
   CARTE_EMPRUNTEUR     NUMERIC(10) NOT NULL,
   NOM_EMPRUNTEUR        VARCHAR(100) NOT NULL,
   PRENOM_EMPRUNTEUR     VARCHAR(100) NOT NULL,
   ADRESSE_RUE_EMPRUNTEUR VARCHAR(200) NOT NULL,
   ADRESSE_VILLE_EMPRUNTEUR VARCHAR(100) NOT NULL,
   ADRESSE_CODEPOSTAL_EMPRUNTEUR VARCHAR(5) NOT NULL,
   EMAIL_EMPRUNTEUR      VARCHAR(250) NOT NULL,
   DATE_NAISSANCE_EMPRUNTEUR DATE,
   DEBUT_ABONNEMENT      DATE,
   EXPIRATION_ABONNEMENT DATE,
   MOT_DE_PASSE          VARCHAR(255) NOT NULL,
   CREATED_AT_EMPRUNTEUR TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   UPDATED_AT_EMPRUNTEUR TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   CONSTRAINT PK_EMPRUNTEUR PRIMARY KEY (CARTE_EMPRUNTEUR),
   CONSTRAINT UQ_CARTE_EMPRUNTEUR UNIQUE (CARTE_EMPRUNTEUR),
   CONSTRAINT UQ_EMPRUNTEUR_EMAIL UNIQUE (EMAIL_EMPRUNTEUR),
   CONSTRAINT CK_ABONNEMENT_DATES CHECK (EXPIRATION_ABONNEMENT IS NULL OR EXPIRATION_ABONNEMENT > DEBUT_ABONNEMENT),   
   CONSTRAINT CK_EMAIL_EMPRUNTEUR CHECK (EMAIL_EMPRUNTEUR ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

/* Index : EMPRUNTEUR_PK */
CREATE UNIQUE INDEX EMPRUNTEUR_PK ON biblio.EMPRUNTEUR (CARTE_EMPRUNTEUR);


/*==============================================================*/
/* Table : DOCUMENT                                             */
/*==============================================================*/
CREATE TABLE biblio.DOCUMENT (
    ID_DOCUMENT           SERIAL NOT NULL,
    ID_TYPE_DOCUMENT      INTEGER NOT NULL,
    GIF_DOCUMENT          VARCHAR(200),
    FORMAT_TAILLE         VARCHAR(100),
    DESCRIPTION_DOCUMENT  VARCHAR(250),
    DATE_ACQUISITION      DATE,
    CODE_EMPLACEMENT      VARCHAR(10),
    CODE_ISBN             VARCHAR(17) NOT NULL,
    CODE_EMPRUNTABLE      BOOLEAN,
    ETAT_DOCUMENT         VARCHAR(100),
    CREATED_AT_DOCUMENT   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_DOCUMENT   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_DOCUMENT PRIMARY KEY (ID_DOCUMENT),
    CONSTRAINT UQ_DOCUMENT_ISBN UNIQUE (CODE_ISBN),
    CONSTRAINT CK_EMPRUNTABLE CHECK (CODE_EMPRUNTABLE IN (TRUE, FALSE))
);

/* Index : DOCUMENT_PK */
CREATE UNIQUE INDEX DOCUMENT_PK ON biblio.DOCUMENT (ID_DOCUMENT);

/* Index FK : DOCUMENT_TYPE_DOCUMENT_FK */
CREATE INDEX DOCUMENT_TYPE_DOCUMENT_FK ON biblio.DOCUMENT (ID_TYPE_DOCUMENT);


/*==============================================================*/
/* Table : LIVRE                                                */
/*==============================================================*/
CREATE TABLE biblio.LIVRE (
    ID_DOCUMENT           INTEGER NOT NULL,
    ID_LIVRE              SERIAL NOT NULL,
    ID_EDITEUR            INTEGER NOT NULL,
    ID_TYPE_DOCUMENT      INTEGER NOT NULL,
    GIF_DOCUMENT          VARCHAR(200),
    TITRE_LIVRE           VARCHAR(250) NOT NULL,
    NB_PAGES              INTEGER,
    DATE_PUBLICATION      DATE,
    FORMAT_TAILLE         VARCHAR(100),
    DESCRIPTION_DOCUMENT  VARCHAR(250),
    DATE_ACQUISITION      DATE,
    CODE_EMPLACEMENT      VARCHAR(10),
    CODE_ISBN             VARCHAR(17) NOT NULL,
    CODE_EMPRUNTABLE      BOOLEAN,
    ETAT_DOCUMENT         VARCHAR(100),
    CREATED_AT_DOCUMENT   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_DOCUMENT   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_LIVRE PRIMARY KEY (ID_DOCUMENT, ID_LIVRE),
    CONSTRAINT UQ_LIVRE_ISBN UNIQUE (CODE_ISBN),
    CONSTRAINT CK_LIVRE_PAGES CHECK (NB_PAGES > 0)
);

/* Index : LIVRE_PK */
CREATE UNIQUE INDEX LIVRE_PK ON biblio.LIVRE (ID_DOCUMENT, ID_LIVRE);

/* Index FK : LIVRE_EDITEUR_FK */
CREATE INDEX LIVRE_EDITEUR_FK ON biblio.LIVRE (ID_EDITEUR);

/* Index FK : LIVRE_DOCUMENT_FK */
CREATE INDEX LIVRE_DOCUMENT_FK ON biblio.LIVRE (ID_DOCUMENT);


/*==============================================================*/
/* Table : ECRIRE                                               */
/*==============================================================*/
CREATE TABLE biblio.ECRIRE (
    ID_DOCUMENT           INTEGER NOT NULL,
    ID_LIVRE              INTEGER NOT NULL,
    ID_AUTEUR             INTEGER NOT NULL,

    CONSTRAINT PK_ECRIRE PRIMARY KEY (ID_DOCUMENT, ID_LIVRE, ID_AUTEUR)
);

/* Index : ECRIRE_PK */
CREATE UNIQUE INDEX ECRIRE_PK ON biblio.ECRIRE (ID_DOCUMENT, ID_LIVRE, ID_AUTEUR);

/* Index FK : ECRIRE_LIVRE_FK */
CREATE INDEX ECRIRE_LIVRE_FK ON biblio.ECRIRE (ID_DOCUMENT, ID_LIVRE);

/* Index FK : ECRIRE_AUTEUR_FK */
CREATE INDEX ECRIRE_AUTEUR_FK ON biblio.ECRIRE (ID_AUTEUR);


/*==============================================================*/
/* Table : CLASSER                                              */
/*==============================================================*/
CREATE TABLE biblio.CLASSER (
    ID_TYPE_AUTEUR        INTEGER NOT NULL,
    ID_AUTEUR             INTEGER NOT NULL,

    CONSTRAINT PK_CLASSER PRIMARY KEY (ID_TYPE_AUTEUR, ID_AUTEUR)
);

/* Index : CLASSER_PK */
CREATE UNIQUE INDEX CLASSER_PK ON biblio.CLASSER (ID_TYPE_AUTEUR, ID_AUTEUR);

/* Index FK : CLASSER_TYPE_AUTEUR_FK */
CREATE INDEX CLASSER_TYPE_AUTEUR_FK ON biblio.CLASSER (ID_TYPE_AUTEUR);

/* Index FK : CLASSER_AUTEUR_FK */
CREATE INDEX CLASSER_AUTEUR_FK ON biblio.CLASSER (ID_AUTEUR);


/*==============================================================*/
/* Table : EMPRUNTER                                            */
/*==============================================================*/
CREATE TABLE biblio.EMPRUNTER (
    ID_DOCUMENT           INTEGER NOT NULL,
    CARTE_EMPRUNTEUR      NUMERIC(10) NOT NULL,
    DATE_EMPRUNT          DATE NOT NULL DEFAULT CURRENT_DATE,
    DATE_RETOUR_PREVUE    DATE,
    DATE_RETOUR_REELLE    DATE,
    PROLONGATION_EMPRUNT  NUMERIC,
    CREATED_AT_EMPRUNTER  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_EMPRUNTER  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_EMPRUNTER PRIMARY KEY (ID_DOCUMENT, CARTE_EMPRUNTEUR),
    CONSTRAINT CK_EMPRUNT_DATES CHECK ( DATE_RETOUR_PREVUE IS NULL OR DATE_RETOUR_PREVUE >= DATE_EMPRUNT),
    CONSTRAINT CK_RETOUR_REEL CHECK ( DATE_RETOUR_REELLE IS NULL OR DATE_RETOUR_REELLE >= DATE_EMPRUNT),
    CONSTRAINT CK_PROLONGATION CHECK (PROLONGATION_EMPRUNT IS NULL OR PROLONGATION_EMPRUNT >= 0)
);

/* Index : EMPRUNTER_PK */
CREATE UNIQUE INDEX EMPRUNTER_PK ON biblio.EMPRUNTER (ID_DOCUMENT, CARTE_EMPRUNTEUR);

/* Index : UX_EMPRUNT_ACTIF */
CREATE UNIQUE INDEX UX_EMPRUNT_ACTIF ON biblio.EMPRUNTER (ID_DOCUMENT) WHERE DATE_RETOUR_REELLE IS NULL;

/* Index FK : EMPRUNTER_DOCUMENT_FK */
CREATE INDEX EMPRUNTER_DOCUMENT_FK ON biblio.EMPRUNTER (ID_DOCUMENT);

/* Index FK : EMPRUNTER_EMPRUNTEUR_FK */
CREATE INDEX EMPRUNTER_EMPRUNTEUR_FK ON biblio.EMPRUNTER (CARTE_EMPRUNTEUR);


/*==============================================================*/
/* Table : STOCKER                                              */
/*==============================================================*/
CREATE TABLE biblio.STOCKER (
    ID_BIBLIOTHEQUE       INTEGER NOT NULL,
    ID_DOCUMENT           INTEGER NOT NULL,

    CONSTRAINT PK_STOCKER PRIMARY KEY (ID_BIBLIOTHEQUE, ID_DOCUMENT)
);

/* Index : STOCKER_PK */
CREATE UNIQUE INDEX STOCKER_PK ON biblio.STOCKER (ID_BIBLIOTHEQUE, ID_DOCUMENT);

/* Index FK : STOCKER_BIBLIOTHEQUE_FK */
CREATE INDEX STOCKER_BIBLIOTHEQUE_FK ON biblio.STOCKER (ID_BIBLIOTHEQUE);

/* Index FK : STOCKER_DOCUMENT_FK */
CREATE INDEX STOCKER_DOCUMENT_FK ON biblio.STOCKER (ID_DOCUMENT);


/*==============================================================*/
/* Table : EVENEMENT                                            */
/*==============================================================*/
CREATE TABLE biblio.EVENEMENT (
    ID_EVENEMENT         SERIAL NOT NULL,
    ID_TYPE_EVENEMENT    INTEGER NOT NULL,
    ID_BIBLIOTHEQUE      INTEGER NOT NULL,
    TITRE_EVENEMENT      VARCHAR(150),
    DESCRIPTION_EVENEMENT VARCHAR(250),
    DATE_DEBUT           DATE,
    DATE_FIN             DATE,
    CREATED_AT_EVENEMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT_EVENEMENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_EVENEMENT PRIMARY KEY (ID_EVENEMENT),
    CONSTRAINT CK_EVENEMENT_DATES CHECK (DATE_FIN IS NULL OR DATE_FIN >= DATE_DEBUT)
);

/* Index : EVENEMENT_PK */
CREATE UNIQUE INDEX EVENEMENT_PK ON biblio.EVENEMENT (ID_EVENEMENT);

/* Index FK : EVENEMENT_BIBLIOTHEQUE_FK */
CREATE INDEX EVENEMENT_BIBLIOTHEQUE_FK ON biblio.EVENEMENT (ID_BIBLIOTHEQUE);

/* Index FK : EVENEMENT_TYPE_EVENEMENT_FK */
CREATE INDEX EVENEMENT_TYPE_EVENEMENT_FK ON biblio.EVENEMENT (ID_TYPE_EVENEMENT);


/*==============================================================*/
/* BIBLIOTHECAIRE -> BIBLIOTHEQUE                               */
/*==============================================================*/
ALTER TABLE biblio.BIBLIOTHECAIRE
ADD CONSTRAINT FK_BIBLIOTHECAIRE_BIBLIOTHEQUE
FOREIGN KEY (ID_BIBLIOTHEQUE)
REFERENCES biblio.BIBLIOTHEQUE (ID_BIBLIOTHEQUE)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* DOCUMENT -> TYPE_DOCUMENT                                    */
/*==============================================================*/
ALTER TABLE biblio.DOCUMENT
ADD CONSTRAINT FK_DOCUMENT_TYPE_DOCUMENT
FOREIGN KEY (ID_TYPE_DOCUMENT)
REFERENCES biblio.TYPE_DOCUMENT (ID_TYPE_DOCUMENT)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* LIVRE -> EDITEUR                                             */
/*==============================================================*/
ALTER TABLE biblio.LIVRE
ADD CONSTRAINT FK_LIVRE_EDITEUR
FOREIGN KEY (ID_EDITEUR)
REFERENCES biblio.EDITEUR (ID_EDITEUR)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* LIVRE -> DOCUMENT (héritage)                                 */
/*==============================================================*/
ALTER TABLE biblio.LIVRE
ADD CONSTRAINT FK_LIVRE_DOCUMENT
FOREIGN KEY (ID_DOCUMENT)
REFERENCES biblio.DOCUMENT (ID_DOCUMENT)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* CLASSER -> TYPE_AUTEUR                                       */
/*==============================================================*/
ALTER TABLE biblio.CLASSER
ADD CONSTRAINT FK_CLASSER_TYPE_AUTEUR
FOREIGN KEY (ID_TYPE_AUTEUR)
REFERENCES biblio.TYPE_AUTEUR (ID_TYPE_AUTEUR)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* CLASSER -> AUTEUR                                            */
/*==============================================================*/
ALTER TABLE biblio.CLASSER
ADD CONSTRAINT FK_CLASSER_AUTEUR
FOREIGN KEY (ID_AUTEUR)
REFERENCES biblio.AUTEUR (ID_AUTEUR)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* ECRIRE -> LIVRE                                              */
/*==============================================================*/
ALTER TABLE biblio.ECRIRE
ADD CONSTRAINT FK_ECRIRE_LIVRE
FOREIGN KEY (ID_DOCUMENT, ID_LIVRE)
REFERENCES biblio.LIVRE (ID_DOCUMENT, ID_LIVRE)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* ECRIRE -> AUTEUR                                             */
/*==============================================================*/
ALTER TABLE biblio.ECRIRE
ADD CONSTRAINT FK_ECRIRE_AUTEUR
FOREIGN KEY (ID_AUTEUR)
REFERENCES biblio.AUTEUR (ID_AUTEUR)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* EMPRUNTER -> DOCUMENT                                        */
/*==============================================================*/
ALTER TABLE biblio.EMPRUNTER
ADD CONSTRAINT FK_EMPRUNTER_DOCUMENT
FOREIGN KEY (ID_DOCUMENT)
REFERENCES biblio.DOCUMENT (ID_DOCUMENT)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* EMPRUNTER -> EMPRUNTEUR                                      */
/*==============================================================*/
ALTER TABLE biblio.EMPRUNTER
ADD CONSTRAINT FK_EMPRUNTER_EMPRUNTEUR
FOREIGN KEY (CARTE_EMPRUNTEUR)
REFERENCES biblio.EMPRUNTEUR (CARTE_EMPRUNTEUR)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* STOCKER -> BIBLIOTHEQUE                                      */
/*==============================================================*/
ALTER TABLE biblio.STOCKER
ADD CONSTRAINT FK_STOCKER_BIBLIOTHEQUE
FOREIGN KEY (ID_BIBLIOTHEQUE)
REFERENCES biblio.BIBLIOTHEQUE (ID_BIBLIOTHEQUE)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* STOCKER -> DOCUMENT                                          */
/*==============================================================*/
ALTER TABLE biblio.STOCKER
ADD CONSTRAINT FK_STOCKER_DOCUMENT
FOREIGN KEY (ID_DOCUMENT)
REFERENCES biblio.DOCUMENT (ID_DOCUMENT)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* EVENEMENT -> BIBLIOTHEQUE                                    */
/*==============================================================*/
ALTER TABLE biblio.EVENEMENT
ADD CONSTRAINT FK_EVENEMENT_BIBLIOTHEQUE
FOREIGN KEY (ID_BIBLIOTHEQUE)
REFERENCES biblio.BIBLIOTHEQUE (ID_BIBLIOTHEQUE)
ON DELETE RESTRICT ON UPDATE RESTRICT;


/*==============================================================*/
/* EVENEMENT -> TYPE_EVENEMENT                                  */
/*==============================================================*/
ALTER TABLE biblio.EVENEMENT
ADD CONSTRAINT FK_EVENEMENT_TYPE_EVENEMENT
FOREIGN KEY (ID_TYPE_EVENEMENT)
REFERENCES biblio.TYPE_EVENEMENT (ID_TYPE_EVENEMENT)
ON DELETE RESTRICT ON UPDATE RESTRICT;