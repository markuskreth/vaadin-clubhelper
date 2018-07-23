package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.text.DateFormat;
import java.util.Collection;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonGrid extends CustomComponent {

	private static final long serialVersionUID = -8148097982839343673L;

	private final DateFormat birthFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM);

	private final Grid<Person> grid = new Grid<>();

	private CheckBox checkIncluded;
	private ComboBox<GroupDef> comboGroups;

	public PersonGrid() {
		checkIncluded = new CheckBox("Nur gemeldete");
		comboGroups = new ComboBox<>("Gruppenfilter");

		HorizontalLayout filters = new HorizontalLayout();
		filters.addComponents(checkIncluded, comboGroups);

		grid.addColumn(Person::getPrename).setCaption("Vorname");
		grid.addColumn(Person::getSurname).setCaption("Nachname");
		grid.addColumn(Person::getBirth,
				b -> b != null ? birthFormat.format(b) : "")
				.setCaption("Geburtstag");
		grid.setSelectionMode(SelectionMode.MULTI);

		VerticalLayout panel = new VerticalLayout();
		panel.addComponents(filters, grid);
		setCompositionRoot(panel);
	}

	public void setItems(Collection<Person> items) {
		grid.setItems(items);
	}

	public void setItems(Person... items) {
		grid.setItems(items);
	}
}
