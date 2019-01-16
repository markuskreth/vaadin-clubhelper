package de.kreth.vaadin.clubhelper.vaadinclubhelper.ausschreibung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestPflichten;

class AltersgruppenParserTest {

	@Mock
	private PflichtenDao dao;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(dao.listAll()).thenReturn(TestPflichten.getFixedPflichten());
	}

	@Test
	void testBezirksDMT2019() {
		Altersgruppen bezDmt = Altersgruppen.parse(true, bezirksDMT2019());
		assertNotNull(bezDmt);
		List<Altersgruppe> gruppen = bezDmt.getValues();
		assertEquals(7, gruppen.size());

	}

	public String bezirksDMT2019() {
		return "Klasse Alter Jahrgang\r\n"

				+ "Schüler – innen F 8 u. Jünger 2011 und jünger\r\n"

				+ "Schüler – innen E 9-10 2009 - 2010\r\n"

				+ "Schüler – innen D 11-12 2007 - 2008\r\n"

				+ "Schüler – innen C 13-14 2005 - 2006\r\n"

				+ "Schüler – innen B 15-16 2003 - 2004\r\n"

				+ "Jugendturner - innen 17-18 2001 - 2002\r\n"

				+ "Turner - innen 19 Jahre u. älter 2000 und älter";
	}

	public String bezEinzelWK2019() {
		return "Klasse Jahrgang\r\n"

				+ "Schüler – innen G 2013 und jünger P3\r\n"

				+ "Schüler – innen F 2011 – 2012 P3\r\n"

				+ "Schüler – innen E 2009 – 2010 P3\r\n"

				+ "Schüler – innen D 2007 - 2008 P4\r\n"

				+ "Schüler – innen C 2005 - 2006 P4\r\n"

				+ "Schüler – innen B 2003 - 2004 P5\r\n"

				+ "Heranwachsende 2002-1998 P5\r\n"

				+ "Turner - innen 1997 -1990 P5";
	}

	public String bezEinzelMS2019() {
		return "Klasse Jahrgang\r\n"

				+ "Schüler – innen F 2011 und jünger P3\r\n"

				+ "Schüler – innen E 2009 – 2010 P4\r\n"

				+ "Schüler – innen D 2007 - 2008 P5\r\n"

				+ "Schüler – innen C 2005 - 2006 P6\r\n"

				+ "Schüler – innen B 2003 - 2004 P7\r\n"

				+ "Heranwachsende 2002-1998 P8\r\n"

				+ "Turner - innen 1997 -1990 P8\r\n"

				+ "Oldies 1989 und älter P5";
	}

	public String kreisWK2019() {
		return "Einsteigerwettkampf I				Einsteigerwettkampf II\r\n"
				+ "Jahrgangseinteilung/	Schüler/innen Jahrg. 2013 und jünger	P 3	Jahrg. 2012 und jünger	P 3\r\n"
				+ "Pflichtübungen:	Schüler/innen Jahrg. 2011 und 2012	P 3	Jahrg. 2009 bis 2011		P 3\r\n"
				+ "	Schüler/innen Jahrg. 2009 und 2010	P 3	Jahrg. 2006 bis 2008		P 3\r\n"
				+ "	Schüler/innen Jahrg. 2007 und 2008	P 3	Jahrg. 2003 bis 2005		P 3\r\n"
				+ "	Schüler/innen Jahrg. 2005 und 2006	P 3	Jahrg. 1994 bis 2002		P 4\r\n"
				+ "	Schüler/innen Jahrg. 2003 und 2004	P 3	Jahrg. 1993 und älter		P 4\r\n"
				+ "	Heranwachsene Jahrg. 2002 bis 1994	P 4\r\n" + "	Turner/innen Jahrg. 1993 und älter	P 4\r\n"
				+ "\r\n" + "";
	}
}
