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
  Si un parent nous donne des droits à ses fils et que ce parent se fait revoke ses droits alors les fils du parents perdent leurs droits.
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


CREATE TABLE Chambre (
  numC NUMBER ,
  numH NUMBER,
  nbPlaceC NUMBER,
  etageC VARCHAR(20),
  prixC NUMBER,
  FOREIGN KEY (numH) REFERENCES Hotel(numH),
  PRIMARY KEY (numC,numH)
);


CREATE TABLE PClient(
  numP NUMBER PRIMARY KEY,
  nomP VARCHAR(20),
  adresseP VARCHAR(20),
  telephoneP VARCHAR(12)
);



CREATE TABLE Reservation (
  numR NUMBER(20) PRIMARY KEY,
  numP NUMBER(20),
  numH NUMBER(20),
  dateA DATE ,
  dateP DATE,
  FOREIGN KEY (numP) REFERENCES PClient(numP),
  FOREIGN KEY (numH) REFERENCES Hotel(numH)
);

CREATE TABLE Occupation (
  numP NUMBER REFERENCES PClient(numP),
  numH NUMBER,
  numC NUMBER,
  numR NUMBER REFERENCES Reservation(numR),
  PRIMARY KEY(numP,numH,numC),
  FOREIGN KEY (numH,numC) REFERENCES Chambre(numH,numC)
);

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
    INSERT INTO Hotel VALUES ( cptHOTEL.nextval, 'Ibis', CptHOTEL.currval || ' Rue I', '0404040404' );
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
    COMMIT; -- commit here or after the loop or
    -- after the block PL/SQL
  END LOOP; 
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
FOR x in 1..10000 LOOP
    r := ROUND(DBMS_RANDOM.VALUE(1,10000));
    INSERT INTO Chambre VALUES ( CptChambre.nextval,r ,10,'etage 4',500);
    INSERT INTO Occupation VALUES (ROUND(DBMS_RANDOM.VALUE(1,9999)),r,CptChambre.CURRVAL,ROUND(DBMS_RANDOM.VALUE(1,9999)) );
    COMMIT; -- commit here or after the loop or
    -- after the block PL/SQL
  END LOOP; 
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

/*Indexations*/
