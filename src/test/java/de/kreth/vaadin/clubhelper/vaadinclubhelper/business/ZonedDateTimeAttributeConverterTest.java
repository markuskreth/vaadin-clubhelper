package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZonedDateTimeAttributeConverterTest {

	private ZonedDateTimeAttributeConverter converter;

	@BeforeEach
	void setUp() throws Exception {
		this.converter = new ZonedDateTimeAttributeConverter();
	}

	@Test
	void testZonedToDate() {
		ZonedDateTime zonedDatetime = ZonedDateTime.of(2019, 4, 25, 21, 43, 13, 0, ZoneId.systemDefault());
		Date actualDate = converter.convertToDatabaseColumn(zonedDatetime);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(actualDate);
		assertEquals(21, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(43, calendar.get(Calendar.MINUTE));
		assertEquals(13, calendar.get(Calendar.SECOND));
		assertEquals(0, calendar.get(Calendar.MILLISECOND));
	}

	@Test
	void testDateToZoned() {
		GregorianCalendar calendar = new GregorianCalendar();
		ZonedDateTime zonedDateTime = converter.convertToEntityAttribute(calendar.getTime());
		assertNotNull(zonedDateTime);
		assertEquals(calendar.get(Calendar.HOUR_OF_DAY), zonedDateTime.get(ChronoField.HOUR_OF_DAY));
		assertEquals(calendar.get(Calendar.MINUTE), zonedDateTime.get(ChronoField.MINUTE_OF_HOUR));
		assertEquals(calendar.get(Calendar.SECOND), zonedDateTime.get(ChronoField.SECOND_OF_MINUTE));
		assertEquals(calendar.get(Calendar.MILLISECOND), zonedDateTime.get(ChronoField.MILLI_OF_SECOND));
	}

}
