package de.kreth.vaadin.clubhelper.vaadinclubhelper.email;

import java.io.IOException;

public interface EmailCommand {

	void send(Email email) throws IOException;
}
