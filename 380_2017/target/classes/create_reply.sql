CREATE TABLE Reply (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
replyname VARCHAR(100) NOT NULL,
refId INTEGER NOT NULL,
body VARCHAR(200) NOT NULL,
primary key (id)
);