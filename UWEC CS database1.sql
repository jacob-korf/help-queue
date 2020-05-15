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


insert into login (username, password1) values ('username', 'password'); 
insert into login (username, password1) values ('hello', 'name'); 
insert into login (username, password1) values ('person', 'pass'); 

 