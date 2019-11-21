package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.revertable;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;

public interface RevertableCommand extends ClubCommand {

	/**
	 * Macht das Kommando wieder Rückgängig.
	 */
	void revert();

}
