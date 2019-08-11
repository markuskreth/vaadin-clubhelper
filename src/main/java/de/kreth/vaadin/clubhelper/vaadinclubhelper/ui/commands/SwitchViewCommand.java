package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;

public class SwitchViewCommand implements ClubCommand {

	private ClubNavigator navigator;

	private ClubhelperViews target;

	private String label;

	private Resource icon;

	public SwitchViewCommand(ApplicationContext context, String label, Resource icon, ClubhelperViews target) {
		this.navigator = context.getBean(ClubhelperNavigation.class).getNavigator();
		this.label = label;
		this.target = target;
		this.icon = icon;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Resource getIcon() {
		return icon;
	}

	@Override
	public void execute() {
		navigator.navigateTo(target);
	}

}
