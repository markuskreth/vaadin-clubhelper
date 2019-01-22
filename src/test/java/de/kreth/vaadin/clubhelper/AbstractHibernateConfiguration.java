package de.kreth.vaadin.clubhelper;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.BaseEntity;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;

public abstract class AbstractHibernateConfiguration implements HibernateConfiguration {

	private Set<Class<?>> entityClasses;

	public AbstractHibernateConfiguration() {
		Reflections reflections = new Reflections("de.kreth.vaadin.clubhelper.vaadinclubhelper.data");
		entityClasses = new HashSet<>(reflections.getSubTypesOf(BaseEntity.class));
		entityClasses.add(ClubeventHasPerson.class);
		entityClasses.add(CompetitionType.class);
	}

	@Override
	public void configure(Configuration configuration) {

		for (Class<?> entityClass : entityClasses) {
			configuration.addAnnotatedClass(entityClass);
		}

		configuration.addInputStream(getClass().getResourceAsStream("/schema/ClubEvent.hbm.xml"));

		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration.setProperty("spring.jpa.hibernate.ddl-auto", "update");
	}

}
