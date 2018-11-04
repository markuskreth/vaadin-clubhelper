package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.Month;

import org.junit.jupiter.api.Test;

class YearTest {

	@Test()
	void test18thCenturyEx() {
		assertThrows(IllegalArgumentException.class, () -> new Year(1899));
		assertThrows(IllegalArgumentException.class, () -> new Year(2101));
		new Year(1900);
		new Year(2100);
	}

	@Test
	void testMondayIsFirst() {
		Year y = new Year(2018);
		assertEquals(Short.valueOf((short)1), y.getDay(Month.OCTOBER, (short)1, DayOfWeek.MONDAY));
	}

	@Test
	void testThursdayIsFirst() {
		Year y = new Year(2018);
		assertNull(y.getDay(Month.NOVEMBER, (short)1, DayOfWeek.MONDAY));
	}

}
