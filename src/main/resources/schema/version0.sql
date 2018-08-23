CREATE TABLE `clubhelper`.`ClubEvent` (
  `id` VARCHAR(250) NOT NULL,
  `location` VARCHAR(255) NULL,
  `iCalUID` VARCHAR(150) NULL,
  `organizerDisplayName` VARCHAR(150) NULL,
  `caption` VARCHAR(150) NULL,
  `description` VARCHAR(500) NULL,
  `start` DATETIME NULL,
  `end` DATETIME NULL,
  `allDay` SMALLINT(1) NULL,
  PRIMARY KEY (`id`));
CREATE TABLE IF NOT EXISTS `clubhelper`.`clubevent_has_person` (
  `clubevent_id` VARCHAR(250) NOT NULL,
  `person_id` INT(11) NOT NULL,
  `comment` VARCHAR(250) NOT NULL DEFAULT '',
  PRIMARY KEY (`clubevent_id`, `person_id`),
  INDEX `fk_clubevent_has_person_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_clubevent_has_person_clubevent1_idx` (`clubevent_id` ASC) VISIBLE,
  CONSTRAINT `fk_clubevent_has_person_clubevent1`
    FOREIGN KEY (`clubevent_id`)
    REFERENCES `clubhelper`.`clubevent` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_clubevent_has_person_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `clubhelper`.`person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);