package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class CompetitionGroupLineParseTest {

	@Test
	public void testTwo4DigitYears() {
		
		CompetitionGroup group = CompetitionGroup.parseLine("Schüler – innen E 2008 -2009 P4");
		assertEquals(2009, group.getYoungestBirthYear());
		
		group = CompetitionGroup.parseLine("Heranwachsende 2001-1995 P8");
		assertEquals(1995, group.getYoungestBirthYear());
		
		group = CompetitionGroup.parseLine("Jugend C: Jg. 2004/2005 W11 - W13");
		assertEquals(2005, group.getYoungestBirthYear());

		group = CompetitionGroup.parseLine("Schüler – innen E 2008 -2009 P4");
		assertEquals(2008, group.getOldestBirthYear());
		
		group = CompetitionGroup.parseLine("Jugend E: 2008 und jünger P8 - W11");
		assertEquals(2008, group.getOldestBirthYear());
		
		group = CompetitionGroup.parseLine("Heranwachsende 2001-1995 P8");
		assertEquals(2001, group.getOldestBirthYear());
		
		group = CompetitionGroup.parseLine("Jugend C: Jg. 2004/2005 W11 - W13");
		assertEquals(2004, group.getOldestBirthYear());

	}

	@Test
	public void testOneYearIsYoungest() {

		CompetitionGroup group = CompetitionGroup.parseLine("Erwachsene: Jg. 1996 und älter W15 - FIG A");
		assertEquals(1996, group.getYoungestBirthYear());
	}
	
	@Test
	public void testOneYearIsOldest() {
		CompetitionGroup group = CompetitionGroup.parseLine("Schüler – innen F 2010 und jünger P3");
		assertEquals(2010, group.getOldestBirthYear());

		group = CompetitionGroup.parseLine("Jugend E: 2008 und jünger P8 - W11");
		assertEquals(2008, group.getOldestBirthYear());
		
	}
	
	@Test
	public void testRegexPattern() {
		Pattern pattern = Pattern.compile("\\d{2,4}");
		String twoYears = "text 1999 bis 2009 text";
		Matcher matcher = pattern.matcher(twoYears);
		assertTrue("didnt find first year", matcher.find());
		assertEquals("1999", matcher.group());
		assertTrue("didnt find second year", matcher.find());
		assertEquals("2009", matcher.group());
	}
}
