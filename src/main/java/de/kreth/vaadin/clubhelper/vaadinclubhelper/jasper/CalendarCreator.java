package de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper;

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
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public abstract class CalendarCreator {

	static final String HOLIDAY_CALENDAR = "Schulferien";

	public static JasperPrint createCalendar(Date date) throws JRException {
		return new MonthlyCalendarCreator<CharSequence>(date).setValues(Collections.emptyMap())
				.setHolidays(Collections.emptyList()).createCalendar();
	}

	public static <T extends CharSequence> JasperPrint createCalendar(Date date, Map<LocalDate, T> values,
			Collection<LocalDate> holidays) throws JRException {
		return new MonthlyCalendarCreator<T>(date).setValues(values)
				.setHolidays(holidays.stream().map(h -> h.getDayOfMonth()).collect(Collectors.toList()))
				.createCalendar();
	}

	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> JasperPrint createYearCalendar(int year, Map<LocalDate, T> values,
			Collection<LocalDate> holidays) throws JRException {
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

	public static List<LocalDate> filterHolidays(List<ClubEvent> allevents) {
		List<LocalDate> holidays = new ArrayList<>();
		Iterator<ClubEvent> iter = allevents.iterator();
		while (iter.hasNext()) {
			ClubEvent item = iter.next();
			if (item.getOrganizerDisplayName().equals(HOLIDAY_CALENDAR)) {
				iter.remove();
				LocalDate start = item.getStart().toLocalDate();
				LocalDate end = item.getEnd().toLocalDate();
				iterateDays(start, end, d -> holidays.add(d));
			}
		}

		return holidays;
	}

	public static void iterateDays(LocalDate start, LocalDate end, Consumer<LocalDate> consumer) {
		while (end.isAfter(start) || end.isEqual(start)) {
			consumer.accept(start);
			start = start.plusDays(1);
		}
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
			}
			else {
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
		}
		else {
			txt = new StringBuilder();
			values.put(start, txt);
		}
		if (txt.length() > 0) {
			txt.append("\n");
		}
		txt.append(ev.getCaption());
	}

	public static Calendar toCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
	}

}
