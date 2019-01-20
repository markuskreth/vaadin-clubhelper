package de.kreth.vaadin.clubhelper;

import org.hibernate.cfg.Configuration;

public class H2MemoryConfiguration extends AbstractHibernateConfiguration {

	@Override
	public void configure(Configuration configuration) {
		super.configure(configuration);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		configuration.setProperty("hibernate.connection.url", getUrl());
	}

	public String getUrl() {
		return "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE";
	}

}
