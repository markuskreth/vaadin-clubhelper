package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;

@Component
public class PersonBusiness {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PersonDao dao;

	public void save(Person edited) {
		dao.save(edited);
		log.info("Saved {}", edited);
	}

	public void delete(Contact c) {
		dao.delete(c);
		log.info("Deleted {}", c);
	}

	public Collection<? extends Relation> findRelationsFor(Person person) {
		log.info("Loading relations of {}", person);
		return dao.findRelationsFor(person);
	}

	public void delete(Adress a) {
		dao.delete(a);
		log.info("Deleted {}", a);
	}

	public List<Person> listAll() {
		log.info("Loading all Persons");
		return dao.listAll();
	}

	public void delete(Person c) {
		dao.delete(c);
		log.info("Deleted {}", c);
	}

	public Person findLoginUser(String username, String password) {
		log.info("Identifiing Login for user {}", username);
		return dao.findLoginUser(username, password);
	}

}
