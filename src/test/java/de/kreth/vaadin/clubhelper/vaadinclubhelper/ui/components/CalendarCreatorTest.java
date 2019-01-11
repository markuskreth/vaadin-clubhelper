package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import com.ibm.icu.util.Calendar;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper.DateMetadata;

class CalendarCreatorTest {

	@Test
	void testMetadataDays() {
		DateMetadata meta = new DateMetadata(new GregorianCalendar(2018, Calendar.AUGUST, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(31, meta.getDays());

		meta = new DateMetadata(new GregorianCalendar(2018, Calendar.SEPTEMBER, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(30, meta.getDays());
		
		meta = new DateMetadata(new GregorianCalendar(2018, Calendar.FEBRUARY, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(28, meta.getDays());
	}

	@Test
	void testMetadataCells() {
		DateMetadata meta = new DateMetadata(new GregorianCalendar(2018, Calendar.AUGUST, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(34, meta.getCells());

		meta = new DateMetadata(new GregorianCalendar(2018, Calendar.SEPTEMBER, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(36, meta.getCells());
		
		meta = new DateMetadata(new GregorianCalendar(2018, Calendar.FEBRUARY, 21, 17, 11, 13).getTimeInMillis());
		assertEquals(32, meta.getCells());
	}

}
