package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Year {

	private final LocalDate date;
	private final Locale locale;
	private final Map<Month, WeeksOfMonth> monthWeeks;
	private final Map<LocalDate, CharSequence> values;

	public Year(int year) {
		this(year, Collections.emptyMap(), Locale.getDefault());
	}

	public Year(int year, Map<LocalDate, CharSequence> values) {
		this(year, values, Locale.getDefault());
	}

	public Year(int year, Map<LocalDate, CharSequence> values, Locale locale) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("Year value must be between 1900 and 2100");
		}
		this.date = LocalDate.of(year, 1, 1);
		this.locale = locale;
		this.monthWeeks = new HashMap<>();
		for (Month m : Month.values()) {
			monthWeeks.put(m, new WeeksOfMonth(m, year));
		}
		this.values = values;
		for (LocalDate d : values.keySet()) {
			System.out.println(d + "\t" + values.get(d));
		}
	}

	public int getYear() {
		return date.getYear();
	}

	public CharSequence getMonth(Month month) {
		return month.getDisplayName(TextStyle.FULL_STANDALONE, locale);
	}

	/**
	 * Day of month, numeric for the specified by parameters.
	 * 
	 * @param month     month of the day
	 * @param week      1-6th week of the month
	 * @param dayOfWeek weekday of the week in the month.
	 * @return numeric value of the day of the month.
	 */
	public String getDay(Month month, short week, DayOfWeek dayOfWeek) {
		Integer res = getDayOfMonth(month, week, dayOfWeek);

		return res == null ? "" : res.toString();

	}

	public Integer getDayOfMonth(Month month, short week, DayOfWeek dayOfWeek) {
		WeeksOfMonth weeksOfMonth = monthWeeks.get(month);
		if (week > weeksOfMonth.weekCount()) {
			return null;
		}

		return weeksOfMonth.getWeek(week - 1).get(dayOfWeek);
	}

	public CharSequence getContent(Month month, short week, DayOfWeek dayOfWeek) {
		Integer res = getDayOfMonth(month, week, dayOfWeek);
		LocalDate day = date.withMonth(month.getValue()).withDayOfMonth(res);
		return values.get(day);
	}
}
