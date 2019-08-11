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
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridMultiSelect;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
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

	private final TextField textFilter;

	private final PersonBusiness personDao;

	private List<GroupDef> allGroups;

	private PersonFilter filter;

	private ClubEvent currentEvent;

	private Column<Person, String> startpassColumn;

	private Layout filters;

	private SelectionMode currentSelectionMode;

	private Column<Person, ?> genderColumn;

	private Column<Person, ? extends Component> deleteButtonColumn;

	public PersonGrid(GroupDao groupDao, PersonBusiness personDao) {

		setId("main.person");
		setCaption("Teilnehmer");
		addStyleName("bold-caption");

		this.personDao = personDao;

		checkIncluded = new CheckBox("Nur gemeldete");
		comboGroups = new ComboBox<>("Gruppenfilter");
		textFilter = new TextField("Namenfilter");
		textFilter.setIcon(VaadinIcons.SEARCH);
		filters = setupFilterComponents();

		allGroups = groupDao.listAll();
		comboGroups.setItems(allGroups);
		log.info("Loaded Groups: {}", allGroups);

		filter = new PersonFilter(personDao);
		dataProvider = DataProvider.ofCollection(filter.asCollection());
		grid = new Grid<>();

		setupPersonGrid();

		setMargin(false);
		addComponents(filters, grid);
	}

	void setupPersonGrid() {
		filter.add(() -> {
			setEvent(currentEvent);
		});
		dataProvider.addDataProviderListener(filter);

		grid.setDataProvider(dataProvider);
		grid.setId("person.grid");
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.addColumn(Person::getPrename).setCaption("Vorname");
		grid.addColumn(Person::getSurname).setCaption("Nachname");
		grid.addColumn(Person::getBirth, b -> b != null ? birthFormat.format(b) : "").setCaption("Geburtstag")
				.setHidable(true);

		startpassColumn = grid.addColumn(p -> {
			Startpass startpass = p.getStartpass();
			if (startpass != null) {
				return startpass.getStartpassNr();
			}
			else {
				return null;
			}
		}).setCaption("Startpass Nr.");
		startpassColumn.setHidable(true);

		genderColumn = grid.addComponentColumn(p -> {

			Gender gender = p.getGender();
			VaadinIcons icon;

			if (gender == null) {
				icon = VaadinIcons.QUESTION;
			}
			else {
				switch (gender) {
				case FEMALE:
					icon = VaadinIcons.FEMALE;
					break;
				case MALE:
					icon = VaadinIcons.MALE;
					break;
				default:
					icon = VaadinIcons.QUESTION;
					break;
				}
			}

			return new Label(icon.getHtml(), ContentMode.HTML);

		});
		genderColumn.setHidable(true);

		startpassColumn.setHidden(false);
		genderColumn.setHidden(true);

		deleteButtonColumn = grid.addComponentColumn(c -> {
			Button deleteButton = new Button(VaadinIcons.TRASH);
			deleteButton.addClickListener(ev -> delete(c));
			deleteButton.setWidthUndefined();
			return deleteButton;
		}).setCaption("Löschen");
		deleteButtonColumn.setHidden(true);

	}

	public VaadinIcons genderToImage(Gender gender) {
		VaadinIcons icon;

		if (gender == null) {
			icon = VaadinIcons.QUESTION;
		}
		else {
			switch (gender) {
			case FEMALE:
				icon = VaadinIcons.FEMALE;
				break;
			case MALE:
				icon = VaadinIcons.MALE;
				break;
			default:
				icon = VaadinIcons.QUESTION;
				break;
			}
		}

		return icon;
	}

	public void enableDeleteColumn(boolean enable) {
		deleteButtonColumn.setHidden(!enable);
	}

	private void delete(Person c) {

		ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Person löschen")
				.setMessage(c.getPrename() + " " + c.getSurname() + " wirklich löschen?").yesCancel()
				.setResultHandler(button -> {
					if (button == ConfirmDialog.Buttons.YES) {
						personDao.delete(c);
						dataProvider.refreshAll();
					}
				}).build();

		getUI().addWindow(dlg);
	}

	public void setSelectionMode(SelectionMode selectionMode) {
		grid.setSelectionMode(selectionMode);
		currentSelectionMode = selectionMode;
		if (selectionMode == SelectionMode.MULTI) {
			checkIncluded.setVisible(true);
		}
	}

	private Layout setupFilterComponents() {
		checkIncluded.setId("person.filter.checked");
		checkIncluded.addValueChangeListener(ev -> onSelectedOnly(ev));
		checkIncluded.setVisible(false);

		comboGroups.setId("person.filter.groups");
		comboGroups.setEmptySelectionAllowed(true);
		comboGroups.setEmptySelectionCaption("Alle");
		comboGroups.setItemCaptionGenerator(GroupDef::getName);
		comboGroups.addSelectionListener(ev -> onGroupSelected(ev));

		textFilter.setId("person.filter.namefilter");
		textFilter.addValueChangeListener(ev -> textFilterChanged(ev));
		HorizontalLayout filters = new HorizontalLayout();
		filters.setMargin(false);
		filters.addComponents(checkIncluded, comboGroups, textFilter);
		return filters;
	}

	private void textFilterChanged(ValueChangeEvent<String> ev) {
		String value = ev.getValue();
		if (value != null && value.length() >= 2) {
			filter.setNameFilter(value);
			dataProvider.refreshAll();
		}
		else {
			filter.setNameFilter(null);
			dataProvider.refreshAll();
		}
	}

	public void hideFilter() {
		filters.setVisible(false);
	}

	private void onSelectedOnly(ValueChangeEvent<Boolean> ev) {
		updateSelectedOnlyFilter(ev.getValue());
	}

	public void setSelectedOnly() {
		updateSelectedOnlyFilter(true);
	}

	private void updateSelectedOnlyFilter(Boolean selectedOnly) {
		if (selectedOnly != null && selectedOnly.equals(Boolean.TRUE)) {
			filter.setSelectedPersons(grid.getSelectedItems());
		}
		else {
			filter.setSelectedPersons(null);
		}
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
			}
			else {
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

		Set<GroupDef> groupMemberFilter = ev.getAllSelectedItems();
		if (groupMemberFilter.isEmpty()) {
			groupMemberFilter = null;
		}
		filter.setSelectedGroups(groupMemberFilter);
		dataProvider.refreshAll();
	}

	public void onPersonEdit() {
		startpassColumn.setHidden(true);
		genderColumn.setHidden(false);
	}

	public void setEvent(ClubEvent ev) {

		if (currentSelectionMode == SelectionMode.MULTI) {
			if (ev != null) {
				updateSelection(ev);
			}
			else {
				selectItems(new Person[0]);
			}
		}
		else if (ev != null) {
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
		}
		else {
			selectItems(new Person[0]);
		}
	}

}
