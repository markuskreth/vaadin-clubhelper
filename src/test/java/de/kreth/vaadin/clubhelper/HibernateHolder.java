package de.kreth.vaadin.clubhelper;

import java.io.File;

import org.hibernate.cfg.Configuration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Attendance;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.DeletedEntry;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Persongroup;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Pflicht;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relative;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.StartpassStartrechte;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Version;

public enum HibernateHolder {

	INSTANCE;
	private final Configuration configuration = createConfig();

	private org.hibernate.cfg.Configuration createConfig() {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Adress.class);
		configuration.addAnnotatedClass(Altersgruppe.class);
		configuration.addAnnotatedClass(Attendance.class);
		configuration.addAnnotatedClass(Contact.class);
		configuration.addAnnotatedClass(DeletedEntry.class);
		configuration.addAnnotatedClass(GroupDef.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Persongroup.class);
		configuration.addAnnotatedClass(Pflicht.class);
		configuration.addAnnotatedClass(Relative.class);
		configuration.addAnnotatedClass(Startpass.class);
		configuration.addAnnotatedClass(StartpassStartrechte.class);
		configuration.addAnnotatedClass(Version.class);
		configuration.addInputStream(getClass().getResourceAsStream("/schema/ClubEvent.hbm.xml"));
		configuration.addAnnotatedClass(ClubeventHasPerson.class);

//		mysqlLocal(configuration);
		h2Memory(configuration);

		return configuration;
	}

	void h2File(Configuration configuration) {
		File f = new File("./database");
		System.out.println("Databasepath: " + f.getAbsolutePath());
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:" + f.getAbsolutePath());
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");
	}

	void mysqlTest(Configuration configuration) {
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
//		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url",
				"jdbc:mysql://localhost/test?useUnicode=yes&characterEncoding=utf8&serverTimezone=Europe/Berlin");
		configuration.setProperty("hibernate.connection.username", "markus");
		configuration.setProperty("hibernate.connection.password", "0773");
		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration.setProperty("spring.jpa.hibernate.ddl-auto", "update");
	}

	void mysqlLocal(Configuration configuration) {
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		configuration.setProperty("hibernate.connection.url",
				"jdbc:mysql://localhost/clubhelper?useUnicode=yes&characterEncoding=utf8&serverTimezone=Europe/Berlin");
		configuration.setProperty("hibernate.connection.username", "markus");
		configuration.setProperty("hibernate.connection.password", "0773");
	}

	void h2Memory(Configuration configuration) {

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

		configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE");
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");
	}

	public static Configuration configuration() {
		return INSTANCE.configuration;
	}
}
