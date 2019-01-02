package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.server.SerializablePredicate;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonFilter implements SerializablePredicate<Person>, DataProviderListener<Person> {

	private static final long serialVersionUID = -8481035921020651601L;
	private Set<Integer> selectedPersons = null;
	private Set<GroupDef> selectedGroups = null;
	private final List<Person> publishedList;
	private final PersonDao personDao;

	public PersonFilter(PersonDao personDao) {
		this.personDao = personDao;
		publishedList = new ArrayList<>(personDao.listAll());
	}

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
			return t.getGroups() != null && haveCommonGroup(t.getGroups(), selectedGroups);
		}
		return true;
	}

	private boolean haveCommonGroup(Set<GroupDef> groups, Set<GroupDef> selectedGroups2) {

		for (GroupDef g1 : groups) {
			for (GroupDef g2 : selectedGroups2) {
				if (g1.getId() == g2.getId()) {
					return true;
				}
			}
		}
		return false;
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

	public Collection<Person> asCollection() {
		return publishedList;
	}

	@Override
	public void onDataChange(DataChangeEvent<Person> event) {
		publishedList.clear();
		publishedList.addAll(personDao.listAll().stream().filter(this).collect(Collectors.toList()));
	}

}
