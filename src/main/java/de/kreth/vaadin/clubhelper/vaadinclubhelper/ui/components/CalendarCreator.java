package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class CalendarCreator {

	private Date date;
	private YearMonth yearMonthObject;
	private int daysInMonth;

	public static JasperPrint createCalendar(Date date) throws JRException {
		return new CalendarCreator(date).createCalendar();
	}
	
	CalendarCreator(Date date) {
		this.date = date;
		
		yearMonthObject = YearMonth.from(date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
		
		daysInMonth = yearMonthObject.lengthOfMonth();
	}

	JasperPrint createCalendar() throws JRException {

		Map<String, Object> parameters = new HashMap<>();
		Timestamp timestamp = new Timestamp(date.getTime());
		parameters.put("Date", timestamp);
	
		InputStream jrxmlResource = CalendarCreator.class.getResourceAsStream("/jasper/calendar_month.jrxml");
		JasperReport report = JasperCompileManager
				.compileReport(jrxmlResource);
		JasperPrint print = JasperFillManager.fillReport(report,
				parameters, new CalendarSource(yearMonthObject.atDay(1).getDayOfWeek().getValue() -1, daysInMonth));

		return print;
	}

	private static class CalendarSource implements JRDataSource {
		
		private final List<Integer> days;
		private int index = -1;
		
		public CalendarSource(int prefix, int daysInMonth) {
			days = new ArrayList<>();
			for (int i=0, limit = daysInMonth + prefix; i<limit; i++) {
				days.add(i+1);
			}
		}
		
		@Override
		public boolean next() throws JRException {

			if (index+1>=days.size()) {
				return false;
			}
			index++;
			return true;
		}

		@Override
		public Object getFieldValue(JRField jrField) throws JRException {
			System.err.println(String.format("name=%s, type=%s, value=%d, parentProps=%s", jrField.getName(), jrField.getValueClass(), days.get(index), jrField.getParentProperties()));
			return days.get(index);
		}
		
	}
	
	public static void main(String[] args) throws JRException {
		
		Locale.setDefault(Locale.GERMANY);
		
		Calendar cal = new GregorianCalendar(2018, Calendar.AUGUST, 1);
		JasperViewer v1 = new JasperViewer(createCalendar(cal.getTime()));
		v1.setVisible(true);

		cal = new GregorianCalendar(2018, Calendar.SEPTEMBER, 1);
		JasperViewer v2 = new JasperViewer(createCalendar(cal.getTime()));
		v2.setVisible(true);
		
		cal = new GregorianCalendar(2018, Calendar.FEBRUARY, 1);
		JasperViewer v3 = new JasperViewer(createCalendar(cal.getTime()));
		v3.setVisible(true);
	}
	
	/**
	 * 
	 * @author markus
	 *
	 */
	public static class DateMetadata {
		
		private Calendar date;
		private int dow;
		private int dom;
		private int doy;
		private int days;
		private int cells;
		private int limit;

		public DateMetadata(java.sql.Date date) {
			this(toCalendar(date.getTime()));
		}

		public DateMetadata(long date) {
			this(toCalendar(date));
		}

		public DateMetadata(Calendar date) {
			this.date = (Calendar) date.clone();
			date.set(Calendar.DAY_OF_MONTH, 1);
			dow = date.get(Calendar.DAY_OF_WEEK);
			
//			dow =new Date(date.getYear(), date.getMonth(), 1).getDay();
			dom = date.get(Calendar.MONTH) + 1;
			doy = date.get(Calendar.YEAR);
			days = YearMonth.of(doy, dom).lengthOfMonth();
			cells = dow-1+days;
			limit = cells>28?(cells>35?42:35):28;
		}
		
		/**
		 * 
		 * @param date
		 */
		public DateMetadata(Date date) {
			this(toCalendar(date.getTime()));
		}

		public Date getDate() {
			return date.getTime();
		}

		public int getDow() {
			return dow;
		}

		public int getDom() {
			return dom;
		}

		public int getDoy() {
			return doy;
		}

		public int getDays() {
			return days;
		}

		public int getCells() {
			return cells;
		}

		public int getLimit() {
			return limit;
		}
		
		
	}
	
	private static Calendar toCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
	}
}
