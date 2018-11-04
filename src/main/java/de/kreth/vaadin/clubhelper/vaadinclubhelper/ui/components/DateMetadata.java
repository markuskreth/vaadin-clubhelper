package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author markus
 *
 */
public class DateMetadata {
	
	private Calendar date;
	private int dow;
	private int dom;
	private int doy;
	private int days;
	private int cells;
	private int limit;

	public DateMetadata(java.sql.Date date) {
		this(CalendarCreator.toCalendar(date.getTime()));
	}

	public DateMetadata(long date) {
		this(CalendarCreator.toCalendar(date));
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
		this(CalendarCreator.toCalendar(date.getTime()));
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