package de.kreth.vaadin.clubhelper.vaadinclubhelper.security;

import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Service
public class SecurityVerifierImpl implements SecurityVerifier {

	private Person person;

	@Override
	public void setLoggedinPerson(Person person) {
		VaadinSession currentSession = VaadinSession.getCurrent();
		currentSession.setAttribute(Person.SESSION_LOGIN, person);
		this.person = person;
	}

	@Override
	public Person getLoggedinPerson() {
		return person;
	}

	@Override
	public boolean isPermitted(SecurityGroups... groups) {
		if (person != null) {

			for (SecurityGroups g : groups) {
				for (GroupDef def : person.getGroups()) {
					if (g.getValue().equalsIgnoreCase(def.getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isLoggedin() {
		return person != null && person.getGroups() != null && person.getGroups().isEmpty() == false;
	}

}
