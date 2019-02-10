package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventDetails;

@Component
public class ClubhelperNavigation {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClubhelperNavigation.class);

	@Autowired
	PersonDao personDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	EventBusiness eventBusiness;

	@Autowired
	PflichtenDao pflichtenDao;

	@Autowired
	SecurityVerifier securityGroupVerifier;

	@Autowired
	CalendarAdapter calendarAdapter;

	private ClubNavigator navi;

	public void configure(UI mainUI) {

		navi = new ClubNavigator().init(mainUI);

		// Create and register the views
		MainView mainView = new MainViewDesktop(personDao, groupDao, eventBusiness, securityGroupVerifier);
		navi.addView("", mainView);
		navi.addView(ClubhelperViews.MainView.name(), mainView);
		navi.addView(ClubhelperViews.LoginUI.name(), new LoginUI(personDao, securityGroupVerifier));
		navi.addView(ClubhelperViews.PersonEditView.name(), new PersonEditView(groupDao, personDao));
		navi.addView(ClubhelperViews.EventDetails.name(),
				new EventDetails(personDao, groupDao, eventBusiness, pflichtenDao, calendarAdapter));

		mainUI.getPage().addBrowserWindowResizeListener(ev -> {
			int width = ev.getWidth();
			int height = ev.getHeight();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Changed Window Size: [w={},h={}]", width, height);
				Notification.show("Size Changed", "Changed Window Size: [w=" + width + ",h=" + height + "]",
						Notification.Type.TRAY_NOTIFICATION);
			}
		});
	}

	public void navigateTo(String navigationState) {
		navi.navigateTo(navigationState);
	}

	public class ClubNavigator extends Navigator {

		private static final long serialVersionUID = -6503600786209888296L;
		private final Stack<ClubhelperViews> navigationViewNames = new Stack<>();

		ClubNavigator init(UI ui) {
			init(ui, null, new SingleComponentContainerViewDisplay(ui));
			return this;
		}

		@Override
		public void navigateTo(String navigationState) {
			ClubhelperViews byState = ClubhelperViews.byState(navigationState);
			if (navigationViewNames.contains(byState)) {
				int index = navigationViewNames.indexOf(byState);
				LOGGER.warn("Navigating to previously visited View. Removing some history");
				while (navigationViewNames.size() > index) {
					navigationViewNames.remove(index);
				}
			}

			int width = navi.getUI().getPage().getBrowserWindowWidth();
			boolean loggedIn = securityGroupVerifier.isLoggedin();
			if (!loggedIn && width < 1000) {
				navigationViewNames.clear();
				navigationViewNames.add(ClubhelperViews.MainView);
				super.navigateTo(ClubhelperViews.LoginUI.name());
			} else {
				navigationViewNames.add(byState);
				super.navigateTo(navigationState);
			}

		}

		public void back() {
			navigationViewNames.pop();
			navigateTo(navigationViewNames.pop().name());
		}
	}
}
