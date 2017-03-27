
--CREATION DES TABLES

CREATE TABLE T1 (
  id NUMBER PRIMARY KEY,
  name VARCHAR(15)
)

DROP TABLE T3;
CREATE TABLE T3 (
  id NUMBER PRIMARY KEY,
  text VARCHAR(20),
  idT1 NUMBER,
  FOREIGN KEY (idT1) REFERENCES T1(id) 
)

CREATE TABLE T2 (
  id NUMBER PRIMARY KEY,
  text VARCHAR(20),
  idT1 NUMBER,
  FOREIGN KEY (idT1) REFERENCES T1(id) 
)

CREATE TABLE Tbitmap(
  id NUMBER PRIMARY KEY,
  name VARCHAR(100)
)

--SEQUENCES

  DROP SEQUENCE cptT1;
  DROP SEQUENCE cptT2;
  DROP SEQUENCE cptT3;
  DROP SEQUENCE cptT4;


  CREATE SEQUENCE cptT4
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
  /
  CREATE SEQUENCE cptT1
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
  /
  CREATE SEQUENCE cptT2
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
  /
  CREATE SEQUENCE cptT3
  INCREMENT BY 1
  START WITH 1
  NOCYCLE;
  /
  
-- INSERTIONS
BEGIN
  FOR x IN 1 .. 10000 LOOP
    INSERT INTO T1 (id,name)  VALUES (cptT1.nextVal,'name'|| cptT1.currval);
  END LOOP;
  COMMIT;
END;

BEGIN
  FOR x IN 1 .. 100000 LOOP
    INSERT INTO T2 (id,text,idT1)  VALUES (cptT1.nextVal,'text'|| cptT1.currval,ROUND(DBMS_RANDOM.VALUE(1,10000)));
    INSERT INTO T3 (id,text,idT1)  VALUES (cptT1.nextVal,'text'|| cptT1.currval,ROUND(DBMS_RANDOM.VALUE(1,10000)));
  END LOOP;
  COMMIT;
END;

BEGIN
  FOR x IN 1 .. 10000 LOOP
    INSERT INTO Tbitmap (id,name)  VALUES (cptT4.nextVal,ROUND(DBMS_RANDOM.VALUE(1,4)));
  END LOOP;
  COMMIT;
END;

-- CREATION DES INDEXS
set autotrace EXPLAIN
set timing on

SELECT name FROM T1 WHERE id = 'name96';
DROP INDEX name;
CREATE INDEX name ON T1 (name);

DROP INDEX indexEtr2;
DROP INDEX indexEtr3;
DROP INDEX name1;
DROP INDEX BIT;

CREATE INDEX name1 ON T1 (name);
CREATE INDEX indexEtr2 ON T2 (idT1);
CREATE INDEX indexEtr3 ON T3 (idT1);
CREATE BITMAP INDEX BIT ON Tbitmap (id); -- n'est pas utilisé
CREATE BITMAP INDEX BIT ON Tbitmap (name); -- est utilisé

SELECT * FROM TBITMAP WHERE name='1';

SELECT t2.idT1,t3.idT1 FROM T2 t2,T3 t3 WHERE t2.idT1 = t3.idT1;

SELECT t2.idT1,t3.idT1 FROM T2 t2,T3 t3 , T1 t1 WHERE t2.idT1 = t3.idT1 AND  t1.id = '58';

--| Id  | Operation              | Name          | Rows  | Bytes |TempSpc| Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT       |               |  1349K|    50M|       |   476   (2)| 00:00:01 |
|*  1 |  HASH JOIN             |               |  1349K|    50M|  3272K|   476   (2)| 00:00:01 |
|   2 |   NESTED LOOPS         |               | 88101 |  2236K|       |    63   (0)| 00:00:01 |
|*  3 |    INDEX UNIQUE SCAN   | SYS_C00114493 |     1 |    13 |       |     1   (0)| 00:00:01 |
|   4 |    INDEX FAST FULL SCAN| INDEXETR3     | 88101 |  1118K|       |    62   (0)| 00:00:01 |
|   5 |   INDEX FAST FULL SCAN | INDEXETR2     |   148K|  1881K|       |    74   (0)| 00:00:01 |

SELECT t2.idT1,t3.idT1 FROM T2 t2,T3 t3 , T1 t1 WHERE t2.idT1 = t3.idT1 AND  t1.name = 'name58';

--
| Id  | Operation               | Name      | Rows  | Bytes |TempSpc| Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT        |           |  1349K|    45M|       |   460   (2)| 00:00:01 |
|*  1 |  HASH JOIN              |           |  1349K|    45M|  2928K|   460   (2)| 00:00:01 |
|   2 |   MERGE JOIN CARTESIAN  |           | 88101 |  1892K|       |    63   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN     | NAME1     |     1 |     9 |       |     1   (0)| 00:00:01 |
|   4 |    BUFFER SORT          |           | 88101 |  1118K|       |    62   (0)| 00:00:01 |
|   5 |     INDEX FAST FULL SCAN| INDEXETR3 | 88101 |  1118K|       |    62   (0)| 00:00:01 |
|   6 |   INDEX FAST FULL SCAN  | INDEXETR2 |   148K|  1881K|       |    74   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------

SELECT * FROM TBITMAP WHERE name='1';

-----------------------------------------------------------------------------------------------
| Id  | Operation                           | Name    | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                    |         |  1634 |   103K|     5   (0)| 00:00:01 |
|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| TBITMAP |  1634 |   103K|     5   (0)| 00:00:01 |
|   2 |   BITMAP CONVERSION TO ROWIDS       |         |       |       |            |          |
|*  3 |    BITMAP INDEX SINGLE VALUE        | BIT     |       |       |            |          |
-----------------------------------------------------------------------------------------------
 
