package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;

public class LoginCommand implements ClubCommand {

	private final ClubNavigator navigator;

	public LoginCommand(ClubNavigator navigator2) {
		super();
		this.navigator = navigator2;
	}

	@Override
	public void execute() {

		navigator.navigateTo(ClubhelperViews.LoginUI.name());
	}

	@Override
	public String getLabel() {
		return "Anmelden";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.USER;
	}

}
