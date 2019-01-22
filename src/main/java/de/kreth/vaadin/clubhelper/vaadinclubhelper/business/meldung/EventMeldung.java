package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class EventMeldung {

	private final ClubEvent event;
	private final Map<Altersgruppe, List<Person>> groups;

	public EventMeldung(ClubEvent event) {
		super();
		this.event = event;
		groups = new HashMap<>();
		for (Person p : event.getPersons()) {
			group(p);
		}
	}

	private void group(Person p) {
		for (Altersgruppe g : event.getAltersgruppen()) {
			if (g.isBetween(p.getBirth())) {
				if (!groups.containsKey(g)) {
					groups.put(g, new ArrayList<>());
				}
				groups.get(g).add(p);
				return;
			}
		}
		throw new IllegalStateException("No Group found for " + p);
	}

	public String getCaption() {
		return event.getCaption();
	}

	public Map<Altersgruppe, List<Person>> getGroups() {
		return Collections.unmodifiableMap(groups);
	}

	@Override
	public String toString() {
		return MeldungGeneratorFactory.forType(event.getType()).printMeldung(event, groups).toString();
	}

}
