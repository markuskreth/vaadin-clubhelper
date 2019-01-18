package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Pflicht;

public class TestAltersgruppen {

	static final AtomicInteger idGenerator = new AtomicInteger(1);

	public static List<Altersgruppe> getAltersgruppen() {
		List<Pflicht> pflichten = TestPflichten.getFixedPflichten();
		Collections.sort(pflichten);

		List<Altersgruppe> gruppen = new ArrayList<>();

		Altersgruppe g = new Altersgruppe();
		g.setBezeichnung("Schüler/innen Jahrg. 2013 und jünger");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(2013);
		g.setEnd(2099);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		g = new Altersgruppe();
		g.setBezeichnung("Schüler/innen Jahrg. 2010 - 2012");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(2010);
		g.setEnd(2012);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		g = new Altersgruppe();
		g.setBezeichnung("Schüler/innen Jahrg. 2004 und 2010");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(2004);
		g.setEnd(2010);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		g = new Altersgruppe();
		g.setBezeichnung("Schüler/innen Jahrg. 2000 und 2003");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(2000);
		g.setEnd(2003);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		g = new Altersgruppe();
		g.setBezeichnung("Heranwachsene Jahrg. 1994 bis 1999");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(1994);
		g.setEnd(1999);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		g = new Altersgruppe();
		g.setBezeichnung("Turner/innen Jahrg. 1900 und 1993");
		g.setId(idGenerator.getAndIncrement());
		g.setStart(1900);
		g.setEnd(1993);
		g.setPflicht(pflichten.get(g.getId()));
		gruppen.add(g);

		return gruppen;
	}
}
