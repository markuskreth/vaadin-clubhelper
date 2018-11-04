package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

public class MonthlyCalendarCreator<T extends CharSequence> extends CalendarCreator {

	private final YearMonth yearMonthObject;
	private Map<Integer, T> values;
	private Collection<Integer> holidays;

	public MonthlyCalendarCreator(Date date) {
		this.yearMonthObject = YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public MonthlyCalendarCreator<T> setValues(Map<Integer, T> values) {
		this.values = values;
		return this;
	}

	public MonthlyCalendarCreator<T> setHolidays(Collection<Integer> holidays) {
		this.holidays = holidays;
		return this;
	}

	@Override
	protected void fillParameterMap(Map<String, Object> parameters) {

		Timestamp timestamp = new Timestamp(
				yearMonthObject.atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		parameters.put("Date", timestamp);
	}

	@Override
	protected InputStream jrxmlResource() {
		return CalendarCreator.class.getResourceAsStream("/jasper/calendar_month.jrxml");
	}

	@Override
	protected JRDataSource getSource() {
		return new MonthlyCalendarSource<>(yearMonthObject, values, holidays);
	}

}