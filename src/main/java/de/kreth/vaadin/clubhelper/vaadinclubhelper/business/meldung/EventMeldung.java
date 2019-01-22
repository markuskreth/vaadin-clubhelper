package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class EventMeldung {

	private final ClubEvent event;
	private final AltersgruppePersonMap groups;

	public EventMeldung(ClubEvent event) {
		super();
		this.event = event;
		groups = AltersgruppePersonMap.parse(event);
	}

	public String getCaption() {
		return event.getCaption();
	}

	public AltersgruppePersonMap getGroups() {
		return groups;
	}

	@Override
	public String toString() {
		return MeldungGeneratorFactory.forType(event.getType()).printMeldung(event, groups).toString();
	}

}
