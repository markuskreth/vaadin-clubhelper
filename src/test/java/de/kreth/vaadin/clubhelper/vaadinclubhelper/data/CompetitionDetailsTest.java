package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompetitionDetailsTest extends AbstractCompetitionDataTests {

	@Test
	public void parseGroupInsertsElementPerLine() throws Exception {
		
		CompetitionDetails detail = new CompetitionDetails();
		detail.parseCompetitionGroups(getGroupTable1());
		assertEquals(8, detail.getCpGroups().size());
		
		detail = new CompetitionDetails();
		detail.parseCompetitionGroups(getGroupTable2());
		assertEquals(6, detail.getCpGroups().size());
		
		
	}
}
