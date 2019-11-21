package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.ClubhelperMenuBar;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemStateFactory;

public class ExportEmails extends VerticalLayout implements View {

	private PersonBusiness personBusiness;

	private List<GroupDef> allGroups;

	private Set<GroupDef> selected;

	private ListDataProvider<EmailHolder> dataProvider;

	private final List<EmailHolder> items;

	private MenuItemStateFactory stateFactory;

	private TextArea asText;

	public ExportEmails(ApplicationContext context) {

		this.stateFactory = context.getBean(MenuItemStateFactory.class);
		this.personBusiness = context.getBean(PersonBusiness.class);
		allGroups = context.getBean(GroupDao.class).listAll();
		selected = new HashSet<>();

		items = new ArrayList<>();

		dataProvider = DataProvider.ofCollection(items);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);

		ClubhelperMenuBar menubar = new ClubhelperMenuBar(stateFactory.currentState());

		HorizontalLayout groupCheck = new HorizontalLayout();
		for (GroupDef groupDef : allGroups) {
			CheckBox box = new CheckBox(groupDef.getName());
			box.setData(groupDef);
			if (groupDef.hasValidId() && groupDef.getId() == 1) {
				box.setValue(true);
				selected.add(groupDef);
			}
			box.addValueChangeListener(this::groupBoxEvent);
			groupCheck.addComponent(box);
		}

		Grid<EmailHolder> grid = new Grid<>();
		grid.addColumn(EmailHolder::getPrename).setCaption("Vorname");
		grid.addColumn(EmailHolder::getSurname).setCaption("Nachname");
		grid.addColumn(EmailHolder::getEmail).setCaption("Emailadresse");

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(dataProvider);

		asText = new TextArea("Emails als Text");
		asText.setWidth(10, Unit.CM);
		asText.setWordWrap(false);
		asText.setRows(25);
		asText.setEnabled(false);

		addComponent(menubar);
		addComponent(new Label("<H1>Export von Emailadressen</H1>", ContentMode.HTML));
		addComponent(groupCheck);

		addComponent(new HorizontalLayout(grid, asText));
		refreshData();
	}

	private void groupBoxEvent(ValueChangeEvent<Boolean> event) {
		CheckBox box = (CheckBox) event.getComponent();
		GroupDef groupDef = (GroupDef) box.getData();
		Boolean checked = box.getValue();
		if (Boolean.TRUE.equals(checked)) {
			selected.add(groupDef);
		}
		else {
			selected.remove(groupDef);
		}
		refreshData();
	}

	private void refreshData() {
		items.clear();
		personBusiness.listAll().stream()
				.filter(this::matchGroupSelection)
				.map(this::getEmails)
				.forEach(items::addAll);
		dataProvider.refreshAll();

		StringBuilder text = new StringBuilder();
		for (EmailHolder emailHolder : items) {
			if (text.length() > 0) {
				text.append(",\n");
			}
			text.append(emailHolder.getEmail());
		}
		asText.setValue(text.toString());
	}

	private boolean matchGroupSelection(Person p) {
		Set<GroupDef> personGroups = p.getGroups();
		boolean contains = false;
		for (GroupDef g : selected) {
			if (personGroups.contains(g)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	private Collection<EmailHolder> getEmails(Person person) {

		List<Contact> emails = new ArrayList<>();
		List<Contact> contacts = person.getContacts();
		for (Contact contact : contacts) {
			if (Contact.Type.EMAIL.getName().equals(contact.getType())) {
				emails.add(contact);
			}
		}
		Set<EmailHolder> holders = new HashSet<>();
		for (Contact c : emails) {
			holders.add(new EmailHolder(person, c));
		}
		return holders;
	}

	private class EmailHolder {

		private final Person person;

		private final Contact contact;

		public EmailHolder(Person person, Contact contact) {
			super();
			this.person = person;
			this.contact = contact;
		}

		public String getEmail() {
			return contact.getValue();
		}

		public String getPrename() {
			return person.getPrename();
		}

		public String getSurname() {
			return person.getSurname();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((contact.getValue() == null) ? 0 : contact.getValue().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			EmailHolder other = (EmailHolder) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
				return false;
			}
			if (contact.getValue() == null) {
				if (other.contact.getValue() != null) {
					return false;
				}
			}
			else if (!contact.getValue().equals(other.contact.getValue())) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return contact.getValue();
		}

		private ExportEmails getEnclosingInstance() {
			return ExportEmails.this;
		}

	}
}
