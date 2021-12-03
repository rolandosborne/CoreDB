
-- MySQL dump 10.16  Distrib 10.1.47-MariaDB, for debian-linux-gnu (aarch64)
--
-- Host: localhost    Database: coredb
-- ------------------------------------------------------
-- Server version	10.1.47-MariaDB-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emigo_id` varchar(64) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  `locked` tinyint(4) NOT NULL DEFAULT '0',
  `dirty` tinyint(4) NOT NULL DEFAULT '1',
  `index_revision` int(11) NOT NULL DEFAULT '0',
  `share_revision` int(11) NOT NULL DEFAULT '0',
  `prompt_revision` int(11) NOT NULL DEFAULT '0',
  `user_revision` int(11) NOT NULL DEFAULT '0',
  `identity_revision` int(11) NOT NULL DEFAULT '0',
  `profile_revision` int(11) NOT NULL DEFAULT '0',
  `group_revision` int(11) NOT NULL DEFAULT '0',
  `contact_revision` int(11) NOT NULL DEFAULT '0',
  `view_revision` int(11) NOT NULL DEFAULT '0',
  `show_revision` int(11) NOT NULL DEFAULT '0',
  `service_revision` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `account_emigo_idx` (`emigo_id`),
  CONSTRAINT `account_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_alert`
--

DROP TABLE IF EXISTS `account_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `alert_id` varchar(32) DEFAULT NULL,
  `created` int(64) DEFAULT NULL,
  `message` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_alert_id_UNIQUE` (`account_id`,`alert_id`),
  KEY `account_idx` (`account_id`),
  CONSTRAINT `alert_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_answer`
--

DROP TABLE IF EXISTS `account_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prompt_id` int(11) NOT NULL,
  `answer_id` varchar(32) NOT NULL,
  `answer` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `answer_id_UNIQUE` (`prompt_id`,`answer_id`),
  KEY `prompt_idx` (`prompt_id`),
  CONSTRAINT `account_answer_prompt` FOREIGN KEY (`prompt_id`) REFERENCES `account_prompt` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_attribute`
--

