package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootConfiguration
@EnableAutoConfiguration
public class DatabaseTestBean {

	@Autowired
	private ResourceLoader rl;

	@Bean
	public LocalSessionFactoryBean sessionFactory() throws Exception {
		MyTestDatabase tdb = new MyTestDatabase();
		Configuration config = tdb.createConfig();
		
	    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
	    sessionFactoryBean.setHibernateProperties(config.getProperties());
//	    Class<?>[] dataClasses = {Adress.class, Attendance.class, Contact.class, DeletedEntry.class, GroupDef.class, Person.class, Persongroup.class, Relative.class, Startpaesse.class, StartpassStartrechte.class, Version.class};
//	    sessionFactoryBean.setMappingLocations(loadResources());
//	    sessionFactoryBean.setAnnotatedClasses(dataClasses);

	    return sessionFactoryBean;
	}

	public Resource[] loadResources() {
	    Resource[] resources = null;
	    try {
	        resources = ResourcePatternUtils.getResourcePatternResolver(rl)
	            .getResources("classpath:/schema/*.hbm.xml");
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	    return resources;
	}
	
	class MyTestDatabase extends AbstractDatabaseTest {

		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}
		
		public Session getSession() {
			return session;
		}
	}
}
