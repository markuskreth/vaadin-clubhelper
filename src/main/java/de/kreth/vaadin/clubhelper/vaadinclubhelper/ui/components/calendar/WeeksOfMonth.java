package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeksOfMonth {

	private static final List<DayOfWeek> ORDERED_WEEKDAY = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
	private final YearMonth yearMonth;
	private final List<Map<DayOfWeek, Integer>> weeks;

	public WeeksOfMonth(Month month, int year) {
		this(YearMonth.of(year, month));
	}

	public WeeksOfMonth(YearMonth month) {
		this.yearMonth = month;
		weeks = Collections.unmodifiableList(createWeeks());
	}

	private List<Map<DayOfWeek, Integer>> createWeeks() {
		LocalDate day = yearMonth.atDay(1);

		List<Map<DayOfWeek, Integer>> result = new ArrayList<>();
		Map<DayOfWeek, Integer> currentWeek = new HashMap<>();
		
		int monthValue = yearMonth.getMonthValue();
		while (day.getMonthValue() == monthValue) {

			for (DayOfWeek d: ORDERED_WEEKDAY) {
				if (d.equals(day.getDayOfWeek()) && day.getMonthValue() == monthValue) {
					currentWeek.put(d, day.getDayOfMonth());
					day = day.plusDays(1);
				} else {
					currentWeek.put(d, null);
				}				
			}
			result.add(Collections.unmodifiableMap(currentWeek));
			currentWeek = new HashMap<>();
		}
		return result;
	}

	public int dayCount() {
		return yearMonth.lengthOfMonth();
	}

	/**
	 * Week in Month >=0 && < {@link #weekCount()} depending on day count and weekday order.
	 * @param index
	 * @return
	 */
	public Map<DayOfWeek, Integer> getWeek(int index) {
		return weeks.get(index);
	}

	/**
	 * Count of week (parts) in this month.
	 * @return
	 */
	public int weekCount() {
		return weeks.size();
	}
	
	
}
