package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;

public class HeadView extends HorizontalLayout {

	protected transient final Logger log = LoggerFactory.getLogger(getClass());

	protected transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private final Label personLabel;

	protected final Label monthName;

	private final SecurityVerifier securityVerifier;

	public HeadView(SecurityVerifier securityVerifier) {

		this.securityVerifier = securityVerifier;
		monthName = new Label();
		monthName.setId("calendar.month");
		monthName.setStyleName("title_caption");
		monthName.setWidth("");

		personLabel = new Label();
		personLabel.setId("head.user");

		addComponent(monthName);
		addComponent(personLabel);

		setComponentAlignment(monthName, Alignment.MIDDLE_LEFT);
		setComponentAlignment(personLabel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(monthName, 1.0f);

	}

	public void updateLoggedinPerson() {

		Person loggedinPerson = securityVerifier.getLoggedinPerson();
		if (loggedinPerson != null) {
			personLabel.setCaption(loggedinPerson.getSurname() + ", " + loggedinPerson.getPrename());
		}
		else {
			personLabel.setCaption("");
		}
	}

}
