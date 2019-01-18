package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

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
		return txt.toString();
	}
}
