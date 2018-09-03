package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

@Theme("vaadin-clubhelpertheme")
@SpringUI
@Push(value=PushMode.MANUAL)
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainUi.class);

	@Autowired
	PersonDao personDao;

	@Autowired
	GroupDao groupDao;
	
	@Autowired
	EventBusiness eventBusiness;

	private PersonGrid personGrid;

	private CalendarComponent calendar;

	private HorizontalLayout contentLayout;

	@Override
	protected void init(VaadinRequest request) {

		LOGGER.debug("Starting Vaadin UI with " + getClass().getName());

		List<Person> persons = personDao.listAll();
		personGrid = new PersonGrid(groupDao);
		personGrid.setItems(persons);
		personGrid.setCaption("Personen");
		personGrid.onClosedFunction(() -> detailClosed());
		personGrid.onPersonSelect(ev -> personSelectionChange(ev));
		personGrid.onPersonEdit(p -> onPersonEdit(p));

		this.calendar = new CalendarComponent();
		calendar.setHandler(this::onItemClick);

		contentLayout = new HorizontalLayout();
		contentLayout.setSizeFull();
		contentLayout.addComponents(calendar);
		
		setContent(contentLayout);
		setSizeFull();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(() -> {

			final List<ClubEvent> events = eventBusiness.loadEvents(request);
			LOGGER.info("Loaded events: {}", events);
			final UI ui = calendar.getUI();
			ui.access(() -> {
				calendar.setItems(events);
				ui.push();
			});
			
		});
		exec.shutdown();
		LOGGER.info("Loaded UI and started fetch of Events");
	}

	private void onPersonEdit(Person p) {
		PersonEditDialog dlg = new PersonEditDialog(groupDao.listAll(), p, personDao);
		getUI().addWindow(dlg);
	}
	
	private void personSelectionChange(SelectionEvent<Person> ev) {
		Set<Person> selected = ev.getAllSelectedItems();
		LOGGER.debug("Selection changed to: {}", selected);
		eventBusiness.changePersons(selected);
	}

	private void detailClosed() {
		LOGGER.debug("Closing detail view.");
		contentLayout.removeComponent(personGrid);
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {
		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		LOGGER.debug("Opening detail view for {}", ev);

		contentLayout.removeComponent(personGrid);
		contentLayout.addComponent(personGrid);
		
		personGrid.setCaption(ev.getCaption());
		personGrid.setTitle(ev.getCaption());
		personGrid.setEnabled(false);
		personGrid.selectItems(ev.getPersons());
		personGrid.setVisible(true);
		personGrid.setEnabled(true);
		eventBusiness.setSelected(ev);
	}

}
