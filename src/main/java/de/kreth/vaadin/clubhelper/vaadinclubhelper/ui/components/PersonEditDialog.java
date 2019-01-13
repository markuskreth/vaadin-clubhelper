package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonEditDialog extends Window {

	private static final long serialVersionUID = 4692332924201974714L;
	private final TextField textPrename;
	private final TextField textSureName;
	private final Person person;

	private final Binder<Person> binder;
	private DateField birthday;

	public PersonEditDialog(List<GroupDef> groups, Person person, PersonDao dao) {
		this.person = person;

		textPrename = new TextField();
		textSureName = new TextField();
		birthday = new DateField();
		birthday.setCaption("Geburtstag");

		binder = new Binder<>();
		binder.forField(textPrename).bind(Person::getPrename, Person::setPrename);
		binder.forField(textSureName).bind(Person::getSurname, Person::setSurname);
		binder.forField(birthday).bind(Person::getBirth, Person::setBirth);

		binder.readBean(person);

		Panel groupPanel = new Panel("Gruppen");
		VerticalLayout glay = new VerticalLayout();
		groupPanel.setContent(glay);
		List<GroupDef> selected = new ArrayList<>();
		for (GroupDef tmp : person.getPersongroups()) {
			selected.add(tmp);
		}
		for (GroupDef g : groups) {
			Switch sw = new Switch(g.getName());
			sw.setData(g);
			sw.setValue(selected.contains(g));
//			sw.addValueChangeListener(ev -> groupChanged(ev));
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
		close.addClickListener(ev -> closeWithoutSave(dao));
		Button ok = new Button("Speichern");
		ok.addClickListener(ev -> {
			binder.writeBeanIfValid(person);
			dao.update(person);
			PersonEditDialog.this.close();
		});
		VerticalLayout layout = new VerticalLayout();
		layout.addComponents(textPrename, textSureName, birthday, groupPanel, close, ok);
		setContent(layout);
		center();
	}

	private void closeWithoutSave(PersonDao dao) {
		if (binder.hasChanges()) {

			ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Ungespeicherte Änderungen")
					.setMessage("Die Daten wurden geändert. Sollen die Änderungen gespeichert werden?")
					.saveDiscardCancel().setResultHandler(button -> {
						if (button == ConfirmDialog.Buttons.SAVE) {
							binder.writeBeanIfValid(person);
							dao.update(person);
							PersonEditDialog.this.close();
						} else if (button == ConfirmDialog.Buttons.DISCARD) {
							PersonEditDialog.this.close();
						}
					}).build();

			getUI().addWindow(dlg);
		} else {
			close();
		}
	}

//	private void groupChanged(ValueChangeEvent<Boolean> ev) {
//		GroupDef group = (GroupDef) ((Switch) ev.getComponent()).getData();
//		Set<GroupDef> pg = person.getPersongroups();
//		if (ev.getValue()) {
//			pg.add(group);
//		} else {
//			pg.remove(group);
//		}
//	}

}
