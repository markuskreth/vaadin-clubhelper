package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
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

	protected static SessionFactory sessionFactory;
	protected static Session session;

	@BeforeAll
	public static void setUpDatabase() throws Exception {

		// setup the session factory
		Configuration configuration = createConfig();

		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();

	}

	public static Configuration createConfig() {
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
		configuration.addInputStream(AbstractDatabaseTest.class.getResourceAsStream("/schema/ClubEvent.hbm.xml"));

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");
		return configuration;
	}

	public enum DB_TYPE {
		MYSQL("SET @@foreign_key_checks = 0", "SET @@foreign_key_checks = 1"),
		H2("SET REFERENTIAL_INTEGRITY FALSE", "SET REFERENTIAL_INTEGRITY TRUE");

		private DB_TYPE(String disableReferentialIntegrety, String enableReferentialIntegrety) {
			this.disableReferentialIntegrety = disableReferentialIntegrety;
			this.enableReferentialIntegrety = enableReferentialIntegrety;
		}

		final String disableReferentialIntegrety;
		final String enableReferentialIntegrety;
	}

	@BeforeEach
	public void cleanH2Database() {
		session.doWork(conn -> {
			AbstractDatabaseTest.cleanDatabase(conn, DB_TYPE.H2);
		});
	}

	public static void cleanDatabase(Connection connection, DB_TYPE type) throws SQLException {

		String TABLE_NAME = "TABLE_NAME";
		String[] TABLE_TYPES = { "TABLE" };
		Set<String> tableNames = new HashSet<>();

		try (ResultSet tables = connection.getMetaData().getTables(null, null, null, TABLE_TYPES)) {
			while (tables.next()) {
				tableNames.add(tables.getString(TABLE_NAME));
			}
		}

		try (Statement stm = connection.createStatement()) {
			stm.execute(type.disableReferentialIntegrety);
			try {
				for (String tableName : tableNames) {
					try {
						stm.executeUpdate("TRUNCATE TABLE " + tableName);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			} finally {
				stm.execute(type.enableReferentialIntegrety);
			}
		}

		if (connection.getAutoCommit() == false) {
			connection.commit();
		}
	}

	/**
	 * executes the given runnable within a jpa Transaction.
	 * 
	 * @param r
	 */
	protected void transactional(Runnable r) {
		EntityTransaction tx = session.getTransaction();
		tx.begin();
		try {
			r.run();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}
}
