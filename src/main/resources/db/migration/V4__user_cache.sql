-- USER_CACHE
CREATE TABLE USER_CACHE (
    ID_USER_CACHE NUMBER NOT NULL AUTO_INCREMENT,
    USER_ID VARCHAR2(255) NOT NULL,
    LOGIN VARCHAR2(255) NOT NULL,
    AVATAR_URL VARCHAR2(255) NOT NULL
);

ALTER TABLE USER_CACHE
ADD CONSTRAINT PK_USER_CACHE PRIMARY KEY (ID_USER_CACHE);

INSERT INTO USER_CACHE(USER_ID, LOGIN, AVATAR_URL) VALUES ('FAKE1', 'bertiecolt', 'https://image.flaticon.com/icons/png/512/168/168730.png');
INSERT INTO USER_CACHE(USER_ID, LOGIN, AVATAR_URL) VALUES ('FAKE2', 'goylegecko', 'https://image.flaticon.com/icons/png/512/168/168723.png');