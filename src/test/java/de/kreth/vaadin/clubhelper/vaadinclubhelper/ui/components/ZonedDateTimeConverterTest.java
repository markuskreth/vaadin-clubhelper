package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.data.Result;

class ZonedDateTimeConverterTest {

	private ZonedDateTimeConverter converter;

	@BeforeEach
	void setUp() throws Exception {
		converter = new ZonedDateTimeConverter();
	}

	@Test
	void testConvertToModelNull() {
		Result<ZonedDateTime> result = converter.convertToModel(null, null);
		assertNotNull(result);
		assertFalse(result.isError());
		assertNull(result.getOrThrow(msg -> new RuntimeException(msg)));
	}

	@Test
	void testConvertToModel() {
		LocalDate now = LocalDate.now();
		Result<ZonedDateTime> result = converter.convertToModel(now, null);
		assertNotNull(result);
		assertFalse(result.isError());
		ZonedDateTime actual = result.getOrThrow(msg -> new RuntimeException(msg));
		assertEquals(0, actual.get(ChronoField.HOUR_OF_DAY));
		assertEquals(0, actual.get(ChronoField.MINUTE_OF_DAY));
		assertEquals(0, actual.get(ChronoField.SECOND_OF_MINUTE));
		assertEquals(0, actual.get(ChronoField.MILLI_OF_SECOND));
	}

	@Test
	void testConvertToPresentation() {
		ZonedDateTime now = ZonedDateTime.now();
		assertEquals(now.toLocalDate(), converter.convertToPresentation(now, null));
	}

}
