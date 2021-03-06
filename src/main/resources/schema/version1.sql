CREATE TABLE `pflichten` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `fixed` TINYINT NULL,
  `ordered` INT NOT NULL,
  `comment` VARCHAR(500) NULL,
  `changed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);
  
CREATE TABLE `altersgruppe` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `event_id` VARCHAR(250) NOT NULL,
  `pflicht_id` INT NULL,
  `bezeichnung` VARCHAR(100) NOT NULL,
  `start` INT NULL,
  `end` VARCHAR(45) NULL,
  `changed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_altersgruppe_pflicht_idx` (`pflicht_id` ASC) VISIBLE,
  INDEX `fk_altersgruppe_event_idx` (`event_id` ASC) VISIBLE,
  CONSTRAINT `fk_altersgruppe_pflicht`
    FOREIGN KEY (`pflicht_id`)
    REFERENCES `pflichten` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION),
  CONSTRAINT `fk_altersgruppe_event`
  FOREIGN KEY (`event_id`)
  REFERENCES `clubhelper`.`clubevent` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
    
CREATE TABLE `event_has_altersgruppe` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `event_id` VARCHAR(250) NOT NULL,
  `altersgruppe_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_event_has_altersgruppe_event_idx` (`event_id` ASC) VISIBLE,
  INDEX `fk_event_has_altersgruppe_altersgruppe_idx` (`altersgruppe_id` ASC) VISIBLE,
  CONSTRAINT `fk_event_has_altersgruppe_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `clubevent` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_has_altersgruppe_altersgruppe`
    FOREIGN KEY (`altersgruppe_id`)
    REFERENCES `altersgruppe` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
ALTER TABLE `person` 
ADD COLUMN `gender` SMALLINT(1) NULL AFTER `birth`;
CREATE TABLE `clubhelper`.`clubevent_addon` (
  `id` VARCHAR(250) NOT NULL,
  `competition_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_event_addon_id`
    FOREIGN KEY (`id`)
    REFERENCES `clubhelper`.`clubevent` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

ALTER TABLE `adress` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `altersgruppe` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `attendance` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `contact` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `deleted_entries` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `groupdef` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `person` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `persongroup` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `pflichten` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `relative` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `startpaesse` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `startpass_startrechte` 
CHANGE COLUMN `changed` `changed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
CHANGE COLUMN `created` `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

UPDATE `clubhelper`.`version` SET `version` = '12' WHERE (`id` = '1');