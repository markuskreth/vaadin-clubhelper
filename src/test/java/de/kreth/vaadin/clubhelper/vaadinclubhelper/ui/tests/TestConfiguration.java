package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import de.kreth.vaadin.clubhelper.HibernateHolder;

@SpringBootConfiguration
@ComponentScan(basePackages = { "de.kreth" })
@EnableAutoConfiguration
public class TestConfiguration {

	static {
		System.setProperty("spring.config.location", "classpath:test.properties");
		System.setProperty("spring.config.name", "test");
	}

	private SessionFactory sessionFactory;

	public TestConfiguration() {
		sessionFactory = HibernateHolder.sessionFactory();
	}

	@Bean
	public EntityManager entityManager() {
		return sessionFactory.openSession();
	}

}
