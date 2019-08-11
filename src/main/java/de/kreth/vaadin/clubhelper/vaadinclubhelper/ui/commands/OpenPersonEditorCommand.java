package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;

public class OpenPersonEditorCommand implements ClubCommand {

	private ClubNavigator navigator;

	public OpenPersonEditorCommand(ClubNavigator navigator2) {
		this.navigator = navigator2;
	}

	@Override
	public String getLabel() {
		return "Personen verwalten";
	}

	@Override
	public Resource getIcon() {
		return null;
	}

	@Override
	public void execute() {
		navigator.navigateTo(ClubhelperViews.PersonEditView.name());
	}

}
