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

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events.DataUpdatedEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events.DefaultDataUpdateHandler;

public class PersonFilter implements SerializablePredicate<Person>, DataProviderListener<Person> {

	private static final long serialVersionUID = -8481035921020651601L;

	private Set<Integer> selectedPersons = null;

	private Set<GroupDef> selectedGroups = null;

	private final List<Person> publishedList;

	private final PersonBusiness personDao;

	private final DefaultDataUpdateHandler updateHandler;

	private String nameFilter;

	public PersonFilter(PersonBusiness personDao2) {
		this.personDao = personDao2;
		publishedList = new ArrayList<>(personDao2.listAll());
		this.updateHandler = new DefaultDataUpdateHandler();
	}

	@Override
	public boolean test(Person t) {
		if (selectedGroups == null && selectedPersons == null && nameFilter == null) {
			return true;
		}

		if (personSelected(t) == false) {
			return false;
		}
		if (personInGroup(t) == false) {
			return false;
		}
		if (personNameMatch(t) == false) {
			return false;
		}
		return true;
	}

	private boolean personNameMatch(Person t) {
		if (nameFilter == null || nameFilter.isBlank()) {
			return true;
		}
		boolean contains = t.getPrename().toLowerCase().contains(nameFilter)
				|| t.getSurname().toLowerCase().contains(nameFilter);
		return contains;
	}

	private boolean personInGroup(Person t) {
		if (selectedGroups != null) {
			return t.hasAnyGroup() == false && haveCommonGroup(t, selectedGroups);
		}
		return true;
	}

	private boolean haveCommonGroup(Person p, Set<GroupDef> selectedGroups2) {

		for (GroupDef g2 : selectedGroups2) {
			if (p.hasGroup(g2)) {
				return true;
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

	public void setNameFilter(String value) {
		if (value != null) {
			this.nameFilter = value.toLowerCase();
		}
		else {
			this.nameFilter = value;
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
		List<Person> listAll = personDao.listAll();
		List<Person> filtered = listAll.stream().filter(this).collect(Collectors.toList());
		publishedList.addAll(filtered);
		updateHandler.fireUpdateEvent();
	}

	public void add(DataUpdatedEvent ev) {
		updateHandler.add(ev);
	}

	public boolean remove(DataUpdatedEvent o) {
		return updateHandler.remove(o);
	}

}
