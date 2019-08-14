package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

public class NavigationEvent {

	private final ClubhelperViews newView;

	NavigationEvent(ClubhelperViews newView) {
		super();
		this.newView = newView;
	}

	public ClubhelperViews getNewView() {
		return newView;
	}

}
