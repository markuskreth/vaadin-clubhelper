package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import de.kreth.vaadin.clubhelper.HibernateHolder;

@SpringBootConfiguration
@EnableAutoConfiguration
public class DatabaseTestBean {

	@Bean
	public LocalSessionFactoryBean sessionFactory() throws Exception {

		Configuration config = HibernateHolder.configuration();

		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setHibernateProperties(config.getProperties());

		return sessionFactoryBean;
	}

}
