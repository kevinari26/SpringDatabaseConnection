-- create table manually without ddl
CREATE TABLE `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `nama` varchar(255) NOT NULL,
  `nomor` int NOT NULL,
  PRIMARY KEY (`id`)
);
-- insert dummy data
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (1,'2022-12-04 15:33:42','kevin',1000);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (2,'2022-12-02 15:33:42','kevin',200);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (3,'2022-12-29 15:33:42','w',500);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (4,'2022-12-05 15:33:42','w',700);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (5,'2022-12-06 15:33:42','qwe',123);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (6,'2022-12-14 15:33:42','asd',634);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (7,'2022-12-29 15:33:42','gfd',2345);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (8,'2022-12-14 15:33:42','asd',700);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (9,'2022-12-26 15:33:42','kjhgfd',500);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (10,'2022-12-07 15:33:42','qweyt',563);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (11,'2022-12-05 15:33:42','gfed',500);
INSERT INTO `person` (`id`,`created_date`,`nama`,`nomor`) VALUES (12,'2022-12-05 15:33:42','henry v',100);

-- view example
CREATE VIEW `view_person` AS
    SELECT * FROM person
-- call view
SELECT * FROM view_person

-- stored procedure example
CREATE PROCEDURE `concatNumberAndString`(IN numberIn int, IN stringIn varchar(255), OUT output varchar(255))
BEGIN
	SET output = CONCAT(numberIn, stringIn);
END
-- call stored procedure
SET @output = "";
CALL concatNumberAndString(1, " randomString", @output);
SELECT @output;