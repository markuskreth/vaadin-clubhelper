package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

@Repository
public class ClubEventDaoImpl extends AbstractDaoImpl<ClubEvent> implements ClubEventDao {

	public ClubEventDaoImpl() {
		super(ClubEvent.class);
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
