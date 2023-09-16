-- create table manually without ddl
CREATE TABLE `tabel_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `nama` varchar(255) NOT NULL,
  `nomor` int NOT NULL,
  PRIMARY KEY (`id`)
);

-- view example
CREATE VIEW `view_test` AS
    SELECT * FROM tabel_test

-- stored procedure example
CREATE PROCEDURE `concatNumberAndString`(IN numberIn int, IN stringIn varchar(255), OUT output varchar(255))
BEGIN
	SET output = CONCAT(numberIn, stringIn);
END

-- call stored procedure
SET @output = "";
CALL concatNumberAndString(1, " randomString", @output);
SELECT @output;