package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

public class DeleteEventCommand implements ClubCommand {

	private final Runnable deleteDelegate;

	public DeleteEventCommand(Runnable deleteDelegate) {
		super();
		this.deleteDelegate = deleteDelegate;
	}

	@Override
	public String getLabel() {
		return "Veranstaltung löschen";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.DEL;
	}

	@Override
	public void execute() {
		deleteDelegate.run();
	}

}
