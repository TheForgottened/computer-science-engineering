/* NECESSARY FOR DATE-TYPE COLUMNS */
SET GLOBAL time_zone = '+0:00';

DROP DATABASE IF EXISTS metapd;
CREATE DATABASE metapd;
USE metapd;

/*
    MAIN TABLES
*/
CREATE TABLE IF NOT EXISTS user (
    username VARCHAR(64) NOT NULL,
    name VARCHAR(64),
    password VARCHAR(32) NOT NULL,
    status TINYINT NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS chat_group (
    group_id INT NOT NULL AUTO_INCREMENT,
    manager_username VARCHAR(64) NOT NULL,
    group_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (group_id),
    FOREIGN KEY (manager_username)
        REFERENCES user(username)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS message (
    message_id INT NOT NULL AUTO_INCREMENT,
    sender_username VARCHAR(64) NOT NULL,
    text VARCHAR(1024),
    file_name VARCHAR(1024),
    send_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (message_id),
    FOREIGN KEY (sender_username)
        REFERENCES user(username)
        ON DELETE CASCADE
);

/*
    RELATIONSHIP TABLES
*/
CREATE TABLE IF NOT EXISTS part_of (
    username VARCHAR(64) NOT NULL,
    group_id INT NOT NULL,
	accepted TINYINT NOT NULL,
    FOREIGN KEY (username)
        REFERENCES user(username)
        ON DELETE CASCADE,
    FOREIGN KEY (group_id)
        REFERENCES chat_group(group_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS group_receives (
    group_id INT NOT NULL,
    message_id INT NOT NULL,
    FOREIGN KEY (group_id)
        REFERENCES chat_group(group_id)
        ON DELETE CASCADE,
    FOREIGN KEY (message_id)
        REFERENCES message(message_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_receives (
    username VARCHAR(64) NOT NULL,
    message_id INT NOT NULL,
    FOREIGN KEY (username)
        REFERENCES user(username)
        ON DELETE CASCADE,
    FOREIGN KEY (message_id)
        REFERENCES message(message_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contact_of (
	requester_username VARCHAR(64) NOT NULL,
	receiver_username VARCHAR(64) NOT NULL,
	accepted TINYINT NOT NULL,
	FOREIGN KEY (requester_username)
        REFERENCES user(username)
        ON DELETE CASCADE,
    FOREIGN KEY (requester_username)
        REFERENCES user(username)
        ON DELETE CASCADE
);

/*
    PRIVILEGES
*/
DROP USER IF EXISTS 'metapd_server';
CREATE USER IF NOT EXISTS 'metapd_server' IDENTIFIED BY 'metapd-123*';
GRANT SELECT, INSERT, UPDATE, DELETE ON metapd.* TO 'metapd_server';

FLUSH PRIVILEGES;