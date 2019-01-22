package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public interface MeldungGenerator {

	CharSequence printMeldung(ClubEvent event, Map<Altersgruppe, List<Person>> groups);

}