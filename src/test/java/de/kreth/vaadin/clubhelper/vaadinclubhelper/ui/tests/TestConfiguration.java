package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
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

	@Bean
	public CalendarAdapter getCalendarAdapter() throws GeneralSecurityException, IOException {
		return new CalendarAdapter();
	}

}
