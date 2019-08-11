package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;

public class LogoutCommand implements ClubCommand {

	private final ClubNavigator navigator;

	private final SecurityVerifier securityVerifier;

	public LogoutCommand(ClubNavigator navigator2, SecurityVerifier securityVerifier) {
		super();
		this.navigator = navigator2;
		this.securityVerifier = securityVerifier;
	}

	@Override
	public String getLabel() {
		return "Abmelden";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.USER;
	}

	@Override
	public void execute() {
		securityVerifier.setLoggedinPerson(null);
		navigator.navigateTo(ClubhelperViews.MainView.name());
	}

}
