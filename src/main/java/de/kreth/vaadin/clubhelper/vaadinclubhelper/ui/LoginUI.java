package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class LoginUI extends VerticalLayout implements View {

	private static final long serialVersionUID = 4339018452507960084L;
	public static final String VIEW_NAME = "LoginUI";

	private Navigator navigator;
	private String parameters;

	public LoginUI(PersonDao personDao) {

		LoginForm lf = new LoginForm();
		lf.addLoginListener(e -> {

			String username = e.getLoginParameter("username");
			String password = e.getLoginParameter("password");

			try {
				Person loggedin = personDao.findLoginUser(username, password);
				this.getSession().setAttribute(Person.SESSION_LOGIN, loggedin);
				navigator.navigateTo(MainView.VIEW_NAME + '/' + parameters);
			} catch (final Exception ex) {
				String message = "Incorrect user or password:" + ex.getMessage() + e.getLoginParameter("username") + ":"
						+ e.getLoginParameter("password");
				Notification.show(message, Notification.Type.ERROR_MESSAGE);
			}
		});
		addComponent(lf);
		setComponentAlignment(lf, Alignment.MIDDLE_CENTER);
		setSizeFull();

	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		navigator = event.getNavigator();
		parameters = event.getParameters();
	}
}
