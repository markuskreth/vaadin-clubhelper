package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class AddPersonCommand implements ClubCommand {

	private final Consumer<Person> createdPersonConsumer;

	private final GroupDef defaultGroup;

	public AddPersonCommand(Consumer<Person> createdPersonConsumer, GroupDef defaultGroup) {
		super();
		this.createdPersonConsumer = createdPersonConsumer;
		this.defaultGroup = defaultGroup;
	}

	@Override
	public String getLabel() {
		return "Person Hinzufügen";
	}

	@Override
	public Resource getIcon() {
		return null;
	}

	@Override
	public void execute() {
		Person person = new Person();

		person.setGroups(new HashSet<>());
		person.setAdresses(new ArrayList<>());
		person.setEvents(new HashSet<>());
		person.setRelatives1(new ArrayList<>());
		person.add(defaultGroup);

		createdPersonConsumer.accept(person);
	}

}
