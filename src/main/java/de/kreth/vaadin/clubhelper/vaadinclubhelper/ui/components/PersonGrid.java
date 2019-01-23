package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridMultiSelect;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

public class PersonGrid extends VerticalLayout {

	private static final long serialVersionUID = -8148097982839343673L;
	private final transient Logger log = LoggerFactory.getLogger(getClass());

	private final transient DateTimeFormatter birthFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
	private final ListDataProvider<Person> dataProvider;

	private final Grid<Person> grid;
	private final CheckBox checkIncluded;
	private final ComboBox<GroupDef> comboGroups;

	private Boolean selectedOnlyFilter;

	private Set<GroupDef> groupMemberFilter;
	private List<GroupDef> allGroups;
	private PersonFilter filter;
	private ClubEvent currentEvent;
	private Column<Person, Startpass> startpassColumn;
	private Layout filters;
	private SelectionMode currentSelectionMode;
	private Column<Person, String> genderColumn;

	public PersonGrid(GroupDao groupDao, PersonDao personDao) {

		setId("main.person");
		setCaption("Teilnehmer");
		addStyleName("bold-caption");

		checkIncluded = new CheckBox("Nur gemeldete");
		comboGroups = new ComboBox<>("Gruppenfilter");
		filters = setupFilterComponents();

		allGroups = groupDao.listAll();
		comboGroups.setItems(allGroups);
		log.info("Loaded Groups: {}", allGroups);

		filter = new PersonFilter(personDao);
		dataProvider = DataProvider.ofCollection(filter.asCollection());
		grid = new Grid<>();

		setupPersonGrid(personDao);

		setMargin(false);
		addComponents(filters, grid);
	}

	public void setupPersonGrid(PersonDao personDao) {
		filter.add(() -> {
			setEvent(currentEvent);
		});
		dataProvider.addDataProviderListener(filter);

		grid.setDataProvider(dataProvider);
		grid.setId("person.grid");
		grid.setSizeFull();

		grid.addColumn(Person::getPrename).setCaption("Vorname");
		grid.addColumn(Person::getSurname).setCaption("Nachname");
		grid.addColumn(Person::getBirth, b -> b != null ? birthFormat.format(b) : "").setCaption("Geburtstag");

		startpassColumn = grid.addColumn(Person::getStartpass).setCaption("Startpass Nr.");

		genderColumn = grid.addColumn(p -> {

			Gender gender = p.getGender();
			if (gender == null) {
				return "";
			}
			return gender.localized();
		}).setCaption("Geschlecht");

		startpassColumn.setHidden(false);
		genderColumn.setHidden(true);
	}

	public void setSelectionMode(SelectionMode selectionMode) {
		grid.setSelectionMode(selectionMode);
		currentSelectionMode = selectionMode;
	}

	private Layout setupFilterComponents() {
		checkIncluded.setId("person.filter.checked");
		checkIncluded.addValueChangeListener(ev -> onSelectedOnly(ev));
		comboGroups.setId("person.filter.groups");
		comboGroups.setEmptySelectionAllowed(true);
		comboGroups.setEmptySelectionCaption("Alle");
		comboGroups.setItemCaptionGenerator(GroupDef::getName);
		comboGroups.addSelectionListener(ev -> onGroupSelected(ev));

		HorizontalLayout filters = new HorizontalLayout();
		filters.setMargin(false);
		filters.addComponents(checkIncluded, comboGroups);
		return filters;
	}

	public void hideFilter() {
		filters.setVisible(false);
	}

	private void onSelectedOnly(ValueChangeEvent<Boolean> ev) {
		this.selectedOnlyFilter = ev.getValue();
		updateFilter();
	}

	public void setSelectedOnly() {
		this.selectedOnlyFilter = true;
		updateFilter();
	}

	private void updateFilter() {
		if (selectedOnlyFilter != null && selectedOnlyFilter.equals(Boolean.TRUE)) {
			filter.setSelectedPersons(grid.getSelectedItems());
		} else {
			filter.setSelectedPersons(null);
		}

		filter.setSelectedGroups(groupMemberFilter);
		dataProvider.refreshAll();
	}

	public void refreshItem(Person item) {
		dataProvider.refreshItem(item);
	}

	public void onPersonSelect(SelectionListener<Person> listener) throws UnsupportedOperationException {
		grid.addSelectionListener(listener);
	}

	private void selectItems(Person... items) {
		if (currentSelectionMode == SelectionMode.MULTI) {
			GridMultiSelect<Person> asMultiSelect = grid.asMultiSelect();
			asMultiSelect.deselectAll();
			if (items == null || items.length == 0) {
				log.debug("No Persons selected.");
			} else {
				log.debug("Selecting Persons: {}", Arrays.asList(items));
				asMultiSelect.selectItems(items);
			}
		}
	}

	public void deselectItems(Person... items) {
		grid.asMultiSelect().deselect(items);
	}

	public void select(Person item) {
		grid.select(item);
	}

	public void deselect(Person item) {
		grid.deselect(item);
	}

	public void deselectAll() {
		grid.deselectAll();
	}

	private void onGroupSelected(SingleSelectionEvent<GroupDef> ev) {

		groupMemberFilter = ev.getAllSelectedItems();
		if (groupMemberFilter.isEmpty()) {
			groupMemberFilter = null;
		}
		updateFilter();
	}

	public void onPersonEdit() {
		startpassColumn.setHidden(true);
		genderColumn.setHidden(false);
	}

	public void setEvent(ClubEvent ev) {

		if (currentSelectionMode == SelectionMode.MULTI) {
			if (ev != null) {
				updateSelection(ev);
			} else {
				selectItems(new Person[0]);
			}
		} else if (ev != null) {
			Collection<Person> items = dataProvider.getItems();
			items.clear();
			items.addAll(ev.getPersons());
		}
		this.currentEvent = ev;
	}

	public void updateSelection(ClubEvent ev) {
		Set<Person> persons = ev.getPersons();
		if (persons != null) {
			selectItems(persons.toArray(new Person[0]));
		} else {
			selectItems(new Person[0]);
		}
	}

}
