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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
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

		List<Person> persons = personDao.list();
		personGrid = new PersonGrid(groupDao);
		personGrid.setItems(persons);
		personGrid.setCaption("Personen");
		personGrid.onClosedFunction(() -> detailClosed());
		personGrid.onPersonSelect(ev -> personSelectionChange(ev));

		this.calendar = new CalendarComponent();
		calendar.setHandler(this::onItemClick);

		contentLayout = new HorizontalLayout();
		contentLayout.setSizeFull();
		contentLayout.addComponents(calendar);
		
		setContent(contentLayout);
		setSizeFull();

//		final List<ClubEvent> events = eventBusiness.loadEvents(request);
//		calendar.setItems(events);
//		for (ClubEvent ev : events) {
//			if (ev.getPersons() != null && ev.getPersons().size()>0) {
//				System.out.println(ev.getCaption());
//				for (Person p: ev.getPersons()) {
//					System.out.println("\t" + p.getPrename() + "=" + persons.contains(p));
//				}
//			}
//		}
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
	}

	private void personSelectionChange(SelectionEvent<Person> ev) {
		Set<Person> selected = ev.getAllSelectedItems();
		System.out.println("Selection changed to: " + selected);
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
		personGrid.setVisible(true);
		personGrid.selectItems(ev.getPersons());
		eventBusiness.setSelected(ev);
	}

}
