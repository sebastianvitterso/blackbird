CREATE TABLE `bruker` (
  `bruker_ID` int(11) NOT NULL,
  `rolle` varchar(45) NOT NULL,
  `brukernavn` varchar(45) DEFAULT NULL,
  `passord` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bruker_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fag` (
  `fagkode` varchar(10) NOT NULL,
  PRIMARY KEY (`fagkode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `saltid` (
  `saltid_ID` int(11) NOT NULL,
  `tidspunkt` timestamp NOT NULL,
  `plasser` int(11) NOT NULL,
  `fagkode` varchar(10) NOT NULL,
  PRIMARY KEY (`saltid_ID`),
  KEY `saltid_fk1_idx` (`fagkode`),
  CONSTRAINT `saltid_fk1` FOREIGN KEY (`fagkode`) REFERENCES `fag` (`fagkode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lærerifag` (
  `lærer_id` int(11) NOT NULL,
  `fagkode` varchar(10) NOT NULL,
  PRIMARY KEY (`lærer_id`,`fagkode`),
  KEY `elevifag_fk2_idx` (`fagkode`),
  CONSTRAINT `lærerifag_fk1` FOREIGN KEY (`lærer_id`) REFERENCES `bruker` (`bruker_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lærerifag_fk2` FOREIGN KEY (`fagkode`) REFERENCES `fag` (`fagkode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `assifag` (
  `ass_id` int(11) NOT NULL,
  `fagkode` varchar(10) NOT NULL,
  PRIMARY KEY (`ass_id`,`fagkode`),
  KEY `elevifag_fk2_idx` (`fagkode`),
  CONSTRAINT `assifag_fk10` FOREIGN KEY (`ass_id`) REFERENCES `bruker` (`bruker_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `assifag_fk20` FOREIGN KEY (`fagkode`) REFERENCES `fag` (`fagkode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `elevifag` (
  `elev_id` int(11) NOT NULL,
  `fagkode` varchar(10) NOT NULL,
  PRIMARY KEY (`elev_id`,`fagkode`),
  KEY `elevifag_fk2_idx` (`fagkode`),
  CONSTRAINT `elevifag_fk1` FOREIGN KEY (`elev_id`) REFERENCES `bruker` (`bruker_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `elevifag_fk2` FOREIGN KEY (`fagkode`) REFERENCES `fag` (`fagkode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `lærerassistentpåsal` (
  `laps_ID` int(11) NOT NULL,
  `studassID` int(11) NOT NULL,
  `saltidID` int(11) NOT NULL,
  PRIMARY KEY (`laps_ID`),
  KEY `saltidID_idx` (`saltidID`),
  CONSTRAINT `laps_fk` FOREIGN KEY (`saltidID`) REFERENCES `saltid` (`saltid_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `booking` (
  `booking_ID` int(11) NOT NULL,
  `laps_ID` int(11) NOT NULL,
  `stud_ID` int(11) NOT NULL,
  PRIMARY KEY (`booking_ID`),
  KEY `booking_fk_idx` (`laps_ID`),
  CONSTRAINT `booking_fk` FOREIGN KEY (`laps_ID`) REFERENCES `lærerassistentpåsal` (`laps_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO bruker VALUES (1, "faglærer", "ola", "passord1");
INSERT INTO bruker VALUES (2, "la", "kari", "passord1");
INSERT INTO bruker VALUES (3, "student", "nils", "passord1");

INSERT INTO fag VALUES ("tdt4100");

INSERT INTO lærerifag VALUES (1,"tdt4100");
INSERT INTO assifag VALUES (2,"tdt4100");
INSERT INTO elevifag VALUES (3,"tdt4100");

INSERT INTO saltid VALUES (1, "2019-01-01 00:00:00", 4, "tdt4100");

INSERT INTO lærerassistentpåsal VALUES (1, 2, 1);

INSERT INTO booking VALUES (1, 1, 3);
