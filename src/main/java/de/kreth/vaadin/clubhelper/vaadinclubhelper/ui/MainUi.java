package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import de.kreth.clubhelperbackend.google.calendar.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

@SpringUI
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;
	@Autowired
	PersonDao dao;
	private ClubEventProvider dataProvider;

	@Override
	protected void init(VaadinRequest request) {

		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(new Label("Persons found:"));

		List<Person> persons = dao.list();
		PersonGrid grid = new PersonGrid();
		grid.setItems(persons);
		grid.setCaption("Person Grid");

		dataProvider = new ClubEventProvider();
		Calendar<ClubEvent> calendar = new Calendar<>(dataProvider)
				.withMonth(Month.from(LocalDateTime.now()));
		calendar.setCaption("Events");
		calendar.setHandler(this::onItemClick);

		layout.addComponents(grid, calendar);
		setContent(layout);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(() -> {

			EventBusiness business = new EventBusiness();
			List<ClubEvent> events = business.loadEvents(request);
			dataProvider.setItems(events);
			System.out.println("Updated data: " + events);
		});
		exec.shutdown();
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {
		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		Notification.show("Clicked: " + ev);
	}

	class ClubEventProvider extends BasicItemProvider<ClubEvent> {

		private static final long serialVersionUID = -5415397258827236704L;

		@Override
		public void setItems(Collection<ClubEvent> items) {
			super.setItems(items);
			fireItemSetChanged();
		}

	}

}
