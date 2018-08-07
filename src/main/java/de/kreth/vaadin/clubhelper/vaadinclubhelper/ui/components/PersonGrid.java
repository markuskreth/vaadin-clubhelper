package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Persongroup;

public class PersonGrid extends CustomComponent {

	private static final long serialVersionUID = -8148097982839343673L;
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final DateFormat birthFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM);

	private final Grid<Person> grid;

	private final CheckBox checkIncluded;
	private final ComboBox<GroupDef> comboGroups;

	private final TextField textTitle;

	private final ListDataProvider<Person> dataProvider;
	private ClosedFunction closedFunction = null;

	public PersonGrid(GroupDao groupDao) {

		textTitle = new TextField();
		textTitle.setStyleName("title_label");
		textTitle.setCaption("Veranstaltung");
		textTitle.setEnabled(false);
		textTitle.setSizeFull();

		checkIncluded = new CheckBox("Nur gemeldete");
		checkIncluded.addValueChangeListener(ev -> onSelectedOnly(ev));
		comboGroups = new ComboBox<>("Gruppenfilter");
		comboGroups.setEmptySelectionAllowed(true);
		comboGroups.setEmptySelectionCaption("Alle");
		comboGroups.setItemCaptionGenerator(GroupDef::getName);
		comboGroups.addSelectionListener(ev -> onGroupSelected(ev));
		List<GroupDef> items = groupDao.list();
		comboGroups.setItems(items);
		log.info("Loaded Groups: {}", items);

		HorizontalLayout filters = new HorizontalLayout();
		filters.addComponents(checkIncluded, comboGroups);
		dataProvider = new ListDataProvider<>(new ArrayList<>());
		grid = new Grid<>();
		grid.setDataProvider(dataProvider);
		grid.addColumn(Person::getPrename).setCaption("Vorname");
		grid.addColumn(Person::getSurname).setCaption("Nachname");
		grid.addColumn(Person::getBirth,
				b -> b != null ? birthFormat.format(b) : "")
				.setCaption("Geburtstag");
		grid.setSelectionMode(SelectionMode.MULTI);

		Button close = new Button("Schließen", ev -> {
			PersonGrid.this.setVisible(false);
			if (closedFunction != null) {
				closedFunction.closed();
			}
		});

		VerticalLayout panel = new VerticalLayout();
		panel.addComponents(textTitle, filters, grid, close);
		setCompositionRoot(panel);
	}

	public void onClosedFunction(ClosedFunction closedFunction) {
		this.closedFunction = closedFunction;
	}

	private void onSelectedOnly(ValueChangeEvent<Boolean> ev) {
		dataProvider.clearFilters();
		Set<Person> selected = grid.getSelectedItems();
		dataProvider.addFilter(p -> selected.contains(p));
	}

	private void onGroupSelected(SingleSelectionEvent<GroupDef> ev) {
		dataProvider.clearFilters();
		final Set<GroupDef> groups = ev.getAllSelectedItems();
		dataProvider.addFilter(p -> {
			List<Persongroup> pgs = p.getPersongroups();
			for (Persongroup pg : pgs) {
				if (groups.contains(pg.getGroupDef())) {
					return true;
				}
			}
			return false;
		});
	}

	public void setTitle(String value) {
		if (value == null) {
			value = "";
		}
		textTitle.setValue(value);
	}

	public void setItems(Collection<Person> items) {
		grid.setItems(items);
	}

	public void setItems(Person... items) {
		grid.setItems(items);
	}

	public interface ClosedFunction {
		void closed();
	}
}
