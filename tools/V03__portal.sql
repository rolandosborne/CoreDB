CREATE TABLE `account_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `salt` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `account_login_id_UNIQUE` (`id`),
  UNIQUE INDEX `account_login_username_UNIQUE` (`username`),
  UNIQUE INDEX `account_login_account_UNIQUE` (`account_id`),
  CONSTRAINT `account_login` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `account_token` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NULL,
  `token` VARCHAR(128) NOT NULL,
  `issued` INT(64) NOT NULL,
  `expires` INT(64) NOT NULL,
  `expired` TINYINT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `account_token_id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `account_token_token_UNIQUE` (`token` ASC),
  CONSTRAINT `account_token` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB;

