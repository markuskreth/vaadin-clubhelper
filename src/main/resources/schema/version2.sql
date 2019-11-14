ALTER TABLE `clubhelper`.`ClubEvent` ADD deleted SMALLINT(1) NOT NULL DEFAULT 0;

UPDATE `clubhelper`.`version` SET `version` = '13' WHERE (`id` = '1');