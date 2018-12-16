package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Attendance;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.DeletedEntry;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Persongroup;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relative;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpaesse;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.StartpassStartrechte;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Version;

public abstract class AbstractDatabaseTest {

	protected SessionFactory sessionFactory;
	protected Session session;

	@BeforeEach
	public void setUp() throws Exception {

		// setup the session factory
		Configuration configuration = createConfig();

		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();

	}

	public Configuration createConfig() {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Adress.class);
		configuration.addAnnotatedClass(Attendance.class);
		configuration.addAnnotatedClass(Contact.class);
		configuration.addAnnotatedClass(DeletedEntry.class);
		configuration.addAnnotatedClass(GroupDef.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Persongroup.class);
		configuration.addAnnotatedClass(Relative.class);
		configuration.addAnnotatedClass(Startpaesse.class);
		configuration.addAnnotatedClass(StartpassStartrechte.class);
		configuration.addAnnotatedClass(Version.class);
		configuration.addInputStream(getClass().getResourceAsStream("/schema/ClubEvent.hbm.xml"));

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");
		return configuration;
	}

}
