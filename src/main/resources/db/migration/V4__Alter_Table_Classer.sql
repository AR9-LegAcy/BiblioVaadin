/*==============================================================*/
/* ALTER TABLE CLASSER - Add timestamps                         */
/*==============================================================*/
ALTER TABLE biblio.CLASSER
ADD COLUMN CREATED_AT_CLASSER TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN UPDATED_AT_CLASSER TIMESTAMP DEFAULT CURRENT_TIMESTAMP;