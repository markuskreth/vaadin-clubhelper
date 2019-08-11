package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import org.springframework.context.ApplicationContext;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCsvCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LoginCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;

public class LoggedOffState implements MenuItemState {

	private final ClubNavigator navigator;

	private ApplicationContext context;

	public LoggedOffState(ApplicationContext context) {
		super();
		this.navigator = context.getBean(ClubhelperNavigation.class).getNavigator();
		this.context = context;
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {

		for (MenuItem item : menuBar.getAllMainMenus()) {
			item.setVisible(false);
		}
		MenuItem fileMenu = menuBar.getFileMenuItem();
		fileMenu.setVisible(true);
		LoginCommand loginCommand = new LoginCommand(navigator);
		fileMenu.addItem(loginCommand.getLabel(), new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				loginCommand.execute();
			}
		});
		MenuItem editMenu = menuBar.getEditMenuItem();
		editMenu.setVisible(true);
		ClubCommand exportCsvCommand = new ExportCsvCommand(menuBar, context);
		editMenu.addItem(exportCsvCommand.getLabel(), ev -> exportCsvCommand.execute());

	}

}
