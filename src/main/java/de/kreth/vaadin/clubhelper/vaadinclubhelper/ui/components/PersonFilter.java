package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.server.SerializablePredicate;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonFilter implements SerializablePredicate<Person> {

	private static final long serialVersionUID = -8481035921020651601L;
	private Set<Integer> selectedPersons = null;
	private Set<GroupDef> selectedGroups = null;

	@Override
	public boolean test(Person t) {
		if (selectedGroups == null && selectedPersons == null) {
			return true;
		}

		if (personSelected(t) == false) {
			return false;
		}
		if (personInGroup(t) == false) {
			return false;
		}
		return true;
	}

	private boolean personInGroup(Person t) {
		if (selectedGroups != null) {
			return t.getGroups() != null && !Collections.disjoint(t.getGroups(), selectedGroups);
		}
		return true;
	}

	private boolean personSelected(Person t) {
		if (selectedPersons != null) {
			if (selectedPersons.contains(t.getId()) == false) {
				return false;
			}
		}
		return true;
	}

	public void setSelectedPersons(Set<Person> selected) {
		if (selected == null) {
			selectedPersons = null;
			return;
		}
		selectedPersons = new HashSet<>();
		for (Person p : selected) {
			selectedPersons.add(p.getId());
		}
	}

	public void setSelectedGroups(Set<GroupDef> selected) {
		this.selectedGroups = selected;
	}

}
