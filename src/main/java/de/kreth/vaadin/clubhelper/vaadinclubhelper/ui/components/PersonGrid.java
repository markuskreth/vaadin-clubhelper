package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonGrid extends VerticalLayout {

	private static final long serialVersionUID = -8148097982839343673L;
	private final transient Logger log = LoggerFactory.getLogger(getClass());

	private final transient DateTimeFormatter birthFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
	private final ListDataProvider<Person> dataProvider;

	private final Grid<Person> grid;
	private final CheckBox checkIncluded;
	private final ComboBox<GroupDef> comboGroups;

	private transient ClosedFunction closedFunction = null;
	private transient Consumer<Person> onPersonEdit;
	private Boolean selectedOnlyFilter;

	private Set<GroupDef> groupMemberFilter;
	private List<GroupDef> allGroups;
	private PersonFilter filter;
	private ClubEvent currentEvent;

	public PersonGrid(GroupDao groupDao, PersonDao personDao) {

		setCaption("Teilnehmer");
		addStyleName("bold-caption");

		checkIncluded = new CheckBox("Nur gemeldete");
		comboGroups = new ComboBox<>("Gruppenfilter");
		Layout filters = setupFilterComponents();

		allGroups = groupDao.listAll();
		comboGroups.setItems(allGroups);
		log.info("Loaded Groups: {}", allGroups);

		filter = new PersonFilter(personDao);
		dataProvider = DataProvider.ofCollection(filter.asCollection());
		grid = new Grid<>();

		setupPersonGrid(personDao);

		Button close = new Button("Schließen", ev -> {
			PersonGrid.this.setVisible(false);
			if (closedFunction != null) {
				closedFunction.closed();
			}
		});
		close.setId("person.close");

		setMargin(false);
		addComponents(filters, grid, close);
	}

	public void setupPersonGrid(PersonDao personDao) {
		filter.add(() -> {
			setEvent(currentEvent);
		});
		dataProvider.addDataProviderListener(filter);

		grid.setDataProvider(dataProvider);
		grid.setId("person.grid");
		grid.addColumn(Person::getPrename).setCaption("Vorname");
		grid.addColumn(Person::getSurname).setCaption("Nachname");
		grid.addColumn(Person::getBirth, b -> b != null ? birthFormat.format(b) : "").setCaption("Geburtstag");
		grid.addComponentColumn(this::buildPersonEditButton);
		grid.setSelectionMode(SelectionMode.MULTI);
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

	private Button buildPersonEditButton(Person p) {
		Button button = new Button(VaadinIcons.EDIT);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		button.addClickListener(e -> showPersonDetails(p));
		return button;
	}

	private void showPersonDetails(Person p) {
		if (onPersonEdit != null) {
			onPersonEdit.accept(p);
		}
	}

	public void onClosedFunction(ClosedFunction closedFunction) {
		this.closedFunction = closedFunction;
	}

	private void onSelectedOnly(ValueChangeEvent<Boolean> ev) {
		this.selectedOnlyFilter = ev.getValue();
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

	public void onPersonSelect(SelectionListener<Person> listener) throws UnsupportedOperationException {
		grid.addSelectionListener(listener);
	}

	private void selectItems(Person... items) {
		MultiSelect<Person> asMultiSelect = grid.asMultiSelect();
		asMultiSelect.deselectAll();
		if (items == null || items.length == 0) {
			log.debug("No Persons selected.");
		} else {
			log.debug("Selecting Persons: {}", Arrays.asList(items));
			asMultiSelect.select(items);
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

	public interface ClosedFunction {
		void closed();
	}

	public void onPersonEdit(Consumer<Person> function) {
		this.onPersonEdit = function;
	}

	public void setEvent(ClubEvent ev) {

		if (ev != null) {
			updateSelection(ev);
		} else {
			selectItems(new Person[0]);
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
