package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class AltersgruppePersonMap {

	private final Map<Altersgruppe, Map<Gender, List<Person>>> groups;

	private AltersgruppePersonMap(ClubEvent event) {
		this.groups = new HashMap<>();
		for (Person p : event.getPersons()) {
			group(event, p);
		}
	}

	public static AltersgruppePersonMap parse(ClubEvent event) {
		return new AltersgruppePersonMap(event);
	}

	private void group(ClubEvent event, Person p) {
		for (Altersgruppe g : event.getAltersgruppen()) {
			if (g.isBetween(p.getBirth())) {
				if (!groups.containsKey(g)) {
					groups.put(g, new HashMap<>());
				}
				Map<Gender, List<Person>> map = groups.get(g);
				if (!map.containsKey(p.getGender())) {
					map.put(p.getGender(), new ArrayList<>());
				}
				map.get(p.getGender()).add(p);
				return;
			}
		}
		throw new IllegalStateException("No Group found for " + p);
	}

	public Collection<Altersgruppe> altersgruppen() {
		return groups.keySet();
	}

	public Map<Gender, List<Person>> getGenderMapFor(Altersgruppe g) {
		return groups.get(g);
	}

	@Override
	public String toString() {
		return groups.toString();
	}
}
