package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventDetails;

@Component
public class ClubhelperNavigation implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClubhelperNavigation.class);

	private ApplicationContext context;

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

	@Autowired
	PersonBusiness personBusiness;

	public void configure(UI mainUI) {

		navi = new ClubNavigator().init(mainUI);

		// Create and register the views

		Page page = mainUI.getPage();

		ViewFactory factory = new ViewFactory(page);
		MainView mainView = factory.createMain();

		navi.addView("", mainView);
		navi.addView(ClubhelperViews.MainView.name(), mainView);
		navi.addView(ClubhelperViews.LoginUI.name(), new LoginUI(personBusiness, securityGroupVerifier));
		navi.addView(ClubhelperViews.PersonEditView.name(), factory.createPersonEdit());
		navi.addView(ClubhelperViews.EventDetails.name(),
				new EventDetails(personBusiness, groupDao, eventBusiness, pflichtenDao, calendarAdapter));

		page.addBrowserWindowResizeListener(ev -> {
			int width = ev.getWidth();
			int height = ev.getHeight();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Changed Window Size: [w={},h={}]", width, height);
				Notification.show("Size Changed", "Changed Window Size: [w=" + width + ",h=" + height + "]",
						Notification.Type.TRAY_NOTIFICATION);
			}
		});
	}

	class ViewFactory {

		private Page page;

		public ViewFactory(Page page) {
			this.page = page;
		}

		public MainView createMain() {

			if (page.getBrowserWindowWidth() < 1000) {
				return new MainViewMobile(context, personBusiness, groupDao, eventBusiness, securityGroupVerifier);
			}
			else {
				return new MainViewDesktop(context, personBusiness, groupDao, eventBusiness, securityGroupVerifier);
			}
		}

		public PersonEditView createPersonEdit() {
			return new PersonEditView(groupDao, personBusiness, (page.getBrowserWindowWidth() >= 1000));
		}
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

		public void navigateTo(ClubhelperViews view) {
			navigateTo(view.name());
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
			}
			else {
				navigationViewNames.add(byState);
				super.navigateTo(navigationState);
			}

		}

		public void back() {
			navigationViewNames.pop();
			navigateTo(navigationViewNames.pop().name());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
