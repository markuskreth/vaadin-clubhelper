package de.kreth.vaadin.clubhelper.vaadinclubhelper.security;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class SecurityGroupVerifierImpl {

	private Person person;

	public void setLoggedinPerson(Person person) {
		this.person = person;
	}

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

}
