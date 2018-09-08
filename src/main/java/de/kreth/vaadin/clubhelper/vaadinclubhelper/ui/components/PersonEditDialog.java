package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.converter.LocalDateToDateConverter;
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
		
		Panel groupPanel = new Panel("Gruppen");
		VerticalLayout glay = new VerticalLayout();
		groupPanel.setContent(glay);
		List<GroupDef> selected = new ArrayList<>();
		for (GroupDef tmp : person.getPersongroups()) {
			selected.add(tmp);
		}
		for (GroupDef g: groups) {
			Switch sw = new Switch(g.getName());
			sw.setData(g);
			sw.setValue(selected.contains(g));
			sw.addValueChangeListener(ev -> groupChanged(ev));
			glay.addComponent(sw);
		}

		binder = new Binder<>();
		binder.forField(textPrename).bind(Person::getPrename, Person::setPrename);
		binder.forField(textSureName).bind(Person::getSurname, Person::setSurname);
		binder.forField(birthday).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault())).bind(Person::getBirth, Person::setBirth);
		
		binder.readBean(person);
		
		Button close = new Button("Schließen");
		close.addClickListener(ev -> PersonEditDialog.this.close());
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

	private void groupChanged(ValueChangeEvent<Boolean> ev) {
		GroupDef group = (GroupDef) ((Switch)ev.getComponent()).getData();
		List<GroupDef> pg = person.getPersongroups();
		if (ev.getValue()) {
			pg.add(group);
		} else {
			pg.remove(group);
		}
	}
	
	
}
