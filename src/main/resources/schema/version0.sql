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
