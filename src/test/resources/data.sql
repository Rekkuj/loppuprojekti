INSERT INTO GROUPS (GROUPNAME, MISSIONSCORES) values ('Kettupoppoo', 250);
INSERT INTO GROUPS (GROUPNAME, TEACHERS, MISSIONSCORES) values ('Jermut', ('Tommi', 'Samu'), 20500);
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID, AUTHID) values ('Jermu', 'Teacher', 2000, 1, ('Himmeli', 'Helpperi'), 1, 'auth0|5b87943afe13090f5ffd652b');
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values ('Jouni4', 'Reagoija', 2000, 2, ('Himmeli'), 1);
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values ('Elmo', 'Kehittäjä', 2000, 2, ('Shakki', 'ElsaMaster'), 1);
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID, AUTHID) values ('Rekku', 'Testaaja', 2000, 2, ('Orvokki', 'Ruiskaunokki'), 1, '4');
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values ('Miika', 'Autentikoija', 2000, 2, ('Orvokki', 'Ruiskaunokki'), 1);
INSERT INTO USERS (USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values ('u', 'Uneksija', 2000, 1, ('ElsaMaster'), 1);
INSERT INTO MISSIONBUNDLES (BUNDLENAME, LISTOFMISSIONS) values ('Kettujutut', (25, 3));
INSERT INTO MISSIONBUNDLES (BUNDLENAME, LISTOFMISSIONS) values ('Jermujutut', (1,2,3));