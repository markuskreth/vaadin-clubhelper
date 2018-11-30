package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.CalendarTaskRefresher;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDaoImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Attendance;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.DeletedEntry;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Persongroup;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relative;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpaesse;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.StartpassStartrechte;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Version;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public abstract class CalendarCreator {

	private static final String HOLIDAY_CALENDAR = "Schulferien";

	public static JasperPrint createCalendar(Date date) throws JRException {
		return new MonthlyCalendarCreator<CharSequence>(date).setValues(Collections.emptyMap())
				.setHolidays(Collections.emptyList()).createCalendar();
	}

	public static <T extends CharSequence> JasperPrint createCalendar(Date date, Map<Integer, T> values,
			Collection<Integer> holidays) throws JRException {
		return new MonthlyCalendarCreator<T>(date).setValues(values).setHolidays(holidays).createCalendar();
	}

	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> JasperPrint createCalendar(int year, Map<LocalDate, T> values,
			List<LocalDate> holidays) throws JRException {
		return new YearlyCalendarCreator(year, (Map<LocalDate, CharSequence>) values, holidays).createCalendar();
	}

	public final JasperPrint createCalendar() throws JRException {
		Map<String, Object> parameters = new HashMap<>();
		fillParameterMap(parameters);

		JasperReport report = JasperCompileManager.compileReport(jrxmlResource());

		return JasperFillManager.fillReport(report, parameters, getSource());

	}

	protected abstract JRDataSource getSource();

	protected abstract void fillParameterMap(Map<String, Object> parameters);

	protected abstract InputStream jrxmlResource();

	public static void main(String[] args) throws JRException {

		Locale.setDefault(Locale.GERMANY);

		List<ClubEvent> allevents = loadAllEvents(false);

		List<LocalDate> holidays = filterHolidays(allevents);

		Map<LocalDate, CharSequence> values = map(allevents, 2019);

		JasperViewer v1 = new JasperViewer(createCalendar(2019, values, holidays));
		v1.setVisible(true);

	}

	private static List<LocalDate> filterHolidays(List<ClubEvent> allevents) {
		List<LocalDate> holidays = new ArrayList<>();
		Iterator<ClubEvent> iter = allevents.iterator();
		while (iter.hasNext()) {
			ClubEvent item = iter.next();
			if (item.getOrganizerDisplayName().equals(HOLIDAY_CALENDAR)) {
				iter.remove();
				LocalDate start = item.getStart().toLocalDate();
				LocalDate end = item.getEnd().toLocalDate();
				while (end.isAfter(start) || end.isEqual(start)) {
					holidays.add(start);
					start = start.plusDays(1);
				}
			}
		}

		return holidays;
	}

	protected static Map<LocalDate, CharSequence> map(List<ClubEvent> allevents, int year) {
		Map<LocalDate, CharSequence> values = new HashMap<>();
		allevents.forEach(ev -> {

			LocalDate start = ev.getStart().toLocalDate();
			ZonedDateTime end = ev.getEnd();
			if (end != null) {
				LocalDate endDate = end.toLocalDate();
				if (endDate.getYear() >= year && start.getYear() <= year) {
					while (endDate.isAfter(start) || endDate.isEqual(start)) {
						concatDayValue(values, ev, start);
						start = start.plusDays(1);
					}
				}
			} else {
				if (start.getYear() == year) {
					concatDayValue(values, ev, start);
				}
			}

		});
		return values;
	}

	public static void concatDayValue(Map<LocalDate, CharSequence> values, ClubEvent ev, LocalDate start) {
		StringBuilder txt;
		if (values.get(start) != null) {
			txt = (StringBuilder) values.get(start);
		} else {
			txt = new StringBuilder();
			values.put(start, txt);
		}
		if (txt.length() > 0) {
			txt.append("\n");
		}
		txt.append(ev.getCaption());
	}

	public static List<ClubEvent> loadAllEvents(boolean withRefresh) {
		Configuration configuration = createConfig();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		ClubEventDaoImpl dao = new ClubEventDaoImpl();
		dao.setEntityManager(session);
		if (withRefresh) {
			updateEventsFromGoogleCalendar(session, dao);
		}
		return dao.listAll();
	}

	public static void updateEventsFromGoogleCalendar(Session session, ClubEventDaoImpl dao) {
		CalendarTaskRefresher refresh = new CalendarTaskRefresher();
		refresh.setDao(dao);
		Transaction tx = session.beginTransaction();
		refresh.synchronizeCalendarTasks();
		tx.commit();
	}

	static Calendar toCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
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
		configuration.addInputStream(CalendarCreator.class.getResourceAsStream("/schema/ClubEvent.hbm.xml"));

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url",
				"jdbc:mysql://localhost:3306/clubhelper?useUnicode=yes&characterEncoding=utf8&serverTimezone=Europe/Berlin");
		configuration.setProperty("hibernate.connection.username", "markus");
		configuration.setProperty("hibernate.connection.password", "0773");
		return configuration;
	}

}
