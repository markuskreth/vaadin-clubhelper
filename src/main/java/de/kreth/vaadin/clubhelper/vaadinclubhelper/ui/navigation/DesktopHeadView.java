package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.time.ZonedDateTime;
import java.util.function.Function;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;

public class DesktopHeadView extends HeadView {

	private static final long serialVersionUID = 1596573215389558000L;

	private Label monthName;

	public DesktopHeadView(ClubNavigator navigator, Function<Component, ZonedDateTime> startTime,
			Function<Component, ZonedDateTime> endTime, ClubEventProvider dataProvider,
			SecurityVerifier securityVerifier) {
		super(navigator, startTime, endTime, dataProvider, securityVerifier);

		monthName = new Label();
		monthName.setId("calendar.month");
		monthName.setStyleName("title_caption");
		monthName.setWidth(null);

		this.addComponent(monthName, 1);
		setComponentAlignment(monthName, Alignment.MIDDLE_CENTER);
		setExpandRatio(monthName, 1.0f);
	}

	public void updateMonthText(ZonedDateTime startDate) {
		String monthValue = dfMonth.format(startDate);
		log.debug("Changed Month title to {}", monthValue);
		monthName.setValue(monthValue);
	}

}
