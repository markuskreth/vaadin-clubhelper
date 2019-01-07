package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Repository
@Transactional
public class ClubEventDaoImpl extends AbstractDaoImpl<ClubEvent> implements ClubEventDao {

	public ClubEventDaoImpl() {
		super(ClubEvent.class);
	}

	@Override
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void addPersons(ClubEvent event, Collection<Person> updated) {
		List<Person> added = new ArrayList<>(updated);

		Set<Person> persons2 = event.getPersons();
		if (persons2 != null) {
			added.removeAll(persons2);
		} else {
			persons2 = new HashSet<>();
			event.setPersons(persons2);
			for (Person p : updated) {
				event.add(p);
			}
		}

		Query insertQuery = em.createNativeQuery(
				"INSERT INTO clubevent_has_person (clubevent_id, person_id) VALUES (:eventId,:personId)");
		for (Person p : added) {
			insertQuery.setParameter("eventId", event.getId());
			insertQuery.setParameter("personId", p.getId());
			insertQuery.executeUpdate();

		}
		persons2.addAll(added);
	}

}
