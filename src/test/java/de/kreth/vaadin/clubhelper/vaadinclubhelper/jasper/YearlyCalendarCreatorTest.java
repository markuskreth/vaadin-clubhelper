package de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;

class YearlyCalendarCreatorTest {

	private Map<LocalDate, CharSequence> values;

	private YearlyCalendarCreator creator;

	@BeforeEach
	void setUp() throws Exception {
		values = new HashMap<LocalDate, CharSequence>();
	}

	@Test
	void testGetSource() throws JRException {
		creator = new YearlyCalendarCreator(2019, values);
		JRDataSource source = creator.getSource();
		Integer month = 1;
		JRDesignField jrField = new JRDesignField();
		jrField.setName("MONTH_COUNTER");
		for (; month <= 12; month++) {
			assertTrue(source.next());
			Object value = source.getFieldValue(jrField);
			assertEquals(month, value);
		}
	}

	@Test
	void testFillParameterMap() {
		creator = new YearlyCalendarCreator(2019, values);
		Map<String, Object> parameters = new HashMap<String, Object>();
		creator.fillParameterMap(parameters);
		assertEquals(1, parameters.size());
	}

	@Test
	@Disabled
	void testJrxmlResource() {
		creator = new YearlyCalendarCreator(2019, values);
		InputStream jrxml = creator.jrxmlResource();
		assertNotNull(jrxml);
	}

	@Test
	void testFilterHoliday() {
		List<ClubEvent> allevents = new ArrayList<ClubEvent>();
		allevents.add(new ClubEventBuilder().withAllDay(true).withCaption("Ferien1")
				.withOrganizerDisplayName(YearlyCalendarCreator.HOLIDAY_CALENDAR)
				.withStart(ZonedDateTime.of(2019, 2, 3, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2019, 2, 12, 0, 0, 0, 0, ZoneId.systemDefault()))
				.build());
		allevents.add(new ClubEventBuilder().withAllDay(false).withCaption("caption")
				.withOrganizerDisplayName("organizerDisplayName")
				.withStart(ZonedDateTime.of(2019, 4, 3, 10, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2019, 4, 3, 16, 30, 0, 0, ZoneId.systemDefault()))
				.build());
		allevents.add(new ClubEventBuilder().withAllDay(true).withCaption("Ferien2")
				.withOrganizerDisplayName(YearlyCalendarCreator.HOLIDAY_CALENDAR)
				.withStart(ZonedDateTime.of(2019, 5, 3, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2019, 5, 12, 0, 0, 0, 0, ZoneId.systemDefault()))
				.build());
		allevents.add(new ClubEventBuilder().withAllDay(false).withCaption("caption2")
				.withOrganizerDisplayName("organizerDisplayName2")
				.withStart(ZonedDateTime.of(2019, 7, 3, 10, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2019, 7, 3, 17, 30, 0, 0, ZoneId.systemDefault()))
				.build());
		List<LocalDate> holidays = YearlyCalendarCreator.filterHolidays(allevents);
		assertEquals(20, holidays.size()); // Jeder einzenle Tag enthalten
		assertEquals(2, allevents.size()); // Urlaub-Events entfernt.

	}
}