DROP TABLE IF EXISTS `account_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `attribute_id` varchar(32) NOT NULL,
  `schema_id` varchar(64) NOT NULL,
  `value` mediumtext,
  `revision` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `attribute_id_UNIQUE` (`account_id`,`attribute_id`),
  KEY `account_attribute_account_idx` (`account_id`),
  CONSTRAINT `account_attribute_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_config`
--

DROP TABLE IF EXISTS `account_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `config_id` varchar(32) NOT NULL,
  `str_value` varchar(1024) DEFAULT NULL,
  `num_value` bigint(64) DEFAULT NULL,
  `bool_value` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_config_id_UNIQUE` (`account_id`,`config_id`),
  KEY `account_idx` (`account_id`),
  CONSTRAINT `account_config_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_message`
--

DROP TABLE IF EXISTS `account_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `message` mediumtext NOT NULL,
  `signature` varchar(4096) NOT NULL,
  `pubkey` varchar(4096) NOT NULL,
  `pubkey_type` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_message_emigo_UNIQUE` (`account_id`),
  KEY `account_message_account_idx` (`account_id`),
  CONSTRAINT `account_message_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_prompt`
--

DROP TABLE IF EXISTS `account_prompt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_prompt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `prompt_id` varchar(32) NOT NULL,
  `image` mediumtext,
  `question` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `prompt_id_UNIQUE` (`account_id`,`prompt_id`),
  KEY `account_idx` (`account_id`),
  CONSTRAINT `prompt_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `attribute_label`
--

DROP TABLE IF EXISTS `attribute_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_attribute_id` int(11) DEFAULT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `attribute_idx` (`account_attribute_id`),
  KEY `label_idx` (`label_id`),
  CONSTRAINT `account_label_attribute` FOREIGN KEY (`account_attribute_id`) REFERENCES `account_attribute` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `account_label_label` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` varchar(32) NOT NULL,
  `str_value` varchar(1024) DEFAULT NULL,
  `num_value` bigint(64) DEFAULT NULL,
  `bool_value` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `config_id_UNIQUE` (`config_id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emigo`
--

DROP TABLE IF EXISTS `emigo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emigo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emigo_id` varchar(64) NOT NULL DEFAULT '',
  `account_id` int(11) NOT NULL,
  `prompt` tinyint(4) DEFAULT NULL,
  `accept` tinyint(4) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  `hidden` tinyint(4) DEFAULT NULL,
  `notes` varchar(4096) DEFAULT NULL,
  `revision` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_emigo_id_UNIQUE` (`account_id`,`emigo_id`),
  UNIQUE KEY `emigo_id_UNIQUE` (`id`),
  KEY `account_id` (`id`),
  KEY `account_idx` (`account_id`),
  KEY `emigo_emigo_registry_idx` (`emigo_id`),
  CONSTRAINT `emigo_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `emigo_emigo_registry` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emigo_label`
--

DROP TABLE IF EXISTS `emigo_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emigo_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emigo_id` int(11) NOT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `label_idx` (`label_id`),
  KEY `emigo_label_emigo_idx` (`emigo_id`),
  CONSTRAINT `emigo_label_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `emigo_label_label` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emigo_registry`
--

DROP TABLE IF EXISTS `emigo_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emigo_registry` (
  `emigo_id` varchar(64) NOT NULL DEFAULT '',
  `name` varchar(1024) DEFAULT NULL,
  `location` varchar(1024) DEFAULT NULL,
  `description` varchar(4096) DEFAULT NULL,
  `logo` mediumtext,
  `revision` int(11) NOT NULL,
  `version` varchar(32) NOT NULL,
  `blocked` tinyint(4) NOT NULL DEFAULT '0',
  `handle` varchar(128) DEFAULT NULL,
  `node` varchar(1024) DEFAULT NULL,
  `registry` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`emigo_id`),
  UNIQUE KEY `emigo_registry_id_UNIQUE` (`emigo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `label_id` varchar(32) NOT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `logo` mediumtext,
  `description` varchar(4096) DEFAULT NULL,
  `revision` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `label_id_UNIQUE` (`account_id`,`label_id`),
  KEY `label_account_idx` (`account_id`),
  CONSTRAINT `label_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pass`
--

DROP TABLE IF EXISTS `pass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `pass` varchar(64) NOT NULL,
  `issued` bigint(64) DEFAULT NULL,
  `expires` bigint(64) DEFAULT NULL,
  `account_show` tinyint(4) NOT NULL DEFAULT '0',
  `account_identity` tinyint(4) NOT NULL DEFAULT '0',
  `account_profile` tinyint(4) NOT NULL DEFAULT '0',
  `account_group` tinyint(4) NOT NULL DEFAULT '0',
  `account_share` tinyint(4) NOT NULL DEFAULT '0',
  `account_prompt` tinyint(4) NOT NULL DEFAULT '0',
  `account_app` tinyint(4) NOT NULL DEFAULT '0',
  `account_index` tinyint(4) NOT NULL DEFAULT '0',
  `account_contact` tinyint(4) NOT NULL DEFAULT '0',
  `account_agent` tinyint(4) NOT NULL DEFAULT '0',
  `account_user` tinyint(4) NOT NULL DEFAULT '0',
  `account_access` tinyint(4) NOT NULL DEFAULT '0',
  `account_account` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pass_id_UNIQUE` (`account_id`,`pass`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`),
  CONSTRAINT `pass_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reject_emigo`
--

DROP TABLE IF EXISTS `reject_emigo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reject_emigo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `unique_reject_emigo` (`account_id`,`emigo_id`),
  CONSTRAINT `reject_emigo_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `token` varchar(64) NOT NULL,
  `hidden` tinyint(4) NOT NULL DEFAULT '0',
  `account_show` tinyint(4) NOT NULL DEFAULT '0',
  `account_identity` tinyint(4) NOT NULL DEFAULT '0',
  `account_profile` tinyint(4) NOT NULL DEFAULT '0',
  `account_group` tinyint(4) NOT NULL DEFAULT '0',
  `account_share` tinyint(4) NOT NULL DEFAULT '0',
  `account_prompt` tinyint(4) NOT NULL DEFAULT '0',
  `account_app` tinyint(4) NOT NULL DEFAULT '0',
  `account_index` tinyint(4) NOT NULL DEFAULT '0',
  `account_agent` tinyint(4) NOT NULL DEFAULT '0',
  `account_user` tinyint(4) NOT NULL DEFAULT '0',
  `account_access` tinyint(4) NOT NULL DEFAULT '0',
  `account_account` tinyint(4) NOT NULL DEFAULT '0',
  `service_show` tinyint(4) NOT NULL DEFAULT '0',
  `service_identity` tinyint(4) NOT NULL DEFAULT '0',
  `service_profile` tinyint(4) NOT NULL DEFAULT '0',
  `service_group` tinyint(4) NOT NULL DEFAULT '0',
  `service_share` tinyint(4) NOT NULL DEFAULT '0',
  `service_prompt` tinyint(4) NOT NULL DEFAULT '0',
  `service_app` tinyint(4) NOT NULL DEFAULT '0',
  `service_index` tinyint(4) NOT NULL DEFAULT '0',
  `service_agent` tinyint(4) NOT NULL DEFAULT '0',
  `service_user` tinyint(4) NOT NULL DEFAULT '0',
  `service_access` tinyint(4) NOT NULL DEFAULT '0',
  `service_account` tinyint(4) NOT NULL DEFAULT '0',
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `tok_UNIQUE` (`token`),
  KEY `account_idx` (`account_id`),
  KEY `service_registry_idx` (`service_id`),
  CONSTRAINT `service_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `service_service_registry` FOREIGN KEY (`service_id`) REFERENCES `service_registry` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_agent`
--

DROP TABLE IF EXISTS `service_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_agent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL,
  `issued` int(64) NOT NULL,
  `expires` int(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `service_agent_service` (`service_id`),
  KEY `service_agent_emigo` (`emigo_id`),
  CONSTRAINT `service_agent_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `service_agent_service` FOREIGN KEY (`service_id`) REFERENCES `service_registry` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_registry`
--

DROP TABLE IF EXISTS `service_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emigo_id` varchar(64) NOT NULL DEFAULT '',
  `blocked` tinyint(4) NOT NULL DEFAULT '0',
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `emigo_registry_emigo_id_UNIQUE` (`id`),
  UNIQUE KEY `emigo_registry_id_UNIQUE` (`emigo_id`),
  CONSTRAINT `servcie_emigo_registry` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `share_connection`
--

DROP TABLE IF EXISTS `share_connection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share_connection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo_id` int(11) NOT NULL,
  `share_id` varchar(32) NOT NULL,
  `in_token` varchar(64) DEFAULT NULL,
  `out_token` varchar(64) DEFAULT NULL,
  `state` varchar(16) NOT NULL,
  `updated` int(64) DEFAULT NULL,
  `revision` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `share_connection_share_key` (`account_id`,`share_id`),
  UNIQUE KEY `unique_share_emigo` (`account_id`,`emigo_id`),
  KEY `share_connection_share_idx` (`share_id`),
  KEY `share_connection_account_idx` (`account_id`),
  KEY `recv_request_emigo` (`emigo_id`),
  CONSTRAINT `recv_request_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `recv_request_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `share_pending`
--

DROP TABLE IF EXISTS `share_pending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share_pending` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo` varchar(64) NOT NULL,
  `emigo_message_key` varchar(4096) NOT NULL,
  `emigo_message_key_type` varchar(32) NOT NULL,
  `emigo_message_signature` varchar(1024) NOT NULL,
  `emigo_message_data` mediumtext NOT NULL,
  `share_id` varchar(32) NOT NULL,
  `emigo_id` int(11) DEFAULT NULL,
  `updated` int(64) DEFAULT NULL,
  `revision` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `share_pending_share_key` (`account_id`,`share_id`),
  UNIQUE KEY `unique_pending_emigo` (`account_id`,`emigo`),
  UNIQUE KEY `unique_pending_emigo_id` (`account_id`,`emigo_id`),
  KEY `share_pending_share_idx` (`share_id`),
  KEY `share_pending_account_idx` (`account_id`),
  KEY `pending_request_emigo` (`emigo_id`),
  CONSTRAINT `pending_request_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pending_request_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `share_prompt`
--

DROP TABLE IF EXISTS `share_prompt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share_prompt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `prompt_id` varchar(32) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `emigo_message_key` varchar(4096) NOT NULL,
  `emigo_message_key_type` varchar(32) NOT NULL,
  `emigo_message_signature` varchar(1024) NOT NULL,
  `emigo_message_data` mediumtext NOT NULL,
  `prompt_token` varchar(64) NOT NULL,
  `share_token` varchar(64) NOT NULL,
  `fail_count` int(11) DEFAULT NULL,
  `expires` int(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `prompt_token_UNIQUE` (`prompt_token`),
  KEY `share_prompt_account_idx` (`account_id`),
  CONSTRAINT `confirm_request_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `subject_id` varchar(32) NOT NULL,
  `revision` int(11) NOT NULL,
  `created` int(64) NOT NULL,
  `modified` int(64) NOT NULL,
  `viewable` tinyint(4) NOT NULL,
  `schema_id` varchar(64) DEFAULT NULL,
  `value` mediumtext,
  `tag_revision` int(11) NOT NULL DEFAULT '0',
  `expires` int(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `subject_id_UNIQUE` (`account_id`,`subject_id`),
  KEY `subject_account_idx` (`account_id`),
  CONSTRAINT `subject_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_asset`
--

DROP TABLE IF EXISTS `subject_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_asset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) DEFAULT NULL,
  `asset_id` varchar(32) NOT NULL,
  `asset_size` int(64) DEFAULT NULL,
  `asset_hash` varchar(64) DEFAULT NULL,
  `created` int(64) DEFAULT NULL,
  `transform` varchar(8) NOT NULL,
  `status` varchar(32) NOT NULL,
  `original_id` varchar(48) DEFAULT NULL,
  `pending_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `subject_asset_id_UNIQUE` (`asset_id`,`subject_id`),
  KEY `subject_asset_idx` (`subject_id`),
  KEY `asset_idx` (`asset_id`),
  KEY `pending_subject` (`pending_id`),
  CONSTRAINT `asset_subject` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pending_subject` FOREIGN KEY (`pending_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_label`
--

DROP TABLE IF EXISTS `subject_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NOT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `subject_label_idx` (`subject_id`),
  KEY `subject_label_label` (`label_id`),
  CONSTRAINT `subject_label_label` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `subject_label_subject` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system`
--

DROP TABLE IF EXISTS `system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` int(32) NOT NULL,
  `processor` int(11) NOT NULL,
  `memory` bigint(64) NOT NULL,
  `storage` bigint(64) NOT NULL,
  `requests` bigint(64) NOT NULL,
  `accounts` bigint(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) DEFAULT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `tag_id` varchar(32) NOT NULL,
  `schema_id` varchar(64) NOT NULL,
  `data` mediumtext,
  `created` bigint(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `subject_tag_id_UNIQUE` (`tag_id`,`subject_id`),
  KEY `emigo_tag` (`emigo_id`),
  KEY `subject_tag` (`subject_id`),
  CONSTRAINT `emigo_tag` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `subject_tag` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) DEFAULT NULL,
  `asset_id` varchar(34) NOT NULL,
  `original_name` varchar(1024) NOT NULL,
  `status` varchar(32) NOT NULL,
  `size` bigint(64) DEFAULT NULL,
  `hash` varchar(64) DEFAULT NULL,
  `created` bigint(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `asset_subject_id_UNIQUE` (`asset_id`,`subject_id`),
  KEY `upload_subject` (`subject_id`),
  CONSTRAINT `upload_subject` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `account_token` varchar(64) NOT NULL,
  `service_token` varchar(64) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `user_account_emigo_UNIQUE` (`account_id`,`emigo_id`),
  UNIQUE KEY `service_token_UNIQUE` (`service_token`),
  KEY `service_account_idx` (`account_id`),
  KEY `user_emigo_idx` (`emigo_id`),
  CONSTRAINT `user_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_emigo` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_agent`
--

DROP TABLE IF EXISTS `user_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_agent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `message` mediumtext NOT NULL,
  `signature` varchar(1024) NOT NULL,
  `issued` int(64) NOT NULL,
  `expires` int(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `unique_user` (`user_id`),
  CONSTRAINT `user_agent_emigo` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-12 18:32:37
