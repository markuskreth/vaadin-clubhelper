package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar.Year;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class YearlyCalendarCreator extends CalendarCreator {

	private Year year;

	public YearlyCalendarCreator(int year, Map<LocalDate, CharSequence> values) {
		this(year, values, Collections.emptyList());
	}

	public YearlyCalendarCreator(int year, Map<LocalDate, CharSequence> values, List<LocalDate> holidays) {
		if (values == null) {
			throw new NullPointerException("Calendar values must not be null!");
		}
		if (holidays == null) {
			throw new NullPointerException("holidays values must not be null!");
		}
		this.year = new Year(year, values, holidays);
	}

	@Override
	protected JRDataSource getSource() {
		return new CalendarSource();
	}

	@Override
	protected void fillParameterMap(Map<String, Object> parameters) {
		parameters.put("Year", year);
	}

	@Override
	protected InputStream jrxmlResource() {
		return CalendarCreator.class.getResourceAsStream("/jasper/calendar_year.jrxml");
	}

	public static class CalendarSource implements JRDataSource {

		Iterator<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).iterator();

		@Override
		public boolean next() throws JRException {
			return values.hasNext();
		}

		@Override
		public Object getFieldValue(JRField jrField) throws JRException {
			if ("MONTH_COUNTER".equals(jrField.getName())) {
				Integer next = values.next();
				return next;
			}
			return null;
		}

	}
}
