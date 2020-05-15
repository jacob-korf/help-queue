drop table helpRequest; 
drop table calendar; 
--drop table login; 
drop sequence help_seq; 
drop sequence cal_seq; 

create table login(
username varchar(15), 
password1 varchar(15)
);

create sequence help_seq start with 1 increment by 1; 

Create table helpRequest(
unique_ID NUMBER(10) NOT NULL,
event varchar (50),
workStation varchar(50), 
originator varchar(50),
course_number varchar (50),
section int,
request_time  date,
cancel_time  date, 
wait_time interval day to second, 
Primary key (unique_id)
);
--alter table helpRequest add( constraint help_pk primary key (unique_ID));

create sequence cal_seq start with 1 increment by 1; 
create table calendar(
unique_ID Number(10) NOT NUll,
courseNumber varchar(50), 
sectionNumber int,
startDate date,
endDate date, 
startTime date, 
endTime date, 
daysOfTheWeek varchar(21)
);

--insert into helpRequest (unique_ID, workStation, request_time, wait_time) values (1,'insert',  TO_DATE('Dec 5, 2020', 'MMM DD, YYYY'), interval '0 2:34:26' day to Second); 
--insert into calendar (courseNumber, sectionNumber, startDate, endDate, startTime, endTime) values ('cs 145', '1', to_date('12' , 'may 21st 2020', '3', '5');
--INSERT INTO calendar (courseNumber, sectionNumber, startDate, endDate, startTime, endTime, daysOfTheWeek) values ('cs1', 4,   TO_DATE('12/05/2000', 'MM/dd/yyyy'),
--TO_DATE('12/10/2000', 'MM/dd/yyyy'), TO_DATE('14:00', 'HH24:MI'), TO_DATE('18:00', 'HH24:MI'), 'T' );

--insert into helpRequest (unique_ID, workStation) values (help_seq.nextval, 'person');  

--SELECT event, originator, course_number, section, workStation, TO_CHAR(request_time, 'DD MONTH YY HH:MI:SS') AS request_time, TO_CHAR(cancel_time, 'DD MONTH YY HH:MI:SS') AS cancel_time  FROM helpRequest;

--Select courseNumber, sectionNumber, daysoftheweek, TO_CHAR(StartDate, 'DD MONTH YY HH:MI:SS') AS startDate, TO_CHAR(endDate, 'DD MONTH YY HH:MI:SS') AS endDate, TO_CHAR(startTime, 'DD MONTH YY HH:MI:SS') AS startTime, TO_CHAR(endTime, 'DD MONTH YY HH:MI:SS') AS endTime from calendar; 

--SELECT courseNumber FROM calendar WHERE DATEPART(hh, startTime) <= 'startTime' and DATEPART(hh, endTime) >= 'endTime' and daysOfTheWeek LIKE '%Web%'; 

--SELECT courseNumber FROM calendar WHERE to_char(startTime, 'HH') <= '10' AND to_char(endTime, 'HH') >= '10' and daysOfTheWeek LIKE '%Wed%'; 

--Select courseNumber FROM calendar WHERE sectionNumber = 4; 

insert into login (username, password1) values ('username', 'password'); 
insert into login (username, password1) values ('hello', 'name'); 
insert into login (username, password1) values ('person', 'pass'); 

SELECT username, password1 FROM login where username = 'username' AND password1 = 'password'; 
 
select * from calendar; 
select * from helpRequest; 
select * from login; 