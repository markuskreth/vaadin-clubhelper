create Table notes (
  id INT NOT NULL AUTO_INCREMENT,
  person_id INT(11) NOT NULL,
  notekey varchar(25),
  notetext varchar(2000),
  PRIMARY KEY (id),
  CONSTRAINT fk_notes_person
    FOREIGN KEY (`person_id`)
    REFERENCES `person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

UPDATE `clubhelper`.`version` SET `version` = '15' WHERE (`id` = '1');