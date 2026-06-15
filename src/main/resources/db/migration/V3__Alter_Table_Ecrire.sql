/*==============================================================*/
/* ALTER TABLE ECRIRE - Add timestamps                         */
/*==============================================================*/
ALTER TABLE biblio.ECRIRE
ADD COLUMN CREATED_AT_ECRIRE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN UPDATED_AT_ECRIRE TIMESTAMP DEFAULT CURRENT_TIMESTAMP;