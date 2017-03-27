/*CREATION TABLE*/
DROP TABLE ALGOFOUILLE;
CREATE TABLE ALGOFOUILLE
(
  idAlgo NUMBER PRIMARY KEY,
  typeAlgo VARCHAR(20),
  complexite VARCHAR(20),
  nomAlgo VARCHAR(20)
);

/*INSERTIONS*/
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (2,'magique','o(n)','Apriori');
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (3,'recherche','o(3n)','dichotomique');
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (4,'magique','o(4n)','triRapide');
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (5,'magique','o(5n)','triFusion');
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (6,'magique','o(6n)','branchAndBound');
INSERT INTO ALGOFOUILLE (idAlgo,typeAlgo,complexite,nomAlgo) VALUES (7,'lent','o(7n)','bulle');


SELECT * FROM M1OPT_15.maison;
/*
  15	bleu                	5
  65	rouge               	6
  75	vert                	7
  85	orange              	8
  95	violet              	9
*/


GRANT SELECT ON ALGOFOUILLE TO M1OPT_15;

/*Donner droit*/
GRANT SELECT ON M1OPT_13.etudiants TO M1OPT_15 WITH GRANT OPTION;
/*Supprimer droit*/
REVOKE SELECT ON M1OPT_13.etudiants FROM M1OPT_15;
/*Selects*/
SELECT * FROM M1OPT_08.TEST;
SELECT * FROM M1OPT_13.etudiants;

/*Creation de la vue*/
CREATE VIEW VIEWALGO AS
SELECT nomAlgo
FROM ALGOFOUILLE;

/*Donner droit*/
GRANT SELECT ON M1OPT_14.VIEWALGO TO M1OPT_15 WITH GRANT OPTION;
/*Select vue*/
SELECT * FROM M1OPT_15.view_maison;

/*
Commentaires : 
  Si un parent donne ses droits à ses fils et que ce parent se fait revoke ses droits alors les fils du parents perdent leurs droits.
*/

/*
Hotel( NumH, NomH, AdresseH, TelephoneH )
Chambre( NumC, NumH#, NbPlaceC, EtageC, PrixC )
PClient( NumP, NomP, AdresseP, TelephoneP )
Reservation( NumR, NumP#, NumH#, DateA, DateP )
Occupation( NumP#, NumH#, NumC#, NumR# )
*/
drop table Hotel;
drop table Chambre;
drop table PClient;
drop table RESERVATION;
DROP TABLE Occupation; 

CREATE TABLE Hotel(
  numH NUMBER PRIMARY KEY,
  nomH VARCHAR(20),
  adresseH VARCHAR(20),
  telephoneH VARCHAR(12)
);

/*Indexations*/

CREATE INDEX INDX_HOTEL ON HOTEL (nomH);
CREATE BITMAP INDEX IDX_BITMAP_hotel ON HOTEL (nomH);

CREATE TABLE Chambre (
  numC NUMBER ,
  numH NUMBER,
  nbPlaceC NUMBER,
  etageC VARCHAR(20),
  prixC NUMBER,
  FOREIGN KEY (numH) REFERENCES Hotel(numH),
  PRIMARY KEY (numC,numH)
);

CREATE INDEX INDX_CHAMBRE ON CHAMBRE (prixC);
CREATE BITMAP INDEX IDX_BITMAP_CHAMBRE ON CHAMBRE (etageC); /*Il y a seulement 5 étages*/


CREATE TABLE PClient(
  numP NUMBER PRIMARY KEY,
  nomP VARCHAR(20),
  adresseP VARCHAR(20),
  telephoneP VARCHAR(12)
);

CREATE INDEX INDX_PCLIENT ON PCLIENT (numH);
CREATE BITMAP INDEX IDX_BITMAP_PCLIENT ON PCLIENT (numH);

