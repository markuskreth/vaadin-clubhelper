package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

public interface RevertableCommand extends ClubCommand {

	/**
	 * Macht das Kommando wieder Rückgängig.
	 */
	void revert();

}
