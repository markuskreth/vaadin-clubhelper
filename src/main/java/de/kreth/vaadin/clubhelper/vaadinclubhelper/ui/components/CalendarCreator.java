package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

	private final YearMonth yearMonthObject;

	public static JasperPrint createCalendar(Date date) throws JRException {
		return new CalendarCreator(date).createCalendar(Collections.emptyMap());
	}

	public static <T extends CharSequence> JasperPrint createCalendar(Date date, Map<Integer, T> values) throws JRException {
		return new CalendarCreator(date).createCalendar(values);
	}
	
	CalendarCreator(Date date) {
		yearMonthObject = YearMonth
				.from(date.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate()
				);
		
	}

	 <T extends CharSequence> JasperPrint createCalendar(Map<Integer, T> values) throws JRException {

		Map<String, Object> parameters = new HashMap<>();
		Timestamp timestamp = new Timestamp(yearMonthObject.atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		
		parameters.put("Date", timestamp);
	
		InputStream jrxmlResource = CalendarCreator.class.getResourceAsStream("/jasper/calendar_month.jrxml");
		JasperReport report = JasperCompileManager
				.compileReport(jrxmlResource);

		JasperPrint print = JasperFillManager.fillReport(report,
				parameters, new CalendarSource<>(yearMonthObject, values));

		return print;
	}

	private static class CalendarSource<T extends CharSequence> implements JRDataSource {

		private final List<Integer> days;
		private final Map<Integer, T> dayContent;
		private int index = -1;
		private int prefix;
		
		public CalendarSource(YearMonth yearMonthObject, Map<Integer, T> dayContent) {

			days = new ArrayList<>();
			this.dayContent = dayContent;
			prefix = yearMonthObject.atDay(1).getDayOfWeek().getValue() -1;
			int daysInMonth = yearMonthObject.lengthOfMonth();
			
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
			if (jrField.getName().equals("id")) {
				return days.get(index);
			} else if (jrField.getName().equals("Field_Value") && days.get(index)>0) {
				T content = dayContent.get(index - prefix);
				if (content != null) {
					return content.toString();
				} else {
					return "";
				}
			} else {
				return "";
			}
		}
		
	}
	
	public static void main(String[] args) throws JRException {
		
		Locale.setDefault(Locale.GERMANY);
		
		Calendar cal = new GregorianCalendar(2018, Calendar.OCTOBER, 1);
		JasperViewer v1 = new JasperViewer(createCalendar(cal.getTime()));
		v1.setVisible(true);
		
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
			
			dom = date.get(Calendar.MONTH) + 1;
			doy = date.get(Calendar.YEAR);
			days = YearMonth.of(doy, dom).lengthOfMonth();
			cells = dow - 1 + days;
			limit = cells > 28 ? (cells>35?42:35) : 28;
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
