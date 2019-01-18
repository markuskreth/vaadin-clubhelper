package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDaoImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDaoImpl;

@Configuration
public class TestConfiguration {

	private SessionFactory sessionFactory;

	public TestConfiguration() {
		org.hibernate.cfg.Configuration configuration = HibernateHolder.configuration();
		sessionFactory = configuration.buildSessionFactory();
	}

	@Bean
	public EntityManager getEntityManager() {
		return sessionFactory.openSession();
	}

	@Bean
	public ClubEventDao getClubEventDao() {
		return new ClubEventDaoImpl();
	}

	@Bean
	public AltersgruppeDao getAltersgruppeDao() {
		return new AltersgruppeDaoImpl();
	}

	@Bean
	public EventBusiness getEventBusiness() {
		return new EventBusiness();
	}

}
