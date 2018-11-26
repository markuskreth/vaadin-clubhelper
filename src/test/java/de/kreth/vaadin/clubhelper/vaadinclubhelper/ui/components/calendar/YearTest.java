package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class YearTest {

	@Test()
	void test18thCenturyEx() {
		assertThrows(IllegalArgumentException.class, () -> new Year(1899, Collections.emptyMap()));
		assertThrows(IllegalArgumentException.class, () -> new Year(2101, Collections.emptyMap()));
		new Year(1900, Collections.emptyMap());
		new Year(2100, Collections.emptyMap());
	}

	@Test
	void testMondayIsFirst() {
		Year y = new Year(2018, Collections.emptyMap());
		assertEquals("1", y.getDay(Month.OCTOBER, (short) 1, DayOfWeek.MONDAY));
	}

	@Test
	void testThursdayIsFirst() {
		Year y = new Year(2018, Collections.emptyMap());
		assertEquals("", y.getDay(Month.NOVEMBER, (short) 1, DayOfWeek.MONDAY));
	}

}
