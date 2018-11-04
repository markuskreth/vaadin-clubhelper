package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class Year {

	private final LocalDate date;
	private final Locale locale;

	public Year(int year) {
		this(year, Locale.getDefault());
	}
	
	public Year(int year, Locale locale) {
		if (year < 1900 || year >2100) {
			throw new IllegalArgumentException("Year value must be between 1900 and 2100");
		}
		this.date = LocalDate.of(year, 1, 1);
		this.locale = locale;
	}
	
	public int getYear() {
		return date.getYear();
	}
	
	public CharSequence getMonth(Month month) {
		return month.getDisplayName(TextStyle.FULL_STANDALONE, locale);
	}
	
	List<Integer> days = Arrays.asList(null, null, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,28,30,31,32);
	Queue<Integer> queue = new LinkedList<>(days);
	
	/**
	 * Day of month, numeric for the specified by parameters.
	 * @param month	month of the day
	 * @param week	1-5th week of the month
	 * @param dayOfWeek	weekday of the week in the month.
	 * @return	numeric value of the day of the month.
	 */
	public String getDay(Month month, short week, DayOfWeek dayOfWeek) {
		if (queue.isEmpty()) {
			queue.addAll(days);
		}
		Integer poll = queue.poll();
		if (poll != null) {
			return poll.toString();
		} else {
			return "";
		}
	}
	
}
