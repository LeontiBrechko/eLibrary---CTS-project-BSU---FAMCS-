-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema eLibrary
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `eLibrary` ;

-- -----------------------------------------------------
-- Schema eLibrary
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `eLibrary` DEFAULT CHARACTER SET utf8 ;
USE `eLibrary` ;

-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK` (
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ISBN_13` VARCHAR(13) NOT NULL,
  `TITLE` VARCHAR(255) NOT NULL,
  `PUB_YEAR` INT NOT NULL,
  `DESC` VARCHAR(1000) NULL,
  `POPULARITY` DECIMAL(4,2) NULL,
  `IMAGE` VARCHAR(255) NULL,
  `THUMB` VARCHAR(255) NULL,
  PRIMARY KEY (`BOOK_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `BOOK_ID_UNIQUE` ON `eLibrary`.`BOOK` (`BOOK_ID` ASC);

CREATE UNIQUE INDEX `ISBN_UNIQUE` ON `eLibrary`.`BOOK` (`ISBN_13` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT` (
  `ACCOUNT_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `LOGIN` VARCHAR(255) NOT NULL,
  `EMAIL` VARCHAR(255) NOT NULL,
  `PASSWORD` VARCHAR(255) NOT NULL,
  `ROLE` ENUM('USER', 'ADMIN') NOT NULL,
  `CREATION_TIME` DATETIME NOT NULL,
  `STATE` ENUM('ACTIVE', 'FROZEN', 'CLOSED', 'TEMPORARY') NOT NULL,
  PRIMARY KEY (`ACCOUNT_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `ACCOUNT_ID_UNIQUE` ON `eLibrary`.`ACCOUNT` (`ACCOUNT_ID` ASC);

CREATE UNIQUE INDEX `EMAIL_UNIQUE` ON `eLibrary`.`ACCOUNT` (`EMAIL` ASC);

CREATE UNIQUE INDEX `LOGIN_UNIQUE` ON `eLibrary`.`ACCOUNT` (`LOGIN` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT_BOOK_LIST`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT_BOOK_LIST` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT_BOOK_LIST` (
  `ACCOUNT_ID` BIGINT(255) UNSIGNED NOT NULL,
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`ACCOUNT_ID`, `BOOK_ID`),
  CONSTRAINT `fk_ACCOUNT_BOOK_LIST_ACCOUNT1`
    FOREIGN KEY (`ACCOUNT_ID`)
    REFERENCES `eLibrary`.`ACCOUNT` (`ACCOUNT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ACCOUNT_BOOK_LIST_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ACCOUNT_BOOK_LIST_BOOK1_idx` ON `eLibrary`.`ACCOUNT_BOOK_LIST` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`LANGUAGE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`LANGUAGE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`LANGUAGE` (
  `LANG_ID` BIGINT(255) UNSIGNED NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`LANG_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `LANG_ID_UNIQUE` ON `eLibrary`.`LANGUAGE` (`LANG_ID` ASC);

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`LANGUAGE` (`NAME` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK_LANGUAGE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK_LANGUAGE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK_LANGUAGE` (
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  `LANG_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`BOOK_ID`, `LANG_ID`),
  CONSTRAINT `fk_BOOK_LANGUAGE_LANGUAGE1`
    FOREIGN KEY (`LANG_ID`)
    REFERENCES `eLibrary`.`LANGUAGE` (`LANG_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_LANGUAGE_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BOOK_LANGUAGE_LANGUAGE1_idx` ON `eLibrary`.`BOOK_LANGUAGE` (`LANG_ID` ASC);

CREATE INDEX `fk_BOOK_LANGUAGE_BOOK1_idx` ON `eLibrary`.`BOOK_LANGUAGE` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`AUTHOR`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`AUTHOR` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`AUTHOR` (
  `AUTH_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` VARCHAR(255) NOT NULL,
  `LAST_NAME` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`AUTH_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `AUTH_ID_UNIQUE` ON `eLibrary`.`AUTHOR` (`AUTH_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK_AUTHOR`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK_AUTHOR` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK_AUTHOR` (
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  `AUTH_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`BOOK_ID`, `AUTH_ID`),
  CONSTRAINT `fk_BOOK_AUTHOR_AUTHOR1`
    FOREIGN KEY (`AUTH_ID`)
    REFERENCES `eLibrary`.`AUTHOR` (`AUTH_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_AUTHOR_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BOOK_AUTHOR_BOOK1_idx` ON `eLibrary`.`BOOK_AUTHOR` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ADDRESS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ADDRESS` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ADDRESS` (
  `ADDRESS_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `COUNTRY` VARCHAR(255) NOT NULL,
  `STATE` VARCHAR(255) NULL,
  `CITY` VARCHAR(255) NOT NULL,
  `STREET_NUM` INT NULL,
  `STREET_NAME` VARCHAR(255) NULL,
  PRIMARY KEY (`ADDRESS_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `ADDRESS_ID_UNIQUE` ON `eLibrary`.`ADDRESS` (`ADDRESS_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`PUBLISHER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`PUBLISHER` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`PUBLISHER` (
  `PUB_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL,
  `ADDRESS_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`PUB_ID`),
  CONSTRAINT `fk_PUBLISHER_ADDRESS1`
    FOREIGN KEY (`ADDRESS_ID`)
    REFERENCES `eLibrary`.`ADDRESS` (`ADDRESS_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`PUBLISHER` (`NAME` ASC);

CREATE UNIQUE INDEX `PUB_ID_UNIQUE` ON `eLibrary`.`PUBLISHER` (`PUB_ID` ASC);

CREATE INDEX `fk_PUBLISHER_ADDRESS1_idx` ON `eLibrary`.`PUBLISHER` (`ADDRESS_ID` ASC);

CREATE UNIQUE INDEX `ADDRESS_ID_UNIQUE` ON `eLibrary`.`PUBLISHER` (`ADDRESS_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK_PUBLISHER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK_PUBLISHER` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK_PUBLISHER` (
  `PUB_ID` BIGINT(255) UNSIGNED NOT NULL,
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`PUB_ID`, `BOOK_ID`),
  CONSTRAINT `fk_BOOK_PUBLISHER_PUBLISHER1`
    FOREIGN KEY (`PUB_ID`)
    REFERENCES `eLibrary`.`PUBLISHER` (`PUB_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_PUBLISHER_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BOOK_PUBLISHER_BOOK1_idx` ON `eLibrary`.`BOOK_PUBLISHER` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`CATEGORY` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`CATEGORY` (
  `CATEGORY_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL,
  `DESC` VARCHAR(1000) NULL,
  PRIMARY KEY (`CATEGORY_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `CATEGORY_ID_UNIQUE` ON `eLibrary`.`CATEGORY` (`CATEGORY_ID` ASC);

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`CATEGORY` (`NAME` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK_CATEGORY` (
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  `CATEGORY_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`BOOK_ID`, `CATEGORY_ID`),
  CONSTRAINT `fk_BOOK_CATEGORY_CATEGORY1`
    FOREIGN KEY (`CATEGORY_ID`)
    REFERENCES `eLibrary`.`CATEGORY` (`CATEGORY_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_CATEGORY_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BOOK_CATEGORY_BOOK1_idx` ON `eLibrary`.`BOOK_CATEGORY` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`FORMAT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`FORMAT` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`FORMAT` (
  `FORMAT_ID` BIGINT(255) UNSIGNED NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`FORMAT_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`FORMAT` (`NAME` ASC);

CREATE UNIQUE INDEX `FORMAT_ID_UNIQUE` ON `eLibrary`.`FORMAT` (`FORMAT_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`BOOK_FORMAT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`BOOK_FORMAT` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`BOOK_FORMAT` (
  `BOOK_ID` BIGINT(255) UNSIGNED NOT NULL,
  `FORMAT_ID` BIGINT(255) UNSIGNED NOT NULL,
  `PATH` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`BOOK_ID`, `FORMAT_ID`),
  CONSTRAINT `fk_BOOK_FORMAT_BOOK1`
    FOREIGN KEY (`BOOK_ID`)
    REFERENCES `eLibrary`.`BOOK` (`BOOK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_FORMAT_FORMAT1`
    FOREIGN KEY (`FORMAT_ID`)
    REFERENCES `eLibrary`.`FORMAT` (`FORMAT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BOOK_FORMAT_FORMAT1_idx` ON `eLibrary`.`BOOK_FORMAT` (`FORMAT_ID` ASC);

CREATE UNIQUE INDEX `PATH_UNIQUE` ON `eLibrary`.`BOOK_FORMAT` (`PATH` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`USER` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`USER` (
  `USER_ID` BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` VARCHAR(255) NOT NULL,
  `LAST_NAME` VARCHAR(45) NOT NULL,
  `ACCOUNT_ID` BIGINT(255) UNSIGNED NOT NULL,
  PRIMARY KEY (`USER_ID`),
  CONSTRAINT `fk_USER_ACCOUNT1`
    FOREIGN KEY (`ACCOUNT_ID`)
    REFERENCES `eLibrary`.`ACCOUNT` (`ACCOUNT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `eLibrary`.`USER` (`USER_ID` ASC);

CREATE INDEX `fk_USER_ACCOUNT1_idx` ON `eLibrary`.`USER` (`ACCOUNT_ID` ASC);

CREATE UNIQUE INDEX `ACCOUNT_ID_UNIQUE` ON `eLibrary`.`USER` (`ACCOUNT_ID` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK` (`BOOK_ID`, `ISBN_13`, `TITLE`, `PUB_YEAR`, `DESC`, `POPULARITY`, `IMAGE`, `THUMB`) VALUES (1, '9781593272203', 'The Linux Programming Interface: A Linux and UNIX System Programming Handbook', 2010, '/catalog/books/9781593272203/desc.txt', NULL, '/catalog/books/9781593272203/image.jpg', '/catalog/books/9781593272203/thumb.jpg');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`LANGUAGE`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`LANGUAGE` (`LANG_ID`, `NAME`) VALUES (0, 'ENGLISH');
INSERT INTO `eLibrary`.`LANGUAGE` (`LANG_ID`, `NAME`) VALUES (1, 'RUSSIAN');
INSERT INTO `eLibrary`.`LANGUAGE` (`LANG_ID`, `NAME`) VALUES (2, 'BELARUSIAN');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK_LANGUAGE`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK_LANGUAGE` (`BOOK_ID`, `LANG_ID`) VALUES (1, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`AUTHOR`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`AUTHOR` (`AUTH_ID`, `FIRST_NAME`, `LAST_NAME`) VALUES (1, 'Michael', 'Kerrisk');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK_AUTHOR`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK_AUTHOR` (`BOOK_ID`, `AUTH_ID`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`ADDRESS`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`ADDRESS` (`ADDRESS_ID`, `COUNTRY`, `STATE`, `CITY`, `STREET_NUM`, `STREET_NAME`) VALUES (1, 'USA', 'CA', 'San Francisco', 38, 'Ringold Street');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`PUBLISHER`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`PUBLISHER` (`PUB_ID`, `NAME`, `ADDRESS_ID`) VALUES (1, 'No Starch Press, Inc.', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK_PUBLISHER`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK_PUBLISHER` (`PUB_ID`, `BOOK_ID`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`CATEGORY`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`CATEGORY` (`CATEGORY_ID`, `NAME`, `DESC`) VALUES (1, 'Образование', 'В этой категории книг Вы можете найти множество полезной информации, которая поможет в изучении определенного предмета.');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK_CATEGORY`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK_CATEGORY` (`BOOK_ID`, `CATEGORY_ID`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`FORMAT`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`FORMAT` (`FORMAT_ID`, `NAME`) VALUES (2, 'DJVU');
INSERT INTO `eLibrary`.`FORMAT` (`FORMAT_ID`, `NAME`) VALUES (1, 'EPUB');
INSERT INTO `eLibrary`.`FORMAT` (`FORMAT_ID`, `NAME`) VALUES (3, 'FB2');
INSERT INTO `eLibrary`.`FORMAT` (`FORMAT_ID`, `NAME`) VALUES (0, 'PDF');

COMMIT;


-- -----------------------------------------------------
-- Data for table `eLibrary`.`BOOK_FORMAT`
-- -----------------------------------------------------
START TRANSACTION;
USE `eLibrary`;
INSERT INTO `eLibrary`.`BOOK_FORMAT` (`BOOK_ID`, `FORMAT_ID`, `PATH`) VALUES (1, 0, '/catalog/books/9781593272203/The_Linux_Programming_Interface.pdf');

COMMIT;

