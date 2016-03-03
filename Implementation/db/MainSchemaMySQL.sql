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
  `BOOK_ID` INT NOT NULL AUTO_INCREMENT,
  `ISBN` VARCHAR(45) NOT NULL,
  `TITLE` VARCHAR(255) NOT NULL,
  `PUB_YEAR` INT NOT NULL,
  `DESC` VARCHAR(1000) NULL,
  `POPULARITY` DECIMAL(4,2) NULL,
  `IMAGE` VARCHAR(255) NULL,
  `THUMB` VARCHAR(255) NULL,
  PRIMARY KEY (`BOOK_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `BOOK_ID_UNIQUE` ON `eLibrary`.`BOOK` (`BOOK_ID` ASC);

CREATE UNIQUE INDEX `ISBN_UNIQUE` ON `eLibrary`.`BOOK` (`ISBN` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT_PASS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT_PASS` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT_PASS` (
  `LOGIN` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`LOGIN`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `LOGIN_UNIQUE` ON `eLibrary`.`ACCOUNT_PASS` (`LOGIN` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`USER` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`USER` (
  `USER_ID` INT NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` VARCHAR(255) NOT NULL,
  `LAST_NAME` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`USER_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `eLibrary`.`USER` (`USER_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT` (
  `ACCOUNT_ID` INT NOT NULL AUTO_INCREMENT,
  `EMAIL` VARCHAR(255) NOT NULL,
  `CREATION_TIME` DATETIME NOT NULL,
  `LOGIN` VARCHAR(45) NOT NULL,
  `USER_USER_ID` INT NOT NULL,
  PRIMARY KEY (`ACCOUNT_ID`),
  CONSTRAINT `fk_ACCOUNT_ACCOUNT_PASS`
    FOREIGN KEY (`LOGIN`)
    REFERENCES `eLibrary`.`ACCOUNT_PASS` (`LOGIN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ACCOUNT_USER1`
    FOREIGN KEY (`USER_USER_ID`)
    REFERENCES `eLibrary`.`USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `ACCOUNT_ID_UNIQUE` ON `eLibrary`.`ACCOUNT` (`ACCOUNT_ID` ASC);

CREATE UNIQUE INDEX `EMAIL_UNIQUE` ON `eLibrary`.`ACCOUNT` (`EMAIL` ASC);

CREATE INDEX `fk_ACCOUNT_ACCOUNT_PASS_idx` ON `eLibrary`.`ACCOUNT` (`LOGIN` ASC);

CREATE UNIQUE INDEX `LOGIN_UNIQUE` ON `eLibrary`.`ACCOUNT` (`LOGIN` ASC);

CREATE INDEX `fk_ACCOUNT_USER1_idx` ON `eLibrary`.`ACCOUNT` (`USER_USER_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ROLE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ROLE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ROLE` (
  `ROLE_ID` INT NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `ROLE_ID_UNIQUE` ON `eLibrary`.`ROLE` (`ROLE_ID` ASC);

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`ROLE` (`NAME` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT_ROLE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT_ROLE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT_ROLE` (
  `ACCOUNT_ACCOUNT_ID` INT NOT NULL,
  `ROLE_ROLE_ID` INT NOT NULL,
  PRIMARY KEY (`ACCOUNT_ACCOUNT_ID`, `ROLE_ROLE_ID`),
  CONSTRAINT `fk_ACCOUNT_ROLE_ROLE1`
    FOREIGN KEY (`ROLE_ROLE_ID`)
    REFERENCES `eLibrary`.`ROLE` (`ROLE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ACCOUNT_ROLE_ACCOUNT1`
    FOREIGN KEY (`ACCOUNT_ACCOUNT_ID`)
    REFERENCES `eLibrary`.`ACCOUNT` (`ACCOUNT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ACCOUNT_ROLE_ACCOUNT1_idx` ON `eLibrary`.`ACCOUNT_ROLE` (`ACCOUNT_ACCOUNT_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`STATE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`STATE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`STATE` (
  `STATE_ID` INT NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`STATE_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `NAME_UNIQUE` ON `eLibrary`.`STATE` (`NAME` ASC);

CREATE UNIQUE INDEX `STATE_ID_UNIQUE` ON `eLibrary`.`STATE` (`STATE_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT_STATE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT_STATE` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT_STATE` (
  `ACCOUNT_ACCOUNT_ID` INT NOT NULL,
  `STATE_STATE_ID` INT NOT NULL,
  PRIMARY KEY (`ACCOUNT_ACCOUNT_ID`, `STATE_STATE_ID`),
  CONSTRAINT `fk_ACCOUNT_STATE_ACCOUNT1`
    FOREIGN KEY (`ACCOUNT_ACCOUNT_ID`)
    REFERENCES `eLibrary`.`ACCOUNT` (`ACCOUNT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ACCOUNT_STATE_STATE1`
    FOREIGN KEY (`STATE_STATE_ID`)
    REFERENCES `eLibrary`.`STATE` (`STATE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ACCOUNT_STATE_STATE1_idx` ON `eLibrary`.`ACCOUNT_STATE` (`STATE_STATE_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`ACCOUNT_BOOK_LIST`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`ACCOUNT_BOOK_LIST` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`ACCOUNT_BOOK_LIST` (
  `ACCOUNT_ID` INT NOT NULL,
  `BOOK_ID` INT NOT NULL,
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
  `LANG_ID` INT NOT NULL,
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
  `BOOK_ID` INT NOT NULL,
  `LANG_ID` INT NOT NULL,
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

CREATE INDEX `fk_BOOK_LANGUAGE_BOOK1_idx` ON `eLibrary`.`BOOK_LANGUAGE` (`BOOK_ID` ASC);


-- -----------------------------------------------------
-- Table `eLibrary`.`AUTHOR`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `eLibrary`.`AUTHOR` ;

CREATE TABLE IF NOT EXISTS `eLibrary`.`AUTHOR` (
  `AUTH_ID` INT NOT NULL AUTO_INCREMENT,
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
  `BOOK_ID` INT NOT NULL,
  `AUTH_ID` INT NOT NULL,
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
  `ADDRESS_ID` INT NOT NULL AUTO_INCREMENT,
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
  `PUB_ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL,
  `ADDRESS_ID` INT NOT NULL,
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
  `PUB_ID` INT NOT NULL,
  `BOOK_ID` INT NOT NULL,
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
  `CATEGORY_ID` INT NOT NULL AUTO_INCREMENT,
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
  `BOOK_ID` INT NOT NULL,
  `CATEGORY_ID` INT NOT NULL,
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
  `FORMAT_ID` INT NOT NULL,
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
  `BOOK_ID` INT NOT NULL,
  `FORMAT_ID` INT NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
