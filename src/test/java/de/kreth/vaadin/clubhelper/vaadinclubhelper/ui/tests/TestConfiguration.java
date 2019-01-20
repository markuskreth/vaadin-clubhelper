package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDaoImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDaoImpl;

@Configuration
//@SpringBootConfiguration
//@EnableAutoConfiguration
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
	public EntityManager getEntityManager() {
		return sessionFactory.openSession();
	}

	@Bean
	public ClubEventDao getClubEventDao() {
		return new ClubEventDaoImpl();
	}

	@Bean
	public AltersgruppeDao getAltersgruppeDao() {
		return new AltersgruppeDaoImpl();
	}

	@Bean
	public EventBusiness getEventBusiness() {
		return new EventBusiness();
	}

}
