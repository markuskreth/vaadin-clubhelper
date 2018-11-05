package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Map;

import org.junit.jupiter.api.Test;

class WeeksOfMonthTest {

	@Test
	void test2018November() {
		WeeksOfMonth november = new WeeksOfMonth(Month.NOVEMBER, 2018);
		assertNotNull(november);
		assertEquals(30, november.dayCount());
		assertEquals(5, november.weekCount());
		Map<DayOfWeek, Integer> week = november.getWeek(0);
		assertNotNull(week);
		assertEquals(7, week.size());
		assertNull(week.get(DayOfWeek.MONDAY));
		assertNull(week.get(DayOfWeek.TUESDAY));
		assertNull(week.get(DayOfWeek.WEDNESDAY));
		assertEquals(1, week.get(DayOfWeek.THURSDAY).intValue());
		assertEquals(2, week.get(DayOfWeek.FRIDAY).intValue());
		assertEquals(4, week.get(DayOfWeek.SUNDAY).intValue());
		
		week = november.getWeek(4);
		assertEquals(26, week.get(DayOfWeek.MONDAY).intValue());
		assertEquals(27, week.get(DayOfWeek.TUESDAY).intValue());
		assertEquals(28, week.get(DayOfWeek.WEDNESDAY).intValue());
		assertEquals(29, week.get(DayOfWeek.THURSDAY).intValue());
		assertEquals(30, week.get(DayOfWeek.FRIDAY).intValue());
		assertNull(week.get(DayOfWeek.SATURDAY));
		assertNull(week.get(DayOfWeek.SUNDAY));
	}

	@Test
	void testLastDayOfMonthIsMonday() {
		WeeksOfMonth april = new WeeksOfMonth(Month.APRIL, 2018);
		assertEquals(6, april.weekCount());

		Map<DayOfWeek, Integer> week = april.getWeek(0);
		assertNull(week.get(DayOfWeek.SATURDAY));
		assertEquals(1, week.get(DayOfWeek.SUNDAY).intValue());
		week = april.getWeek(5);
		assertEquals(30, week.get(DayOfWeek.MONDAY).intValue());
		assertNull(week.get(DayOfWeek.TUESDAY));
		
	}
}
