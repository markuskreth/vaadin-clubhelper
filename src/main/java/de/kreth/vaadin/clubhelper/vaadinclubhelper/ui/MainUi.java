package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;

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

	@Override
	protected void init(VaadinRequest request) {

		LOGGER.debug("Starting Vaadin UI with {}", getClass().getName());

		getPage().setTitle("Vereinshelfer");

		Navigator navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView(MainView.VIEW_NAME, new MainView(personDao, groupDao, eventBusiness));
		navigator.addView(LoginUI.VIEW_NAME, new LoginUI(personDao));
		navigator.addView(PersonEditView.VIEW_NAME, new PersonEditView(groupDao, personDao));
		navigator.addView(EventDetails.VIEW_NAME, new EventDetails(personDao, groupDao, eventBusiness));
		navigator.navigateTo(MainView.VIEW_NAME);
	}

}
