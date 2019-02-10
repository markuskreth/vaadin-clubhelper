package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

public enum ClubhelperViews {
	MainView, EventDetails, PersonEditView, LoginUI;

	public static ClubhelperViews byState(String state) {
		for (ClubhelperViews v : values()) {
			if (state.startsWith(v.name())) {
				return v;
			}
		}
		throw new IllegalArgumentException("View not found for state=" + state);
	}
}
