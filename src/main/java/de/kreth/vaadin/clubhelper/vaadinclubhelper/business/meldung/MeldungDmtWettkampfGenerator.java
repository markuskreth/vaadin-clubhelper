package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

class MeldungDmtWettkampfGenerator extends AbstractMeldungGenerator {

	@Override
	public void personRepresentation(StringBuilder txt, Altersgruppe g, Person p) {
		txt.append("\n").append(p.getPrename()).append(" ").append(p.getSurname()).append("\t")
				.append(p.getBirth().getYear()).append("\t");
		Startpass startpass = p.getStartpass();
		if (startpass != null) {
			txt.append(p.getStartpass().getStartpassNr());
		}
	}

}
