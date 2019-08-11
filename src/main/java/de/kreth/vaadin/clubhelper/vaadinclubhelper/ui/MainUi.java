package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemStateFactory;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;

@Theme("vaadin-clubhelpertheme")
@SpringUI
@Push(value = PushMode.MANUAL)
@PreserveOnRefresh
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainUi.class);

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

	@Autowired
	ClubhelperNavigation clubhelperNavigation;

	@Autowired
	MenuItemStateFactory menuStateFactory;

	@Override
	protected void init(VaadinRequest request) {

		Page page = getPage();
		int browserWidth = page.getBrowserWindowWidth();
		WebBrowser webBrowser = page.getWebBrowser();

		LOGGER.debug("Starting Vaadin UI with {}, windowWidth={}, touchDevice={}, Android={}, IPad={}, IPhone={}",
				getClass().getName(), browserWidth, webBrowser.isTouchDevice(), webBrowser.isAndroid(),
				webBrowser.isIPad(), webBrowser.isIPhone());

		page.setTitle("Vereinshelfer");

		clubhelperNavigation.configure(this);
		menuStateFactory.configure(getUI());
		clubhelperNavigation.navigateTo(ClubhelperViews.MainView.name());
	}

}
