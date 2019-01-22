package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class MeldungEinzelWettkampfGenerator implements MeldungGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kreth.vaadin.clubhelper.vaadinclubhelper.business.MeldungGenerator#
	 * printMeldung()
	 */
	@Override
	public CharSequence printMeldung(ClubEvent event, Map<Altersgruppe, List<Person>> groups) {
		StringBuilder txt = new StringBuilder();
		txt.append("\n\n für den Wettkampf ");
		txt.append(event.getCaption()).append(" am ").append(event.getStart().toLocalDate().toString())
				.append(" melden wir für den MTV Groß-Buchholz folgende Starter:");
		List<Altersgruppe> groupList = new ArrayList<>(groups.keySet());
		groupList.sort((o1, o2) -> Integer.compare(o2.getStart(), o1.getStart()));
		for (Altersgruppe g : groupList) {
			txt.append("\n\n").append(g.getBezeichnung());
			for (Person p : groups.get(g)) {
				txt.append("\n").append(p.getPrename()).append(" ").append(p.getSurname()).append("\t")
						.append(p.getBirth().getYear()).append("\t").append(g.getPflicht().getName());
			}
		}
		return txt;
	}
}
