package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ClubhelperException;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.ClubhelperErrorDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.ClubhelperMenuBar;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemState;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemStateFactory;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public abstract class MainView extends VerticalLayout implements View {

	private static final long serialVersionUID = 4831071242146146399L;

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private ApplicationContext context;

	private final GroupDao groupDao;

	protected final EventBusiness eventBusiness;

	protected final SecurityVerifier securityVerifier;

	protected PersonGrid personGrid;

	protected SingleEventView eventView;

	protected ClubNavigator navigator;

	private final PersonBusiness personBusiness;

	protected ClubEventProvider dataProvider;

	private ClubhelperMenuBar menuBar;

	private MenuItemStateFactory menuStateFactory;

	public MainView(ApplicationContext context2, GroupDao groupDao, EventBusiness eventBusiness,
			PersonBusiness personBusiness,
			SecurityVerifier securityGroupVerifier) {
		this.context = context2;
		this.personBusiness = personBusiness;
		this.groupDao = groupDao;
		this.eventBusiness = eventBusiness;
		this.securityVerifier = securityGroupVerifier;

		dataProvider = context2.getBean(ClubEventProvider.class);
		menuStateFactory = context.getBean(MenuItemStateFactory.class);
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

		MenuItemState state = menuStateFactory.currentState();
		menuBar.applyState(state);
	}

	public void initUI(ViewChangeEvent event) {

		MenuItemState state = menuStateFactory.currentState();
		menuBar = new ClubhelperMenuBar(state);

		navigator = (ClubNavigator) event.getNavigator();

		eventView = new SingleEventView(false);
		eventView.setId(eventView.getClass().getName());
		eventView.setVisible(false);

		personGrid = new PersonGrid(groupDao, personBusiness);
		personGrid.setCaption("Personen");
		personGrid.setSelectionMode(SelectionMode.MULTI);
		personGrid.onPersonSelect(ev -> personSelectionChange(ev));
		personGrid.setVisible(false);

		addComponent(menuBar);
	}

	public ApplicationContext getContext() {
		return context;
	}

	protected void showPrint(String calendarMonth, JasperPrint print) {
		Window window = new Window();
		window.setCaption("View PDF");
		AbstractComponent e;
		try {
			e = createEmbedded(calendarMonth, print);
		}
		catch (Exception e1) {

			return;
		}
		window.setContent(e);
		window.setModal(true);
		window.setWidth("50%");
		window.setHeight("90%");
		eventView.getUI().addWindow(window);
	}

	private AbstractComponent createEmbedded(String title, JasperPrint print) throws IOException, JRException {

		final PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream(in);

		final StreamResource resource = new StreamResource(() -> in, title);
		resource.setMIMEType("application/pdf");

		BrowserFrame c = new BrowserFrame("PDF invoice", resource);

		c.setSizeFull();

		try {
			JasperExportManager.exportReportToPdfStream(print, out);
		}
		finally {
			try {
				out.close();
				in.close();
			}
			catch (IOException e) {
				LOGGER.warn("Error closing Jasper output stream.", e);
			}

		}
		return c;
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

	public abstract Supplier<ZonedDateTime> startDateSupplier();

	public abstract Supplier<ZonedDateTime> endDateSupplier();
}
