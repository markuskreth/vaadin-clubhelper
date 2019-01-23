package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.List;
import java.util.function.Consumer;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
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

	private final Binder<Person> binder;
	private Consumer<Person> personChangeHandler;
	private Person current;

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao) {
		this(groups, dao, true);
	}

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao, boolean showCloseButton) {

		textPrename = new TextField("Vorname");
		textSureName = new TextField("Nachname");
		birthday = new DateField("Geburtstag");
		ComboBox<Gender> genderBox = new ComboBox<>("Geschlecht");
		genderBox.setEmptySelectionAllowed(false);
		genderBox.setItemCaptionGenerator(item -> item.localized());
		genderBox.setItems(Gender.values());
		TextField textStartPass = new TextField("Startpass");

		binder = new Binder<>();
		binder.forField(textPrename).bind(Person::getPrename, Person::setPrename);
		binder.forField(textSureName).bind(Person::getSurname, Person::setSurname);
		binder.forField(birthday).bind(Person::getBirth, Person::setBirth);
		binder.forField(genderBox).bind(Person::getGender, Person::setGender);
		binder.forField(textStartPass).bind(p -> {
			Startpass startpass = p.getStartpass();
			if (startpass == null) {
				return null;
			}
			return startpass.getStartpassNr();
		}, (p, value) -> p.setStartpass(value));

		Panel groupPanel = new Panel("Gruppen");
		VerticalLayout glay = new VerticalLayout();
		groupPanel.setContent(glay);

		for (GroupDef g : groups) {
			Switch sw = new Switch(g.getName());
			sw.setData(g);
			glay.addComponent(sw);

			binder.forField(sw).bind(p -> p.getGroups().contains(g), (bean, fieldvalue) -> {
				if (fieldvalue) {
					bean.getGroups().add(g);
				} else {
					bean.getGroups().remove(g);
				}
			});
		}

		Button close = new Button("Schließen");
		if (showCloseButton) {
			close.addClickListener(ev -> closeWithoutSave(dao));
		} else {
			close.setVisible(false);
		}
		Button ok = new Button("Speichern");
		ok.addClickListener(ev -> {
			if (binder.validate().isOk()) {
				binder.writeBeanIfValid(current);
				dao.update(current);
				if (personChangeHandler != null) {
					personChangeHandler.accept(current);
				}
			}
		});

		VerticalLayout layout = new VerticalLayout(textPrename, textSureName, birthday, genderBox, textStartPass, close,
				ok);
		addComponents(layout, groupPanel);

	}

	public void setPersonChangeHandler(Consumer<Person> personChangeHandler) {
		this.personChangeHandler = personChangeHandler;
	}

	public void setBean(Person person) {
		this.current = person;
		binder.readBean(person);
	}

	private void closeWithoutSave(PersonDao dao) {
		if (binder.hasChanges()) {

			ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Ungespeicherte Änderungen")
					.setMessage("Die Daten wurden geändert. Sollen die Änderungen gespeichert werden?")
					.saveDiscardCancel().setResultHandler(button -> {
						if (button == ConfirmDialog.Buttons.SAVE) {
							if (binder.validate().isOk()) {
								binder.writeBeanIfValid(current);
								dao.update(current);
							}
						} else if (button == ConfirmDialog.Buttons.DISCARD) {
							binder.readBean(current);
						}
					}).build();

			getUI().addWindow(dlg);
		} else {
		}
	}

}
