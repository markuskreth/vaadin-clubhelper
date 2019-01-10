package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.springframework.lang.NonNull;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar.Year;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class YearlyCalendarCreator extends CalendarCreator {

	private Year year;

	public YearlyCalendarCreator(int year, Map<LocalDate, CharSequence> values) {
		this(year, values, Collections.emptyList());
	}

	public YearlyCalendarCreator(int year, @NonNull Map<LocalDate, CharSequence> values,
			@NonNull Collection<LocalDate> holidays) {
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
