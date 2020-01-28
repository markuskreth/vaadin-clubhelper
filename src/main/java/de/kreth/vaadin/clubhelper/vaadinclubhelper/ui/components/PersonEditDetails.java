package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

public class PersonEditDetails extends HorizontalLayout {

	private static final long serialVersionUID = 4692332924201974714L;

	private final TextField textPrename;

	private final TextField textSureName;

	private final DateField birthday;

	private final PersonBusiness dao;

	private final Binder<Person> binder;

	private Consumer<Person> personChangeHandler;

	private Button okButton;

	private ContactGrid contactLayout;

	private RelationComponent relationshipLayout;

	private AdressComponent adressLayout;

	private NotesComponent notes;

	public PersonEditDetails(List<GroupDef> groups, PersonBusiness dao) {
		this(groups, dao, true);
	}

	public PersonEditDetails(List<GroupDef> groups, PersonBusiness dao, boolean showCloseButton) {

		this.dao = dao;

		setCaption("Personendetails");

		textPrename = new TextField("Vorname");
		textSureName = new TextField("Nachname");
		birthday = new DateField("Geburtstag");
		ComboBox<Gender> genderBox = new ComboBox<>("Geschlecht");
		genderBox.setEmptySelectionAllowed(false);
		genderBox.setItemCaptionGenerator(item -> item.localized());
		genderBox.setItems(Gender.values());
		TextField textStartPass = new TextField("Startpass");

		binder = new Binder<>();
		binder.forField(textPrename).asRequired().bind(Person::getPrename, Person::setPrename);
		binder.forField(textSureName).asRequired().bind(Person::getSurname, Person::setSurname);
		binder.forField(birthday).bind(Person::getBirth, Person::setBirth);
		binder.forField(genderBox).bind(Person::getGender, Person::setGender);
		binder.forField(textStartPass).bind(p -> {
			Startpass startpass = p.getStartpass();
			if (startpass == null) {
				return null;
			}
			return startpass.getStartpassNr();
		}, (p, value) -> p.setStartpass(value));

		binder.withValidator(p -> (p.hasAnyGroup()),
				"Mind. eine Gruppe muss gewählt sein!");

		Button close = new Button("Schließen");
		if (showCloseButton) {
			close.addClickListener(ev -> closeWithoutSave());
		}
		else {
			close.setVisible(false);
		}

		okButton = new Button("Speichern");
		okButton.addClickListener(ev -> {
			okButton.setComponentError(null);
			BinderValidationStatus<Person> validate = binder.validate();
			if (validate.isOk()) {
				Person edited = binder.getBean();
				try {
					dao.save(edited);
				}
				catch (DataAccessException e) {
					edited = dao.getById(edited.getId());
					updateBean(edited);
					throw e;
				}
				if (personChangeHandler != null) {
					personChangeHandler.accept(binder.getBean());
				}
				updateBean(edited);
			}
			else {
				List<ValidationResult> errors = validate.getBeanValidationErrors();
				StringBuilder msg = new StringBuilder();
				for (ValidationResult res : errors) {
					if (res.isError()) {
						if (msg.length() > 0) {
							msg.append("\n");
						}
						msg.append(res.getErrorMessage());
					}
				}
				Notification.show("Fehler korrigieren", msg.toString(), Notification.Type.ERROR_MESSAGE);
			}
		});
		okButton.setEnabled(false);
		VerticalLayout layout = new VerticalLayout(textPrename, textSureName, birthday, genderBox, textStartPass, close,
				okButton);

		Component groupLayout = createGroupPanel(groups);
		contactLayout = new ContactGrid();
		contactLayout.setDeleteConsumer(c -> {

			ConfirmDialog dlg = ConfirmDialog
					.builder().setCaption("Kontakt löschen").setMessage(c.getPerson().getPrename() + " "
							+ c.getPerson().getSurname() + " \"" + c + "\" wirklich löschen?")
					.yesCancel().setResultHandler(button -> {
						if (button == ConfirmDialog.Buttons.YES) {
							dao.delete(c);
						}
					}).build();

			getUI().addWindow(dlg);
		});
		contactLayout.addSuccessConsumer(newContact -> binder.getBean().add(newContact));

		relationshipLayout = new RelationComponent(dao);

		adressLayout = new AdressComponent();
		adressLayout.setDeleteConsumer(a -> {

			ConfirmDialog dlg = ConfirmDialog
					.builder().setCaption("Adresse löschen").setMessage(a.getPerson().getPrename() + " "
							+ a.getPerson().getSurname() + " \"" + a + "\" wirklich löschen?")
					.yesCancel().setResultHandler(button -> {
						if (button == ConfirmDialog.Buttons.YES) {
							dao.delete(a);
						}
					}).build();

			getUI().addWindow(dlg);
		});
		adressLayout.addSuccessConsumer(newAdress -> binder.getBean().addAdress(newAdress));

		notes = new NotesComponent();

		TabSheet sheet = new TabSheet();
		sheet.addTab(groupLayout, "Gruppen");
		sheet.addTab(contactLayout, "Kontakte");
		sheet.addTab(relationshipLayout, "Angehörige");
		sheet.addTab(adressLayout, "Adresse");
		sheet.addTab(notes, "Notizen");

		addComponents(layout, sheet);
		setExpandRatio(layout, 1f);
		setExpandRatio(sheet, 2f);

		iterator().forEachRemaining(comp -> comp.setEnabled(false));
	}

