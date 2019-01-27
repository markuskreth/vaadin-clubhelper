package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Repository
@Transactional
public class ClubEventDaoImpl extends AbstractDaoImpl<ClubEvent> implements ClubEventDao {

	private static final long serialVersionUID = 7648111697282525347L;

	public ClubEventDaoImpl() {
		super(ClubEvent.class);
	}

	@Override
	public void save(ClubEvent obj) {
		CompetitionType competitionType = obj.getCompetitionType();
		if (competitionType != null) {
			if (obj.getCompetitionType().getId() == null) {
				obj.getCompetitionType().setId(obj.getId());
			}
		}
		super.save(obj);
	}

	@Override
	public void addPersons(ClubEvent event, Collection<Person> updated) {
		List<Person> added = new ArrayList<>(updated);
		List<Person> removed = new ArrayList<>();
		Set<Person> current = event.getPersons();

		if (current != null) {
			for (Person p : current) {
				if (added.contains(p) == false) {
					removed.add(p);
				}
			}
			added.removeAll(current);
		} else {
			current = new HashSet<>();
			event.setPersons(current);
			for (Person p : updated) {
				event.add(p);
			}
		}

		executeQueries(event, added, removed);
		current.addAll(added);
		current.removeAll(removed);
	}

	public void executeQueries(ClubEvent event, List<Person> added, List<Person> removed) {
		if (!added.isEmpty()) {
			Query insertQuery = entityManager.createNativeQuery(
					"INSERT INTO clubevent_has_person (clubevent_id, person_id) VALUES (:eventId,:personId)");
			for (Person p : added) {
				insertQuery.setParameter("eventId", event.getId());
				insertQuery.setParameter("personId", p.getId());
				insertQuery.executeUpdate();
			}
		}
		if (!removed.isEmpty()) {
			Query deleteQuery = entityManager.createNativeQuery(
					"DELETE FROM clubevent_has_person WHERE clubevent_id=:eventId AND person_id=:personId");
			for (Person p : removed) {
				deleteQuery.setParameter("eventId", event.getId());
				deleteQuery.setParameter("personId", p.getId());
				deleteQuery.executeUpdate();
			}
		}
	}

	@Override
	public void updateEventType(ClubEvent obj) {
		CompetitionType type = obj.getCompetitionType();
		if (type != null) {
			Query query;
			if (type.getId() == null) {
				type.setId(obj.getId());
				query = entityManager.createNativeQuery(
						"INSERT INTO clubevent_addon (id, competition_type) VALUES (:eventId,:eventtype)");
			} else {
				query = entityManager
						.createNativeQuery("UPDATE clubevent_addon SET competition_type=:eventtype WHERE id=:eventId");
			}

			query.setParameter("eventId", obj.getId());
			query.setParameter("eventtype", type.getType().name());
			query.executeUpdate();
		}
	}

}
