create table user(
	userID 		int not null,
    name		varchar(40)
);


INSERT INTO user values(1, "Sebastian");
INSERT INTO user values(2, "Patrik");
INSERT INTO user values(3, "Beatrice");
INSERT INTO user values(4, "Eivind");
INSERT INTO user values(5, "Francis");
INSERT INTO user values(6, "Eric");


select * from user;

create table person(
	PID 		int not null,
    name		varchar(40),
    birthDate	varchar(10)
);

insert into person values(1, "seb", "03.02.1995");