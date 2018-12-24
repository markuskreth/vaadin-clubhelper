package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import javax.persistence.EntityManager;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public interface ClubEventDao extends IDao<ClubEvent> {

	void setEntityManager(EntityManager em);

	EntityManager getEntityManager();

}
