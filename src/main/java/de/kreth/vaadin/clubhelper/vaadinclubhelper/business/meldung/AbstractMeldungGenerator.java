package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public abstract class AbstractMeldungGenerator implements MeldungGenerator {

	public AbstractMeldungGenerator() {
		super();
	}

	public abstract void personRepresentation(StringBuilder txt, Altersgruppe g, Person p);

	@Override
	public CharSequence printMeldung(ClubEvent event, AltersgruppePersonMap groups) {
		StringBuilder txt = new StringBuilder();
		txt.append("\n\n für den Wettkampf ");
		txt.append(event.getCaption()).append(" am ").append(event.getStart().toLocalDate().toString())
				.append(" melden wir für den MTV Groß-Buchholz folgende Starter:");
		List<Altersgruppe> groupList = new ArrayList<>(groups.altersgruppen());
		groupList.sort((o1, o2) -> Integer.compare(o2.getStart(), o1.getStart()));
		for (Altersgruppe g : groupList) {
			txt.append("\n\n").append(g.getBezeichnung());
			Map<Gender, List<Person>> list = groups.getGenderMapFor(g);
			for (Gender sex : list.keySet()) {
				txt.append("\n\t").append(sex.localized());
				for (Person p : list.get(sex)) {
					personRepresentation(txt, g, p);
				}
			}
		}
		return txt;
	}

}