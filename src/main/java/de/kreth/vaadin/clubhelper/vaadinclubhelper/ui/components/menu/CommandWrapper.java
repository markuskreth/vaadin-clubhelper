package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;

public class CommandWrapper implements Command, ClubCommand {

	private final ClubCommand command;

	public CommandWrapper(ClubCommand command) {
		super();
		this.command = command;
	}

	@Override
	public void menuSelected(MenuItem selectedItem) {
		execute();
	}

	@Override
	public String getLabel() {
		return command.getLabel();
	}

	@Override
	public Resource getIcon() {
		return command.getIcon();
	}

	@Override
	public void execute() {
		command.execute();
	}

}
