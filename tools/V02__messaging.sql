ALTER TABLE account ADD COLUMN  dialogue_revision int(11) NOT NULL DEFAULT '0'; 
ALTER TABLE account ADD COLUMN  insight_revision int(11) NOT NULL DEFAULT '0'; 
ALTER TABLE pass ADD COLUMN  account_conversation tinyint(4) NOT NULL DEFAULT '0'; 
ALTER TABLE service ADD COLUMN  account_conversation tinyint(4) NOT NULL DEFAULT '0'; 
ALTER TABLE service ADD COLUMN  service_conversation tinyint(4) NOT NULL DEFAULT '0'; 

CREATE TABLE `insight` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `dialogue_id` varchar(32) NOT NULL,
  `revision` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dialogue_id_UNIQUE` (`id`),
  UNIQUE KEY `insight_dialogue_UNIQUE` (`account_id`, `emigo_id`, `dialogue_id`),
  CONSTRAINT `insight_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `insight_emigo_registry` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `dialogue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `dialogue_id` varchar(32) NOT NULL,
  `revision` int(11) NOT NULL,
  `linked` tinyint(4) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `synced` tinyint(4) NOT NULL,
  `created` int(64) NOT NULL,
  `modified` int(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dialogue_id_UNIQUE` (`id`),
  UNIQUE KEY `dialogue_UNIQUE` (`account_id`, `dialogue_id`),
  CONSTRAINT `dialogue_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dialogue_emigo_registry` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dialogue_id` int(11) NOT NULL,
  `topic_id` varchar(32) NOT NULL,
  `revision` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `topic_id_UNIQUE` (`id`),
  UNIQUE KEY `topic_UNIQUE` (`dialogue_id`, `topic_id`),
  CONSTRAINT `topic_dialog` FOREIGN KEY (`dialogue_id`) REFERENCES `dialogue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `blurb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dialogue_id` int(11) NOT NULL,
  `emigo_id` varchar(64) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `blurb_id` varchar(32) NOT NULL,
  `revision` int(11) NOT NULL,
  `created` int(64) NOT NULL,
  `modified` int(64) NOT NULL,
  `schema_id` varchar(64) DEFAULT NULL,
  `value` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `blurb_UNIQUE` (`dialogue_id`,`blurb_id`),
  CONSTRAINT `blurb_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `blurb_dialogue` FOREIGN KEY (`dialogue_id`) REFERENCES `dialogue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `blurb_emigo_registry` FOREIGN KEY (`emigo_id`) REFERENCES `emigo_registry` (`emigo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

