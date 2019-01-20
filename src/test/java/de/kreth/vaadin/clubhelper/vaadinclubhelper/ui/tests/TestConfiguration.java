package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import de.kreth.vaadin.clubhelper.HibernateHolder;

//@Configuration
@SpringBootConfiguration
@ComponentScan(basePackages = { "de.kreth" })
@EnableAutoConfiguration
public class TestConfiguration {

	private SessionFactory sessionFactory;

	public TestConfiguration() {
		sessionFactory = HibernateHolder.sessionFactory();
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() throws Exception {

		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setHibernateProperties(HibernateHolder.getProperties());

		return sessionFactoryBean;
	}

	@Bean
	public EntityManager entityManager() {
		return sessionFactory.openSession();
	}

}
