DROP TABLE users;
CREATE TABLE users (
  id int(10) unsigned NOT NULL auto_increment,
  name varchar(255) NOT NULL,
  profession varchar(255) NOT NULL,
  employer varchar(255) NOT NULL,
  PRIMARY KEY  (id)
);




/*-----------------GENERATOR-------------------*/
drop procedure if exists generator;

delimiter #
create procedure generator()
begin

declare v_max int unsigned default 500000;
declare v_counter int unsigned default 0;

  truncate table users;
  start transaction;
  while v_counter < v_max do
    insert into users (name,profession,employer) VALUES (concat('name',v_counter),'profession2','type2');
    insert into users (name,profession,employer) VALUES (concat('name',v_counter),'profession1','type1');
    insert into users (name,profession,employer) VALUES (concat('name',v_counter),'profession3','type3');
    insert into users (name,profession,employer) VALUES (concat('name',v_counter),'profession4','type4');
    insert into users (name,profession,employer) VALUES (concat('name',v_counter),'profession5','type4');
    set v_counter=v_counter+1;
  end while;
  commit;
end #

delimiter ;


/*------------------------------------------*/
call generator();



/***********************TEST*******************/
DROP INDEX test_generator ON users;
set profiling=1;
SELECT u.profession FROM users u GROUP BY profession ;
SELECT DISTINCT u.profession FROM users u  ;
show profiles;


CREATE INDEX test_generator ON users (profession);

SELECT DISTINCT u.profession FROM users u  ;
SELECT u.profession FROM users u GROUP BY profession ;
show profiles;

/*
Query_ID	Duration	Query
1	1.19743275	SELECT u.profession FROM users u GROUP BY profession
2	1.13439250	SELECT DISTINCT u.profession FROM users u
3	4.13953100	CREATE INDEX test_generator ON users (profession)
4	0.00018425	SELECT DISTINCT u.profession FROM users u
5	0.00014025	SELECT u.profession FROM users u GROUP BY profession
*/
/*INDEX EFFICACE*/


