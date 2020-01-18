package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.MenuBar;

public class ClubhelperMenuBar extends MenuBar {

	private final MenuItem fileMenuItem;

	private final MenuItem editMenuItem;

	private final MenuItem viewMenuItem;

	private final MenuItem settingsItem;

	public ClubhelperMenuBar(MenuItemState initialState) {
		setWidth("100%");
		fileMenuItem = addItem("Datei");
		editMenuItem = addItem("Bearbeiten");
		viewMenuItem = addItem("Ansicht");
		settingsItem = addItem("Einstellungen");

		if (initialState != null) {
			applyState(initialState);
		}
	}

	public void applyState(MenuItemState state) {
		for (MenuItem item : getAllMainMenus()) {
			item.removeChildren();
		}
		state.applyMenuStates(this);
	}

	public MenuItem getFileMenuItem() {
		return fileMenuItem;
	}

	public MenuItem getEditMenuItem() {
		return editMenuItem;
	}

	public MenuItem getViewMenuItem() {
		return viewMenuItem;
	}

	public MenuItem getSettingsItem() {
		return settingsItem;
	}

	public List<MenuItem> getAllMainMenus() {
		return Arrays.asList(fileMenuItem, editMenuItem, viewMenuItem, settingsItem);
	}
}
