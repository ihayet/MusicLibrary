CREATE TABLE  "ARTISTTABLE" 
   (	"ARTISTID" NUMBER(12,0), 
	"NAMEOFARTIST" VARCHAR2(30) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(255), 
	"ORIGIN" VARCHAR2(30), 
	"GENRES" VARCHAR2(30) NOT NULL ENABLE, 
	"YEARSACTIVE" VARCHAR2(30), 
	 PRIMARY KEY ("ARTISTID") ENABLE
   );



CREATE TABLE  "ALBUMTABLE" 
   (	"ALBUMID" NUMBER(12,0), 
	"NAMEOFALBUM" VARCHAR2(30) NOT NULL ENABLE, 
    "ARTISTID" NUMBER(12,0) NOT NULL ENABLE,
	"YEAROFRELEASE" NUMBER(4,0) NOT NULL ENABLE, 
	 CONSTRAINT "PK_ALBUM" PRIMARY KEY ("ALBUMID", "ARTISTID") ENABLE, 
	 CONSTRAINT "FK_ALBUMTOARTIST" FOREIGN KEY ("ARTISTID")
	  REFERENCES  "ARTISTTABLE" ("ARTISTID") ON DELETE CASCADE ENABLE
   );



CREATE TABLE  "SONGTABLE" 
(	
"SONGID" NUMBER(12,0), 
"ALBUMID" NUMBER(12,0) NOT NULL ENABLE, 
"ARTISTID" NUMBER(12,0) NOT NULL ENABLE, 
"SONGNAME" VARCHAR2(30) NOT NULL ENABLE, 
"LENGTH" VARCHAR2(5) NOT NULL ENABLE, 
"PRIORITY" NUMBER(12,0),
 CONSTRAINT "PK" PRIMARY KEY ("SONGID", "ALBUMID", "ARTISTID") ENABLE, 
 CONSTRAINT "FK_SONGTOALBUMARTIST" FOREIGN KEY ("ALBUMID", "ARTISTID")
 REFERENCES  "ALBUMTABLE" ("ALBUMID", "ARTISTID") ON DELETE CASCADE ENABLE
);

CREATE TABLE themeTable
(
themeID NUMBER(2) PRIMARY KEY,
themeName VARCHAR2(30) NOT NULL,
image VARCHAR2(30) NOT NULL,
css VARCHAR2(30) NOT NULL,
background VARCHAR2(30),
foreground VARCHAR2(30),
foreground1 VARCHAR2(30),
border VARCHAR2(30)
);


CREATE TABLE userInfo
(
userID NUMBER(12),
themeID NUMBER(2) NOT NULL,
nameOfUser VARCHAR2(30) NOT NULL,
username VARCHAR2(30) NOT NULL,
password VARCHAR2(30) NOT NULL,
dob DATE,
sex VARCHAR2(1),
country VARCHAR2(30),
about VARCHAR2(255),
PRIMARY KEY ("USERID") ENABLE,
CONSTRAINT fk_userInfo
FOREIGN KEY (themeID) REFERENCES themeTable(themeID)
);

CREATE TABLE playList
(
playlistID NUMBER(12,0),
name VARCHAR2(30),
userID NUMBER(12,0) NOT NULL ENABLE,
CONSTRAINT "PK_PLAYLIST" PRIMARY KEY(playlistID,userID),
CONSTRAINT "FK_USER" FOREIGN KEY ("USERID")
REFERENCES "USERINFO" ("USERID") ON DELETE CASCADE ENABLE
);

create table pcontainss
(
 playlistID NUMBER(12,0),
 userID NUMBER(12,0),
 artistID NUMBER(12,0) not null enable,
 albumID NUMBER(12,0) not null enable,
 songID NUMBER(12,0) not null enable,
 constraint fk_playlist_songs foreign key (playlistID,userID) references playlist (playlistID,userID),
 constraint fk_playlist_artist_album_song foreign key (artistid,albumid,songid) references songtable (artistid,albumid,songid)
);

create table currentPlaylist
(
 artistID NUMBER(12,0) not null enable,
 albumID NUMBER(12,0) not null enable,
 songID NUMBER(12,0) not null enable,
 constraint fk_playlist_artist_album_song foreign key (artistid,albumid,songid) references songtable (artistid,albumid,songid)
);

create table currentRecommendation
(
 artistID NUMBER(12,0) not null enable,
 albumID NUMBER(12,0) not null enable,
 songID NUMBER(12,0) not null enable,
 constraint fk_playlist_artist_album_song foreign key (artistid,albumid,songid) references songtable (artistid,albumid,songid)
);