CREATE TABLE Reservation (
  numR NUMBER(20) PRIMARY KEY,
  numP NUMBER(20),
  numH NUMBER(20),
  dateA DATE ,
  dateP DATE,
  FOREIGN KEY (numP) REFERENCES PClient(numP),
  FOREIGN KEY (numH) REFERENCES Hotel(numH)
);

CREATE INDEX INDX_RESERVATION ON RESERVATION (numH);
CREATE BITMAP INDEX IDX_BITMAP_RESERVATION ON RESERVATION (numH);

CREATE TABLE Occupation (
  numP NUMBER REFERENCES PClient(numP),
  numH NUMBER,
  numC NUMBER,
  numR NUMBER REFERENCES Reservation(numR),
  PRIMARY KEY(numP,numH,numC),
  FOREIGN KEY (numH,numC) REFERENCES Chambre(numH,numC)
);

CREATE INDEX OCCUPATION ON OCCUPATION (numH);
CREATE BITMAP INDEX IDX_BITMAP_RESERVATION ON OCCUPATION (numP,bumH);

/*SEQUENCES*/
DROP SEQUENCE CptHotel;
CREATE SEQUENCE CptHotel
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
/
DROP SEQUENCE CptChambre;
  CREATE SEQUENCE CptChambre
    INCREMENT BY 1
    START WITH 1
    NOCYCLE;
  /

DROP SEQUENCE CptPClient;
CREATE SEQUENCE CptPClient
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
/

DROP SEQUENCE CptReservation;
CREATE SEQUENCE CptReservation
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
/

/*PLSQL*/
/*COMMENTAIRES : LE COMMIT EST PLUS RAPIDE EN DEHORS DE BOUCLE*/
 SET TIMING ON
/*INSERT HOTEL*/
DELETE FROM HOTEL;
BEGIN
FOR x in 1..10000 LOOP
    INSERT INTO Hotel VALUES ( cptHOTEL.nextval, ROUND(DBMS_RANDOM.VALUE(1,10000))||'Ibis', CptHOTEL.currval || ' Rue I '||ROUND(DBMS_RANDOM.VALUE(1,10000)) , '0404040404' );
    -- after the block PL/SQL
  END LOOP;
      COMMIT; -- commit here or after the loop or
END;
/
/*
  Commit in loop
  Procédure PL/SQL terminée.
  Elapsed: 00:00:01.297
*/
/*
  Commit at the end
  Procédure PL/SQL terminée.
  Elapsed: 00:00:00.938
*/

/*D'après les temps d'exécution il est largement préférable de commit après le for : sur notre test on gagne 0.3 milliseocndes.
Il est dont plus opti de commit 10 000 données que commit 10 000 fois une données. */

/*INSERT PCLIENT */
DELETE FROM PCLIENT;

BEGIN
FOR x in 1..10000 LOOP
    INSERT INTO PClient VALUES ( CptPClient.nextval, CptPClient.CURRVAL||'client','Adresse ' || CptPClient.CURRVAL,'061785'||CptPClient.Currval);
    -- after the block PL/SQL
  END LOOP;
  COMMIT; -- commit here or after the loop or
END;
/

/*
  Commit at the end
  Procédure PL/SQL terminée.
  Elapsed: 00:00:00.628s
*/

/*INSERT RESERVATION */
DELETE FROM RESERVATION;

