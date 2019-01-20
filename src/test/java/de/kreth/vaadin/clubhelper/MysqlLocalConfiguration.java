package de.kreth.vaadin.clubhelper;

import org.hibernate.cfg.Configuration;

public class MysqlLocalConfiguration extends AbstractHibernateConfiguration {

	@Override
	public void configure(Configuration configuration) {
		super.configure(configuration);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
//		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", getUrl());
		configuration.setProperty("hibernate.connection.username", getUsername());
		configuration.setProperty("hibernate.connection.password", getPassword());
	}

	public String getPassword() {
		return "0773";
	}

	public String getUsername() {
		return "markus";
	}

	public String getUrl() {
		return "jdbc:mysql://localhost/test?useUnicode=yes&characterEncoding=utf8&serverTimezone=Europe/Berlin";
	}

}
