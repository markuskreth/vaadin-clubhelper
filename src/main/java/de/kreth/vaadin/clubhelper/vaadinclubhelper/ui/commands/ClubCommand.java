package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import com.vaadin.server.Resource;

public interface ClubCommand {

	String getLabel();

	/**
	 * may be null if no Icon is supported.
	 * @return
	 */
	Resource getIcon();

	void execute();
}
