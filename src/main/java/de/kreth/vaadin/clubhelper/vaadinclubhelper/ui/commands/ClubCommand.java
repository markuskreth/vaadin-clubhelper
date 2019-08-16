package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.io.Serializable;

import com.vaadin.server.Resource;

public interface ClubCommand extends Serializable {

	String getLabel();

	/**
	 * may be null if no Icon is supported.
	 * @return
	 */
	Resource getIcon();

	void execute();
}
