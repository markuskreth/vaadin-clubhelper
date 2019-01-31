package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.List;
import java.util.function.Consumer;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

public class PersonEditDetails extends HorizontalLayout {

	private static final long serialVersionUID = 4692332924201974714L;
	private final TextField textPrename;
	private final TextField textSureName;
	private final DateField birthday;

	private final PersonDao dao;

	private final Binder<Person> binder;
	private Consumer<Person> personChangeHandler;
	private Button okButton;

	private ContactGrid contactLayout;
	private RelationComponent relationshipLayout;
	private AdressComponent adressLayout;

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao) {
		this(groups, dao, true);
	}

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao, boolean showCloseButton) {

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

		binder.withValidator(p -> (p.getGroups() != null && p.getGroups().isEmpty() == false),
				"Mind. eine Gruppe muss gewählt sein!");

		Button close = new Button("Schließen");
		if (showCloseButton) {
			close.addClickListener(ev -> closeWithoutSave());
		} else {
			close.setVisible(false);
		}

		okButton = new Button("Speichern");
		okButton.addClickListener(ev -> {
			BinderValidationStatus<Person> validate = binder.validate();
			if (validate.isOk()) {
				Person edited = binder.getBean();
				dao.save(edited);
				if (personChangeHandler != null) {
					personChangeHandler.accept(binder.getBean());
				}
				contactLayout.setPerson(edited);
				relationshipLayout.setPerson(edited);
				adressLayout.setPerson(edited);
			} else {
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

		TabSheet sheet = new TabSheet();
		sheet.addTab(groupLayout, "Gruppen");
		sheet.addTab(contactLayout, "Kontakte");
		sheet.addTab(relationshipLayout, "Angehörige");
		sheet.addTab(adressLayout, "Adresse");
		addComponents(layout, sheet);
		setExpandRatio(layout, 1f);
		setExpandRatio(sheet, 2f);

	}

	public Component createGroupPanel(List<GroupDef> groups) {

		VerticalLayout layout = new VerticalLayout();

		for (GroupDef g : groups) {
			Switch sw = new Switch(g.getName());
			sw.setData(g);
			layout.addComponent(sw);

			binder.forField(sw).bind(p -> p.getGroups().contains(g), (bean, fieldvalue) -> {
				if (fieldvalue) {
					bean.getGroups().add(g);
				} else {
					bean.getGroups().remove(g);
				}
			});
		}
		return layout;
	}

	public void setPersonChangeHandler(Consumer<Person> personChangeHandler) {
		this.personChangeHandler = personChangeHandler;
	}

	public void setBean(Person person) {
		closeWithoutSave();
		binder.setBean(person);
		contactLayout.setPerson(person);
		relationshipLayout.setPerson(person);
		adressLayout.setPerson(person);

		if (person != null) {
			binder.validate();
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}
	}

	void closeWithoutSave() {
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
		return binder.hasChanges() || contactLayout.hasChanges() || relationshipLayout.hasChanges()
				|| adressLayout.hasChanges();
	}

}
