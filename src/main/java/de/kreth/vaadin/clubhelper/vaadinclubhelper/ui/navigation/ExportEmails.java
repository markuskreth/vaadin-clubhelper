package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import org.springframework.context.ApplicationContext;

public class ExportEmails extends SendEmails {

	public ExportEmails(ApplicationContext context) {
		super(context, false);
	}

}
