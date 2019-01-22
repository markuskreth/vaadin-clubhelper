package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public interface MeldungGenerator {

	CharSequence printMeldung(ClubEvent event, AltersgruppePersonMap groups);

}