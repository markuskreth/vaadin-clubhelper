package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ClubhelperException;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.ClubhelperErrorDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;

public abstract class MainView extends VerticalLayout implements View {

	private static final long serialVersionUID = 4831071242146146399L;

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private final PersonDao personDao;

	private final GroupDao groupDao;

	protected final EventBusiness eventBusiness;

	protected final SecurityVerifier securityVerifier;

	protected PersonGrid personGrid;

	protected SingleEventView eventView;

	protected ClubNavigator navigator;

	public MainView(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness,
			SecurityVerifier securityGroupVerifier) {
		this.personDao = personDao;
		this.groupDao = groupDao;
		this.eventBusiness = eventBusiness;
		this.securityVerifier = securityGroupVerifier;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (this.navigator == null) {
			initUI(event);
			LOGGER.info("Loaded UI and started fetch of Events");
		}
		else {
			if (securityVerifier.isLoggedin()) {
				LOGGER.info("{} already initialized - opening Person View.", getClass().getName());
				ClubEvent current = eventBusiness.getCurrent();
				openDetailForEvent(current);
			}
			else {
				LOGGER.info("{} already initialized - but not loggedin.", getClass().getName());
				detailClosed();
			}
		}

	}

	public void initUI(ViewChangeEvent event) {

		navigator = (ClubNavigator) event.getNavigator();

		eventView = new SingleEventView(false);
		eventView.setId(eventView.getClass().getName());
		eventView.setVisible(false);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setCaption("Personen");
		personGrid.setSelectionMode(SelectionMode.MULTI);
		personGrid.onPersonSelect(ev -> personSelectionChange(ev));
		personGrid.setVisible(false);

	}

	private void personSelectionChange(SelectionEvent<Person> ev) {
		Set<Person> selected = ev.getAllSelectedItems();
		LOGGER.debug("Selection changed to: {}", selected);
		try {
			eventBusiness.changePersons(selected);
		}
		catch (ClubhelperException e) {
			LOGGER.error("Error storing Persons.", e);

			String text = "Fehler beim Speichern der Personen dieser Veranstaltung";
			new ClubhelperErrorDialog(text, e).show(getUI());

		}
	}

	public void detailClosed() {
		LOGGER.debug("Closing detail view.");
		personGrid.setVisible(false);
		eventView.setVisible(false);
	}

	public void openDetailForEvent(ClubEvent ev) {
		LOGGER.debug("Opening detail view for {}", ev);

		eventBusiness.setSelected(null);
		eventView.setEvent(ev);

		personGrid.setEnabled(false);
		personGrid.setEvent(ev);
		personGrid.setEnabled(true);

		personGrid.setVisible(true);
		eventView.setVisible(true);

		eventBusiness.setSelected(ev);
	}

}
