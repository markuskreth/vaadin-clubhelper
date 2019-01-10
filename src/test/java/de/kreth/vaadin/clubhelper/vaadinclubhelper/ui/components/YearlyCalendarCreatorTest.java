package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class YearlyCalendarCreatorTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testYearlyCalendarCreatorIntMapOfLocalDateCharSequenceCollectionOfLocalDate() {
		assertThrows(NullPointerException.class, () -> new YearlyCalendarCreator(2018, null, null));
	}

}
