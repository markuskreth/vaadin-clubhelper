package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public abstract class CalendarCreator {

	public static JasperPrint createCalendar(Date date) throws JRException {
		return new MonthlyCalendarCreator<CharSequence>(date).setValues(Collections.emptyMap()).setHolidays(Collections.emptyList()).createCalendar();
	}

	public static <T extends CharSequence> JasperPrint createCalendar(Date date, Map<Integer, T> values,
			Collection<Integer> holidays) throws JRException {
		return new MonthlyCalendarCreator<T>(date).setValues(values).setHolidays(holidays).createCalendar();
	}

	public static JasperPrint createCalendar(int year) throws JRException {
		return new YearlyCalendarCreator(year).createCalendar();
	}

	public final JasperPrint createCalendar() throws JRException {
		Map<String, Object> parameters = new HashMap<>();
		fillParameterMap(parameters);
		
		JasperReport report = JasperCompileManager.compileReport(jrxmlResource());

		JasperPrint print = JasperFillManager.fillReport(report, parameters,
				getSource());

		return print;
	}

	protected abstract JRDataSource getSource();
	
	protected abstract void fillParameterMap(Map<String, Object> parameters);
	
	protected abstract InputStream jrxmlResource();
	
	public static void main(String[] args) throws JRException {

		Locale.setDefault(Locale.GERMANY);

//		Calendar cal = new GregorianCalendar(2018, Calendar.DECEMBER, 1);
//
//		Map<Integer, String> events = new HashMap<>();
//		events.put(3, "Event at 3rd.");
//		events.put(23, "Event at 23th.");

//		JasperViewer v1 = new JasperViewer(
//				createCalendar(cal.getTime(), events, Arrays.asList(2, 3, 4, 5, 6, 22, 23, 24)));
//		v1.setVisible(true);
		JasperViewer v1 = new JasperViewer(
				createCalendar(2018));
		v1.setVisible(true);

//		cal = new GregorianCalendar(2018, Calendar.FEBRUARY, 1);
//		JasperViewer v3 = new JasperViewer(createCalendar(cal.getTime()));
//		v3.setVisible(true);
	}

	static Calendar toCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
	}
}
