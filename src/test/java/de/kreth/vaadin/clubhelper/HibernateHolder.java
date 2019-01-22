package de.kreth.vaadin.clubhelper;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum HibernateHolder {

	INSTANCE;
	private final Configuration configuration;

	private HibernateHolder() {
		configuration = new Configuration();
		HibernateConfiguration config = new H2MemoryConfiguration();
		config.configure(configuration);
	}

	public static Properties getProperties() {
		return INSTANCE.configuration.getProperties();
	}

	public static SessionFactory sessionFactory() {
		return INSTANCE.configuration.buildSessionFactory();
	}

}
