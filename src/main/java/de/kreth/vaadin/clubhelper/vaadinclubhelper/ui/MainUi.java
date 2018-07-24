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
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

@Theme("vaadin-clubhelpertheme")
@SpringUI
public class MainUi extends UI {

	private static final long serialVersionUID = 7581634188909841919L;

	@Autowired
	PersonDao personDao;

	@Autowired
	GroupDao groupDao;

	private ClubEventProvider dataProvider;
	private PersonGrid personGrid;
	private EventGrid eventGrid;

	@Override
	protected void init(VaadinRequest request) {

		HorizontalLayout layout = new HorizontalLayout();

		List<Person> persons = personDao.list();
		personGrid = new PersonGrid(groupDao);
		personGrid.setItems(persons);
		personGrid.setCaption("Personen");
		personGrid.setVisible(false);

		eventGrid = new EventGrid();
		eventGrid.setCaption("Termine");
		eventGrid.addItemClickListener(ev -> showDetails(ev.getItem()));

		dataProvider = new ClubEventProvider();
		Calendar<ClubEvent> calendar = new Calendar<>(dataProvider)
				.withMonth(Month.from(LocalDateTime.now()));
		calendar.setCaption("Events");
		calendar.setHandler(this::onItemClick);

		layout.addComponents(calendar, personGrid, eventGrid);
		setContent(layout);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(() -> {

			EventBusiness business = new EventBusiness();
			List<ClubEvent> events = business.loadEvents(request);
			dataProvider.setItems(events);
			eventGrid.setItems(events);
			eventGrid.getDataProvider().refreshAll();
			System.out.println("Updated data: " + events);
		});
		exec.shutdown();
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {
		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		showDetails(ev);
	}

	private void showDetails(ClubEvent ev) {
		eventGrid.setVisible(false);
		personGrid.setVisible(true);
		personGrid.setCaption(ev.getCaption());
		personGrid.setTitle(ev.getCaption());

		Notification.show("" + ev);
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
