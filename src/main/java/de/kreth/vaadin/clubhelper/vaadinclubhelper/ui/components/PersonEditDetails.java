package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.List;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonEditDetails extends VerticalLayout {

	private static final long serialVersionUID = 4692332924201974714L;
	private final TextField textPrename;
	private final TextField textSureName;

	private final Binder<Person> binder;
	private DateField birthday;
	private Person current;

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao) {
		this(groups, dao, true);
	}

	public PersonEditDetails(List<GroupDef> groups, PersonDao dao, boolean showCloseButton) {

		textPrename = new TextField();
		textPrename.setCaption("Vorname");
		textSureName = new TextField();
		textSureName.setCaption("Nachname");
		birthday = new DateField();
		birthday.setCaption("Geburtstag");

		binder = new Binder<>();
		binder.forField(textPrename).bind(Person::getPrename, Person::setPrename);
		binder.forField(textSureName).bind(Person::getSurname, Person::setSurname);
		binder.forField(birthday).bind(Person::getBirth, Person::setBirth);

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
			}
		});

		addComponents(textPrename, textSureName, birthday, groupPanel, close, ok);

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
