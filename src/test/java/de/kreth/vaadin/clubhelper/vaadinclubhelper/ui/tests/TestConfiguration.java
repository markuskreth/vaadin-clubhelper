package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.kreth.vaadin.clubhelper.HibernateHolder;

@Configuration
public class TestConfiguration {

	@Bean
	public EntityManager getEntityManager() throws Exception {

		// setup the session factory
		org.hibernate.cfg.Configuration configuration = HibernateHolder.configuration();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		return sessionFactory.openSession();

	}

}
