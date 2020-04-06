package de.kreth.vaadin.clubhelper;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.PersonNote;

public abstract class AbstractHibernateConfiguration implements HibernateConfiguration {

	private Set<Class<?>> entityClasses;

	public AbstractHibernateConfiguration() {
		Reflections reflections = new Reflections("de.kreth.vaadin.clubhelper.vaadinclubhelper.data");
		entityClasses = new HashSet<>(reflections.getTypesAnnotatedWith(Entity.class));
		entityClasses.add(ClubeventHasPerson.class);
		entityClasses.add(CompetitionType.class);
		entityClasses.add(PersonNote.class);

	}

	@Override
	public void configure(Configuration configuration) {

		for (Class<?> entityClass : entityClasses) {
			configuration.addAnnotatedClass(entityClass);
		}

		InputStream resourceAsStream = getClass().getResourceAsStream("/schema/ClubEvent.hbm.xml");
		configuration.addInputStream(resourceAsStream);

		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration.setProperty("spring.jpa.hibernate.ddl-auto", "update");
	}

}
