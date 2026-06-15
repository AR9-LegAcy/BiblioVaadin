/*==============================================================*/
/* ALTER TABLE STOCKER - Add timestamps                         */
/*==============================================================*/
ALTER TABLE biblio.STOCKER
ADD COLUMN CREATED_AT_STOCKER TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN UPDATED_AT_STOCKER TIMESTAMP DEFAULT CURRENT_TIMESTAMP;