	static boolean containsGroup(Person p, GroupDef g) {
		return p != null
				&& p.hasGroup(g);
	}

	static void changeGroupContent(Person p, GroupDef g, boolean schouldBeContained) {
		if (schouldBeContained) {
			p.add(g);
		}
		else {
			p.remove(g);
		}
	}

	public Component createGroupPanel(List<GroupDef> groups) {

		VerticalLayout layout = new VerticalLayout();

		for (GroupDef g : groups) {
			Switch sw = new Switch(g.getName());
			sw.setId("Group_" + g.getName());
			sw.setData(g);

			ValueProvider<Person, Boolean> getter = p -> containsGroup(p, g);
			Setter<Person, Boolean> setter = (p, b) -> changeGroupContent(p, g, b);
			binder.forField(sw).bind(getter, setter);
			layout.addComponent(sw);
		}
		binder.addValueChangeListener(new ValueChangeListener<Object>() {

			transient Logger log = LoggerFactory.getLogger(PersonEditDetails.this.getClass());

			@Override
			public void valueChange(ValueChangeEvent<Object> event) {
				Component comp = event.getComponent();
				Object oldGroups = event.getOldValue();
				Object newGroups = event.getValue();
				log.warn("Changed value in {}, old size={}, new size={}, hasChanges={}", comp.getId(), oldGroups,
						newGroups, binder.hasChanges());
			}
		});
		return layout;
	}

	public void setPersonChangeHandler(Consumer<Person> personChangeHandler) {
		this.personChangeHandler = personChangeHandler;
	}

	public void setBean(Person person) {
		closeWithoutSave();

		okButton.setComponentError(null);

		updateBean(person);

		if (person != null) {
			iterator().forEachRemaining(comp -> comp.setEnabled(true));
			binder.validate();
			okButton.setEnabled(true);
		}
		else {
			iterator().forEachRemaining(comp -> comp.setEnabled(false));
			okButton.setEnabled(false);
		}
	}

	public Person currentBean() {
		return binder.getBean();
	}

	private void updateBean(Person person) {
		contactLayout.setPerson(person);
		relationshipLayout.setPerson(person);
		adressLayout.setPerson(person);
		notes.setPerson(person);
		binder.setBean(person);
	}

	public void closeWithoutSave() {
		if (hasChanges()) {

			final Person current = binder.getBean();
			ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Ungespeicherte Änderungen")
					.setMessage(current.getPrename() + " " + current.getSurname()
							+ " wurde geändert. Sollen die Änderungen gespeichert werden?")
					.saveDiscardCancel().setResultHandler(button -> {
						if (button == ConfirmDialog.Buttons.SAVE) {
							if (binder.validate().isOk()) {
								dao.save(current);
							}
						}
					}).build();

			getUI().addWindow(dlg);
		}
	}

	public boolean hasChanges() {
		return binder.getBean() != null
				&& (binder.hasChanges()
						|| contactLayout.hasChanges()
						|| relationshipLayout.hasChanges()
						|| adressLayout.hasChanges()
						|| notes.hasChanges());
	}

}
