package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import de.kreth.clubhelperbackend.google.calendar.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

@Theme("vaadin-clubhelpertheme")
@SpringUI
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;

	@Autowired
	PersonDao personDao;

	@Autowired
	GroupDao groupDao;

	private PersonGrid personGrid;

	private CalendarComponent calendar;

	private HorizontalLayout contentLayout;

	@Override
	protected void init(VaadinRequest request) {

		List<Person> persons = personDao.list();
		personGrid = new PersonGrid(groupDao);
		personGrid.setItems(persons);
		personGrid.setCaption("Personen");
		personGrid.setVisible(false);
		personGrid.onClosedFunction(() -> detailClosed());

		this.calendar = new CalendarComponent();
		calendar.setHandler(this::onItemClick);

		contentLayout = new HorizontalLayout();
		contentLayout.setSizeFull();
		contentLayout.addComponents(calendar, personGrid);
		contentLayout.setExpandRatio(calendar, 1.0f);

		setContent(contentLayout);
		setSizeFull();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(() -> {

			EventBusiness business = new EventBusiness();
			List<ClubEvent> events = business.loadEvents(request);
			calendar.setItems(events);
			System.out.println("Updated data: " + events);
		});
		exec.shutdown();
	}

	private void detailClosed() {
		personGrid.setVisible(false);
		calendar.setSizeFull();
		contentLayout.setExpandRatio(calendar, 1.0f);
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {
		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		showDetails(ev);
	}

	private void showDetails(ClubEvent ev) {
		personGrid.setVisible(true);
		personGrid.setCaption(ev.getCaption());
		personGrid.setTitle(ev.getCaption());

		contentLayout.setExpandRatio(calendar, .5f);
		calendar.setWidth("50%");

		Notification.show("" + ev);
	}

}
