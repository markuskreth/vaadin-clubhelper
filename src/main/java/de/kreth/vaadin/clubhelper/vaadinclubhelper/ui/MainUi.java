package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@SpringUI
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;
	@Autowired
	PersonDao dao;

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label("Persons found:"));
		List<Person> persons = dao.list();
		for (Person p : persons) {
			layout.addComponent(
					new Label(p.getPrename() + " " + p.getSurname()));
		}
		setContent(layout);
	}

}
