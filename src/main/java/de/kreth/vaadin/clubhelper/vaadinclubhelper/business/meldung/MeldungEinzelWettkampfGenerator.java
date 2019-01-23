package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class MeldungEinzelWettkampfGenerator extends AbstractMeldungGenerator {

	@Override
	public void personRepresentation(StringBuilder txt, Altersgruppe g, Person p) {
		txt.append("\n").append(p.getPrename()).append(" ").append(p.getSurname()).append("\t")
				.append(p.getBirth().getYear()).append("\t").append(g.getPflicht().getName());
	}

}
