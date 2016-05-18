CREATE DATABASE  IF NOT EXISTS `eLibrary` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `eLibrary`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: 127.0.0.1    Database: eLibrary
-- ------------------------------------------------------
-- Server version	5.7.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ACCOUNT`
--

DROP TABLE IF EXISTS `ACCOUNT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACCOUNT` (
  `ACCOUNT_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `SALT_VALUE` varchar(255) NOT NULL,
  `ROLE` enum('USER','ADMIN') NOT NULL,
  `CREATION_TIME` datetime NOT NULL,
  `STATE` enum('ACTIVE','FROZEN','CLOSED','TEMPORARY') NOT NULL,
  `CONFIRM_TOKEN` varchar(255) NOT NULL,
  PRIMARY KEY (`ACCOUNT_ID`),
  UNIQUE KEY `ACCOUNT_ID_UNIQUE` (`ACCOUNT_ID`),
  UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`),
  UNIQUE KEY `LOGIN_UNIQUE` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACCOUNT`
--

LOCK TABLES `ACCOUNT` WRITE;
/*!40000 ALTER TABLE `ACCOUNT` DISABLE KEYS */;
INSERT INTO `ACCOUNT` VALUES (9,'Leonti','leontibrechko@gmail.com','cd7ec73c3ad4838c3abcbf3221748ae5e682fa81419ea6029dc1e214ee808378','NYpY+eDja8d0ZeeQXw+/Q7mAszD8I9q3/ipPrJUeyP8=','USER','2016-05-18 10:38:52','FROZEN','PODNJ98D65ha5qkSUImwHlY3CHbG6NFTu8JxBkFItPY='),(10,'Anastasya','nastya6696@list.ru','1d4e42cb107bb14dea4732c4b0f9c24a4e16f78e9916636d7e1f0ce72f17de12','j6T30Ncbg62BWSypZ86wViDIPpxBe/NYMEHTsNRKSB4=','ADMIN','2016-05-18 10:43:58','ACTIVE','/0W/Dqw8h0+NX2LUY3o7L6pnUE59hOdVBOJ/qk8uY6w='),(14,'lib','elibraryprojectfamcs@gmail.com','b91bde3cf8afbab449f807d0373eca117b832a3ba4c6324ba4267c8a2b651593','ToSeF2qNyKxVgWF2YOMgW/TDKMc/+FMraoSLQYSc+I0=','USER','2016-05-18 18:39:53','ACTIVE','IaKxqYmmEohHfIDkfeSkHzl24eWTCJsKb4rD/Wxt9+g=');
/*!40000 ALTER TABLE `ACCOUNT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ACCOUNT_DOWNLOAD_LIST`
--

DROP TABLE IF EXISTS `ACCOUNT_DOWNLOAD_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACCOUNT_DOWNLOAD_LIST` (
  `ACCOUNT_ID` bigint(255) unsigned NOT NULL,
  `BOOK_ID` bigint(255) unsigned NOT NULL,
  PRIMARY KEY (`ACCOUNT_ID`,`BOOK_ID`),
  KEY `fk_ACCOUNT_BOOK_LIST_BOOK1_idx` (`BOOK_ID`),
  CONSTRAINT `fk_ACCOUNT_BOOK_LIST_ACCOUNT1` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT` (`ACCOUNT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ACCOUNT_BOOK_LIST_BOOK1` FOREIGN KEY (`BOOK_ID`) REFERENCES `BOOK` (`BOOK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACCOUNT_DOWNLOAD_LIST`
--

LOCK TABLES `ACCOUNT_DOWNLOAD_LIST` WRITE;
/*!40000 ALTER TABLE `ACCOUNT_DOWNLOAD_LIST` DISABLE KEYS */;
INSERT INTO `ACCOUNT_DOWNLOAD_LIST` VALUES (14,24);
/*!40000 ALTER TABLE `ACCOUNT_DOWNLOAD_LIST` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHOR`
--

DROP TABLE IF EXISTS `AUTHOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHOR` (
  `AUTH_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(255) NOT NULL,
  `LAST_NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`AUTH_ID`),
  UNIQUE KEY `AUTH_ID_UNIQUE` (`AUTH_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHOR`
--

LOCK TABLES `AUTHOR` WRITE;
/*!40000 ALTER TABLE `AUTHOR` DISABLE KEYS */;
INSERT INTO `AUTHOR` VALUES (1,'Michael','Kerrisk'),(2,'Роберт','Лафоре'),(3,'test','test'),(6,'Томас','Кормен'),(7,'89','90'),(8,'Jeanne','Boyarsky');
/*!40000 ALTER TABLE `AUTHOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK`
--

DROP TABLE IF EXISTS `BOOK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK` (
  `BOOK_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `ISBN_13` varchar(13) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `PUB_YEAR` int(11) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `POPULARITY` bigint(255) unsigned DEFAULT NULL,
  `IMAGE` varchar(255) DEFAULT NULL,
  `THUMB` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`BOOK_ID`),
  UNIQUE KEY `BOOK_ID_UNIQUE` (`BOOK_ID`),
  UNIQUE KEY `ISBN_UNIQUE` (`ISBN_13`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK`
--

LOCK TABLES `BOOK` WRITE;
/*!40000 ALTER TABLE `BOOK` DISABLE KEYS */;
INSERT INTO `BOOK` VALUES (19,'9781593272203','The_Linux_Programming_Interface',2010,'/catalog/books/9781593272203/desc.txt',1,'/catalog/books/9781593272203/image.jpg','/catalog/books/9781593272203/thumb.jpg'),(24,'9781118957400','OCA Oracle Certified Associate Java SE 8 Programmer I Study Guide Exam 1Z0-808',2015,'/catalog/books/default_desc.txt',1,'/catalog/books/9781118957400/image.jpg','/catalog/books/9781118957400/thumb.jpg'),(25,'9785947233025','Лафоре Р. Объектно-ориентированное программирование в С++ (4-е издание)',2004,'/catalog/books/default_desc.txt',0,'/catalog/books/9785947233025/image.jpg','/catalog/books/9785947233025/thumb.jpg');
/*!40000 ALTER TABLE `BOOK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK_AUTHOR`
--

DROP TABLE IF EXISTS `BOOK_AUTHOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK_AUTHOR` (
  `BOOK_ID` bigint(255) unsigned NOT NULL,
  `AUTH_ID` bigint(255) unsigned NOT NULL,
  PRIMARY KEY (`BOOK_ID`,`AUTH_ID`),
  KEY `fk_BOOK_AUTHOR_AUTHOR1` (`AUTH_ID`),
  KEY `fk_BOOK_AUTHOR_BOOK1_idx` (`BOOK_ID`),
  CONSTRAINT `fk_BOOK_AUTHOR_AUTHOR1` FOREIGN KEY (`AUTH_ID`) REFERENCES `AUTHOR` (`AUTH_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_AUTHOR_BOOK1` FOREIGN KEY (`BOOK_ID`) REFERENCES `BOOK` (`BOOK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK_AUTHOR`
--

LOCK TABLES `BOOK_AUTHOR` WRITE;
/*!40000 ALTER TABLE `BOOK_AUTHOR` DISABLE KEYS */;
INSERT INTO `BOOK_AUTHOR` VALUES (19,1),(25,2),(24,8);
/*!40000 ALTER TABLE `BOOK_AUTHOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK_CATEGORY`
--

DROP TABLE IF EXISTS `BOOK_CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK_CATEGORY` (
  `BOOK_ID` bigint(255) unsigned NOT NULL,
  `CATEGORY_ID` bigint(255) unsigned NOT NULL,
  PRIMARY KEY (`BOOK_ID`,`CATEGORY_ID`),
  KEY `fk_BOOK_CATEGORY_CATEGORY1` (`CATEGORY_ID`),
  KEY `fk_BOOK_CATEGORY_BOOK1_idx` (`BOOK_ID`),
  CONSTRAINT `fk_BOOK_CATEGORY_BOOK1` FOREIGN KEY (`BOOK_ID`) REFERENCES `BOOK` (`BOOK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_CATEGORY_CATEGORY1` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORY` (`CATEGORY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK_CATEGORY`
--

LOCK TABLES `BOOK_CATEGORY` WRITE;
/*!40000 ALTER TABLE `BOOK_CATEGORY` DISABLE KEYS */;
INSERT INTO `BOOK_CATEGORY` VALUES (24,1),(19,2),(25,2);
/*!40000 ALTER TABLE `BOOK_CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK_FILE`
--

DROP TABLE IF EXISTS `BOOK_FILE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK_FILE` (
  `BOOK_ID` bigint(255) unsigned NOT NULL,
  `FORMAT_ID` bigint(255) unsigned NOT NULL,
  `LANG_ID` bigint(255) unsigned NOT NULL,
  `PATH` varchar(255) NOT NULL,
  PRIMARY KEY (`BOOK_ID`,`FORMAT_ID`,`LANG_ID`),
  UNIQUE KEY `PATH_UNIQUE` (`PATH`),
  KEY `fk_BOOK_FORMAT_FORMAT1_idx` (`FORMAT_ID`),
  KEY `fk_BOOK_FORMAT_LANGUAGE1_idx` (`LANG_ID`),
  CONSTRAINT `fk_BOOK_FORMAT_BOOK1` FOREIGN KEY (`BOOK_ID`) REFERENCES `BOOK` (`BOOK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_FORMAT_FORMAT1` FOREIGN KEY (`FORMAT_ID`) REFERENCES `FORMAT` (`FORMAT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_FORMAT_LANGUAGE1` FOREIGN KEY (`LANG_ID`) REFERENCES `LANGUAGE` (`LANG_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK_FILE`
--

LOCK TABLES `BOOK_FILE` WRITE;
/*!40000 ALTER TABLE `BOOK_FILE` DISABLE KEYS */;
INSERT INTO `BOOK_FILE` VALUES (24,0,0,'/catalog/books/9781118957400/OCA_Oracle_Certified_Associate_Java_SE_8_Programmer_I_Study_Guide_Exam_1Z0-808.pdf'),(19,0,0,'/catalog/books/9781593272203/The_Linux_Programming_Interface.pdf'),(25,2,1,'/catalog/books/9785947233025/Лафоре_Р._Объектно-ориентированное_программирование_в_С++_(4-е_издание).djvu');
/*!40000 ALTER TABLE `BOOK_FILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK_PUBLISHER`
--

DROP TABLE IF EXISTS `BOOK_PUBLISHER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BOOK_PUBLISHER` (
  `PUB_ID` bigint(255) unsigned NOT NULL,
  `BOOK_ID` bigint(255) unsigned NOT NULL,
  PRIMARY KEY (`PUB_ID`,`BOOK_ID`),
  KEY `fk_BOOK_PUBLISHER_BOOK1_idx` (`BOOK_ID`),
  CONSTRAINT `fk_BOOK_PUBLISHER_BOOK1` FOREIGN KEY (`BOOK_ID`) REFERENCES `BOOK` (`BOOK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_BOOK_PUBLISHER_PUBLISHER1` FOREIGN KEY (`PUB_ID`) REFERENCES `PUBLISHER` (`PUB_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK_PUBLISHER`
--

LOCK TABLES `BOOK_PUBLISHER` WRITE;
/*!40000 ALTER TABLE `BOOK_PUBLISHER` DISABLE KEYS */;
INSERT INTO `BOOK_PUBLISHER` VALUES (1,19),(1,24),(3,25);
/*!40000 ALTER TABLE `BOOK_PUBLISHER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATEGORY` (
  `CATEGORY_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESC` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CATEGORY_ID`),
  UNIQUE KEY `CATEGORY_ID_UNIQUE` (`CATEGORY_ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORY`
--

LOCK TABLES `CATEGORY` WRITE;
/*!40000 ALTER TABLE `CATEGORY` DISABLE KEYS */;
INSERT INTO `CATEGORY` VALUES (1,'Образование','В этой категории книг Вы можете найти множество полезной информации, которая поможет в изучении определенного предмета.'),(2,'Программирование',NULL);
/*!40000 ALTER TABLE `CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FORMAT`
--

DROP TABLE IF EXISTS `FORMAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FORMAT` (
  `FORMAT_ID` bigint(255) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`FORMAT_ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  UNIQUE KEY `FORMAT_ID_UNIQUE` (`FORMAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FORMAT`
--

LOCK TABLES `FORMAT` WRITE;
/*!40000 ALTER TABLE `FORMAT` DISABLE KEYS */;
INSERT INTO `FORMAT` VALUES (2,'DJVU'),(1,'EPUB'),(3,'FB2'),(0,'PDF');
/*!40000 ALTER TABLE `FORMAT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LANGUAGE`
--

DROP TABLE IF EXISTS `LANGUAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LANGUAGE` (
  `LANG_ID` bigint(255) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`LANG_ID`),
  UNIQUE KEY `LANG_ID_UNIQUE` (`LANG_ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LANGUAGE`
--

LOCK TABLES `LANGUAGE` WRITE;
/*!40000 ALTER TABLE `LANGUAGE` DISABLE KEYS */;
INSERT INTO `LANGUAGE` VALUES (2,'BELARUSIAN'),(0,'ENGLISH'),(1,'RUSSIAN');
/*!40000 ALTER TABLE `LANGUAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PUBLISHER`
--

DROP TABLE IF EXISTS `PUBLISHER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PUBLISHER` (
  `PUB_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `COUNTRY` varchar(255) NOT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `CITY` varchar(255) NOT NULL,
  `STREET_NUM` int(11) DEFAULT NULL,
  `STREET_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PUB_ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  UNIQUE KEY `PUB_ID_UNIQUE` (`PUB_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PUBLISHER`
--

LOCK TABLES `PUBLISHER` WRITE;
/*!40000 ALTER TABLE `PUBLISHER` DISABLE KEYS */;
INSERT INTO `PUBLISHER` VALUES (1,'No Starch Press, Inc.','USA','CA','San Francisco',38,'Ringold Street'),(2,'test','Belarus','default','Minsk',28,'s'),(3,'ООО \"И.Д. Вильямс\"','Russia','','Moscow',43,'Lesnaya'),(4,'kjk','df','hfh','f',12,'gh');
/*!40000 ALTER TABLE `PUBLISHER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER` (
  `USER_ID` bigint(255) unsigned NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(255) NOT NULL,
  `LAST_NAME` varchar(45) NOT NULL,
  `ACCOUNT_ID` bigint(255) unsigned NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_ID_UNIQUE` (`USER_ID`),
  UNIQUE KEY `ACCOUNT_ID_UNIQUE` (`ACCOUNT_ID`),
  KEY `fk_USER_ACCOUNT1_idx` (`ACCOUNT_ID`),
  CONSTRAINT `fk_USER_ACCOUNT1` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT` (`ACCOUNT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (9,'Leonti','Brechko',9),(10,'Anastasya','Piskunova',10),(14,'eLibrary','eLibrary',14);
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-18 19:36:54