BEGIN
FOR x in 1..10000 LOOP
    INSERT INTO Reservation VALUES ( CptReservation.nextval, ROUND(DBMS_RANDOM.VALUE(1,10000)),ROUND(DBMS_RANDOM.VALUE(1,10000)),TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),TO_DATE('2003/05/10 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));
    -- after the block PL/SQL
  END LOOP; 
  COMMIT; 
END;
/

/*
  Commit in loop
  Procédure PL/SQL terminée.
  Elapsed: 00:00:01.860

*/

/*INSERT OCCUPATION */
DELETE FROM OCCUPATION;
DELETE FROM CHAMBRE;

DECLARE 
r integer;
BEGIN
FOR x in 10001..20000 LOOP
    r := ROUND(DBMS_RANDOM.VALUE(10000));
    INSERT INTO Chambre VALUES ( CptChambre.nextval,r ,ROUND(DBMS_RANDOM.VALUE(1,5)),'etage 4',ROUND(DBMS_RANDOM.VALUE(1,100)));
    INSERT INTO Occupation VALUES (ROUND(DBMS_RANDOM.VALUE(1,10000)),r,CptChambre.CURRVAL,ROUND(DBMS_RANDOM.VALUE(1,10000)) );
    -- after the block PL/SQL
  END LOOP;
  COMMIT; -- commit here or after the loop or 
END;
/

/*
DELETE FROM CHAMBRE;
BEGIN
FOR x in 1..10000 LOOP
    INSERT INTO Chambre VALUES ( CptChambre.nextval, ROUND(DBMS_RANDOM.VALUE(1,10000)),10,'etage 4',500);
      COMMIT; -- commit here or after the loop or
    -- after the block PL/SQL
  END LOOP;
END;
*/


/*********************************TP5***********************************************/

/*USERS TABLE */
SELECT table_name,num_rows,BLOCKS FROM USER_TABLES;
/*ALGOFOUILLE	6	5
CHAMBRE	10000	43
HOTEL	10000	50
OCCUPATION	10000	35
PCLIENT	10000	65
RESERVATION	10000	50*/

ANALYZE TABLE HOTEL COMPUTE STATISTICS FOR TABLE;
ANALYZE TABLE RESERVATION COMPUTE STATISTICS FOR TABLE;
ANALYZE TABLE OCCUPATION COMPUTE STATISTICS FOR TABLE;
ANALYZE TABLE CHAMBRE COMPUTE STATISTICS FOR TABLE;
ANALYZE TABLE PCLIENT COMPUTE STATISTICS FOR TABLE;

SELECT table_name,num_rows,BLOCKS FROM USER_TABLES;
/*
RESERVATION	10000	50
PCLIENT	10000	65
OCCUPATION	10000	35
HOTEL	20000	118 => MISE A JOUR DU NOMBRE DE LIGNES nbrs de lignes + 68
CHAMBRE	10000	43
ALGOFOUILLE	6	5
*/

ANALYZE TABLE HOTEL COMPUTE STATISTICS FOR ALL INDEXES;
ANALYZE TABLE RESERVATION COMPUTE STATISTICS FOR ALL INDEXES;
ANALYZE TABLE OCCUPATION COMPUTE STATISTICS FOR ALL INDEXES;
ANALYZE TABLE CHAMBRE COMPUTE STATISTICS FOR ALL INDEXES;
ANALYZE TABLE PCLIENT COMPUTE STATISTICS FOR ALL INDEXES;

SELECT * FROM USER_INDEXES;
SELECT index_name,blevel,leaf_blocks,distinct_keys FROM USER_INDEXES;
/*
INDX_HOTEL	1	89	20000
PERSON_RESERVATION	0	1	1
SYS_C00108462	0	1	6
SYS_C00108863	1	37	20000
SYS_C00108864	1	32	10000
SYS_C00108866	1	18	10000
SYS_C00108867	1	18	10000
SYS_C00108870	1	35	10000
*/

/* TABLE PLAN_TABLE ne pas exécuter*/


CREATE TABLE PLAN_TABLE (
STATEMENT_ID VARCHAR2 (30),
TIMESTAMP DATE,
REMARKS VARCHAR2 (80),
OPERATION VARCHAR2 (30),
OPTIONS VARCHAR2 (30),
OBJECT_NODE VARCHAR2 (30),
OBJECT_OWNER VARCHAR2 (30),
OBJECT_NAME VARCHAR2 (30),
OBJECT_INSTANCE NUMBER (38),
OBJECT_TYPE VARCHAR2 (30),
SEARCH_COLUMNS NUMBER (38),
ID NUMBER (38),
PARENT_ID NUMBER (38),
POSITION NUMBER (38),
OTHER LONG);

/*On choisit un identifiant pour identifier le plan d’exécution et on spécifie la requête à analyser :*/

EXPLAIN PLAN
SET STATEMENT_ID='exemple'
FOR SELECT NOM from ACTEURS order by NOM

CREATE TABLE Hotel(
  numH NUMBER PRIMARY KEY,
  nomH VARCHAR(20),
  adresseH VARCHAR(20),
  telephoneH VARCHAR(12)
);
SET autotrace ON EXPLAIN;
set timing on ;
CREATE INDEX INDX_HOTEL ON HOTEL (nomH);
CREATE BITMAP INDEX IDX_BITMAP_hotel ON HOTEL (nomH);

/*question 1 sans index hotel*/

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_sans_index'
FOR SELECT adresseH from HOTEL WHERE adresseH ='11665 Rue I 8815' order by adresseH ;

/*. Selection sans index*/
 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_sans_index'
connect by prior id = parent_id
and statement_id='Hotel_sans_inde';

/*
SELECT STATEMENT   Cost = 15
*/
DROP INDEX INDX_HOTEL;
CREATE INDEX INDX_HOTEL ON HOTEL (adresseH);

/*
 Id  | Operation              | Name       | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------
|   0 | CREATE INDEX STATEMENT |            | 10000 |   107K|    21   (0)| 00:00:01 |
|   1 |  INDEX BUILD NON UNIQUE| INDX_HOTEL |       |       |            |          |
|   2 |   SORT CREATE INDEX    |            | 10000 |   107K|            |          |
|   3 |    INDEX FAST FULL SCAN| INDX_HOTEL |       |       |            |          |
-------------------------------------------------------------------------------------
 
Note
-----
   - estimated index size: 262K bytes
*/
EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index'
FOR SELECT adresseH from HOTEL WHERE adresseH ='11665 Rue I 8815' order by adresseH ;

/*Selection avec Index*/
 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index'
connect by prior id = parent_id
and statement_id='Hotel_avec_index';

/*
SELECT STATEMENT   Cost = 1
  INDEX RANGE SCAN INDX_HOTEL 
*/

/*Selection conjonctive avec un index*/
CREATE INDEX INDX_HOTEL ON HOTEL (adresseH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index_conjonction'
FOR SELECT adresseH from HOTEL WHERE adresseH ='11665 Rue I 8815' AND nomH ='226Ibis' ;

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index_conjonction'
connect by prior id = parent_id
and statement_id='Hotel_avec_index_conjonction';
/*
SELECT STATEMENT   Cost = 2
  TABLE ACCESS BY INDEX ROWID BATCHED HOTEL 
    INDEX RANGE SCAN INDX_HOTEL 
*/

/*Selection conjonctive sans index*/

DROP INDEX INDX_HOTEL;

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_sans_index_conjonction3'
FOR SELECT adresseH,nomH from HOTEL WHERE adresseH ='11665 Rue I 8815' AND nomH ='226Ibis' ;

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_sans_index_conjonction3'
connect by prior id = parent_id
and statement_id='Hotel_sans_index_conjonction3';

/*
SELECT STATEMENT   Cost = 15
  TABLE ACCESS FULL HOTEL 
*/

/*Selection conjonctive avec deux index*/

CREATE INDEX INDX_HOTEL ON HOTEL (adresseH,nomH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_indexs_conjonction2'
FOR SELECT adresseH,nomH from HOTEL WHERE adresseH ='11665 Rue I 8815' AND nomH ='226Ibis' ;

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_indexs_conjonction2'
connect by prior id = parent_id
and statement_id='Hotel_avec_indexs_conjonction2';

/*
SELECT STATEMENT   Cost = 1
  INDEX RANGE SCAN INDX_HOTEL 
*/

/*Selection disjonctive avec deux index*/
DROP INDEX INDX_HOTEL;
CREATE INDEX INDX_HOTEL ON HOTEL (nomH,adresseH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index112'
FOR SELECT adresseH,nomH from HOTEL WHERE adresseH ='11665 Rue I 8815' OR nomH ='7036Ibis' ;

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index112'
connect by prior id = parent_id
and statement_id='Hotel_avec_index112';

/*PAS DE GAINS L'INDEXATION N'EST PAS EFFICACE*/
/*AVEC INDEX*/
/*
SELECT STATEMENT   Cost = 5
  CONCATENATION   
    INDEX RANGE SCAN INDX_HOTEL 
    INDEX SKIP SCAN INDX_HOTEL 
  */
  
DROP INDEX INDX_HOTEL;
/*SANS INDEX*/
SELECT COUNT( DISTINCT nomH) FROM HOTEL;
/*1 => 6322*/
/*
SELECT STATEMENT   Cost = 15
  TABLE ACCESS FULL HOTEL 
*/



/*************************JOINTURES *****************************/

/*. Jointures avec 1 index*/

DROP INDEX INDX_HOTEL;
CREATE INDEX INDX_HOTEL ON HOTEL (adresseH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index_jointurel'
FOR SELECT adresseH,O.numP from HOTEL H, OCCUPATION O WHERE O.numH = H.numH AND H.numH ='10169';

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index_jointurel'
connect by prior id = parent_id
and statement_id='Hotel_avec_index_jointurel';

/*
SELECT STATEMENT   Cost = 13
  NESTED LOOPS   
    TABLE ACCESS BY INDEX ROWID HOTEL 
      INDEX UNIQUE SCAN SYS_C00108863 
    INDEX FAST FULL SCAN SYS_C00108870 
*/
/*SANS INDEX*/

DROP INDEX INDX_HOTEL;
EXPLAIN PLAN
SET STATEMENT_ID='Hotel_sans_index_jointurel'
FOR SELECT adresseH,O.numP from HOTEL H, OCCUPATION O WHERE O.numH = H.numH AND H.numH ='10169';

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_sans_index_jointurel'
connect by prior id = parent_id
and statement_id='Hotel_sans_index_jointurel';

/*AVEC INDEX */
DROP INDEX INDX_HOTEL;
CREATE INDEX INDX_HOTEL ON HOTEL (adresseH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index_jointurel1234'
FOR SELECT adresseH,O.numP from HOTEL H, OCCUPATION O WHERE O.numH = H.numH AND H.numH ='10169';

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index_jointurel1234'
connect by prior id = parent_id
and statement_id='Hotel_avec_index_jointurel1234';

/*. Jointures avec 2 index*/

DROP INDEX INDX_OCCUPATION;
DROP INDEX INDX_CHAMBRE;

CREATE INDEX INDX_OCCUPATION ON OCCUPATION (adresseH,nomH);
CREATE INDEX INDX_CHAMBRE  ON CHAMBRE (numH);

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index_jointurel24'
FOR SELECT adresseH,O.numP from HOTEL H, OCCUPATION O WHERE O.numH = H.numH AND H.numH ='284';

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index_jointurel24'
connect by prior id = parent_id
and statement_id='Hotel_avec_index_jointurel24';
/*. Jointures avec 2 index*/

DROP INDEX INDX_OCCUPATION;
DROP INDEX INDX_CHAMBRE;

EXPLAIN PLAN
SET STATEMENT_ID='Hotel_avec_index_jointure10'
FOR SELECT adresseH,nomH,numR from CHAMBRE C, OCCUPATION O WHERE O.numR = C.prixC AND H.prixC ='284' ;

 select lpad(' ', 2*(level-1)) || operation ||' '||options||' ' || object_name
||' '||decode(id, 0, 'Cost = ' ||position) from plan_table
start with id = 0 and STATEMENT_ID='Hotel_avec_index_jointure10'
connect by prior id = parent_id
and statement_id='Hotel_avec_index_jointure10';

