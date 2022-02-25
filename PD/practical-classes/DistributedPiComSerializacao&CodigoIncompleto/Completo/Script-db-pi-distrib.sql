SET GLOBAL time_zone = '+0:00';

CREATE DATABASE IF NOT EXISTS pi_distrib_db;
USE pi_distrib_db;

DROP TABLE IF EXISTS pi_workers;

CREATE TABLE IF NOT EXISTS pi_workers (
  /*id INT NOT NULL AUTO_INCREMENT,*/
  address VARCHAR(100) NOT NULL,
  port INT NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (address, port)
);

/*
LOCK TABLES pi_workers WRITE;
INSERT INTO pi_workers (address, port) VALUES ('localhost',5002);
INSERT INTO pi_workers (address, port) VALUES ('127.0.0.1',5003);
UNLOCK TABLES;
*/

DROP USER IF EXISTS 'piworker'@'%';
CREATE USER IF NOT EXISTS 'piworker'@'10.10.10.10' IDENTIFIED BY 'w-pass-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON pi_distrib_db.* TO `piworker`@`%`;

DROP USER IF EXISTS 'piworker'@'localhost';
CREATE USER IF NOT EXISTS `piworker`@`localhost` IDENTIFIED BY 'w-pass-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON pi_distrib_db.* TO 'piworker'@'localhost';

DROP USER IF EXISTS 'pimaster'@'%';
CREATE USER IF NOT EXISTS `pimaster`@`%` IDENTIFIED BY 'm-pass-123';
GRANT SELECT, DELETE ON pi_distrib_db.* TO 'pimaster'@'%';

DROP USER IF EXISTS 'pimaster'@'localhost';
CREATE USER IF NOT EXISTS `pimaster`@`localhost` IDENTIFIED BY 'm-pass-123';
GRANT SELECT, DELETE ON pi_distrib_db.* TO 'pimaster'@'localhost';

FLUSH PRIVILEGES;

SELECT user, host FROM mysql.user WHERE user LIKE 'pi%';

SHOW GRANTS FOR 'piworker'@'%';
SHOW GRANTS FOR 'pimaster'@'%';
SHOW GRANTS FOR 'piworker'@'localhost';
SHOW GRANTS FOR 'pimaster'@'localhost';

/*select * from pi_workers;

SELECT SLEEP(2);*/ /* Aguarda 2 segundos antes de provocar o update do timestamp num dos registos */

/*UPDATE pi_workers SET timestamp=CURRENT_TIMESTAMP WHERE address LIKE '%127.0.0.1%' AND port=5001;
select * from pi_workers;*/
