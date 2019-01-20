package de.kreth.vaadin.clubhelper;

import org.hibernate.cfg.Configuration;

public enum HibernateHolder {

	INSTANCE;
	private final Configuration configuration = createConfig();

	private org.hibernate.cfg.Configuration createConfig() {
		Configuration configuration = new Configuration();
		HibernateConfiguration config = new H2MemoryConfiguration();
		config.configure(configuration);

		return configuration;
	}

	public static Configuration configuration() {
		return INSTANCE.configuration;
	}
}
