CREATE TABLE if not exists groups (
    groupid int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    groupname varchar NOT NULL,
    teachers array,
    pupils array,
    missionscores int
);
CREATE UNIQUE INDEX groups_groupname_uindex ON groups (groupname);

CREATE TABLE if not exists users (
    id int AUTO_INCREMENT PRIMARY KEY,
    username varchar NOT NULL,
    role varchar,
    points int,
    completedmissions array,
    groupid int,
    contactpersonuserid int,
    authid varchar,
    CONSTRAINT USERS_GROUPS_GROUPID_fk FOREIGN KEY (groupid) REFERENCES GROUPS (GROUPID),
	  CONSTRAINT USERS_USERS_ID_fk FOREIGN KEY (CONTACTPERSONUSERID) REFERENCES USERS (ID)
);
CREATE UNIQUE INDEX users_username_uindex ON users (username);

CREATE TABLE if not exists missionbundles (
    bundleid int AUTO_INCREMENT PRIMARY KEY,
    belongstogroups array,
    listofmissions array,
    bundlename varchar
);
CREATE UNIQUE INDEX missionbundles_bundlename_uindex ON missionbundles (bundlename);