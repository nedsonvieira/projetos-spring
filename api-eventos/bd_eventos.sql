-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bd_eventos
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `eventos`
--

DROP TABLE IF EXISTS `eventos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos` (
  `id` bigint NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `pretty_name` varchar(50) NOT NULL,
  `local` varchar(255) NOT NULL,
  `preco` double NOT NULL,
  `data_inicio` date DEFAULT NULL,
  `data_fim` date DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fim` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pretty_name_UNIQUE` (`pretty_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventos`
--

LOCK TABLES `eventos` WRITE;
/*!40000 ALTER TABLE `eventos` DISABLE KEYS */;
INSERT INTO `eventos` VALUES (5,'CodeCraft Summit 2025','codecraft-summit-2025','Online',0,'2025-03-16','2025-03-18','19:00:00','21:00:00'),(6,'Imersao Java 2025','imersao-java-2025','Online',20,'2025-04-16','2025-04-18','19:00:00','21:00:00');
/*!40000 ALTER TABLE `eventos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscricoes`
--

DROP TABLE IF EXISTS `inscricoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inscricoes` (
  `numero_inscricao` bigint NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  `indicacao_usuario_id` bigint DEFAULT NULL,
  `evento_id` bigint DEFAULT NULL,
  PRIMARY KEY (`numero_inscricao`),
  KEY `fk_tbl_subscription_tbl_user_idx` (`usuario_id`),
  KEY `fk_tbl_subscription_tbl_user1_idx` (`indicacao_usuario_id`),
  KEY `fk_tbl_subscription_tbl_event1_idx` (`evento_id`),
  CONSTRAINT `fk_evento_id` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`),
  CONSTRAINT `fk_indicacao_usuario_id` FOREIGN KEY (`indicacao_usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscricoes`
--

LOCK TABLES `inscricoes` WRITE;
/*!40000 ALTER TABLE `inscricoes` DISABLE KEYS */;
INSERT INTO `inscricoes` VALUES (1,3,NULL,5),(2,5,NULL,5),(3,6,NULL,5),(4,7,NULL,5),(5,8,3,5),(6,9,NULL,5),(7,10,3,5),(8,11,3,5),(9,12,3,5),(10,13,3,5),(11,14,5,5),(12,15,5,5),(13,16,5,5),(14,17,6,5),(15,18,6,5),(16,19,7,5),(17,20,3,5);
/*!40000 ALTER TABLE `inscricoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (3,'Nedson Vieira','test@test.com'),(5,'John Doe','john@doe.com'),(6,'Larry Loe','larry@loe.com'),(7,'Mary May','mary@may.com'),(8,'Peter Parker','peter@parker.com'),(9,'Candy Crush','candy@crush.com'),(10,'Tony Stark','tony.stark@starkindustries.com'),(11,'Bruce Wayne','bruce.wayne@wayneenterprises.com'),(12,'Clark Kent','clark.kent@dailyplanet.com'),(13,'Diana Prince','diana.prince@themyscira.com'),(14,'Steve Rogers','steve.rogers@avengers.com'),(15,'Natasha Romanoff','natasha.romanoff@shield.com'),(16,'Thor Odinson','thor.odinson@asgard.com'),(17,'Bruce Banner','bruce.banner@avengers.com'),(18,'Wanda Maximoff','wanda.maximoff@avengers.com'),(19,'Stephen Strange','stephen.strange@sanctumsanctorum.com'),(20,'Barry Allen','barry.allen@star-labs.com');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-28 14:10:14
