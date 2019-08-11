package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import com.vaadin.navigator.View;

public enum ClubhelperViews {
	/**
	 * With Alias ''
	 */
	MainView,
	EventDetails,
	PersonEditView,
	LoginUI;

	public static ClubhelperViews byState(String state) {
		if (state.isBlank()) {
			return MainView;
		}
		for (ClubhelperViews v : values()) {
			if (state.startsWith(v.name())) {
				return v;
			}
		}
		throw new IllegalArgumentException("View not found for state=" + state);
	}

	public static ClubhelperViews byView(View currentView) {
		if (currentView == null) {
			return MainView;
		}
		return byState(currentView.getClass().getSimpleName());
	}

}
