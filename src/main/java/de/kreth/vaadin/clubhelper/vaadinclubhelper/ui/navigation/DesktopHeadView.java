package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.time.ZonedDateTime;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;

public class DesktopHeadView extends HeadView {

	private static final long serialVersionUID = 1596573215389558000L;

	public DesktopHeadView(SecurityVerifier securityVerifier) {
		super(securityVerifier);
	}

	public void updateMonthText(ZonedDateTime startDate) {
		String monthValue = dfMonth.format(startDate);
		log.debug("Changed Month title to {}", monthValue);
		monthName.setValue(monthValue);
	}

}
