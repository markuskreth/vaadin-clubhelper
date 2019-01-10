package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.BackwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ForwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;

import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class CalendarComponent extends CustomComponent {

	private static final long serialVersionUID = -9152173211931554059L;
	private transient final Logger log = LoggerFactory.getLogger(getClass());

	private ClubEventProvider dataProvider;
	private Calendar<ClubEvent> calendar;
	private HeadComponent head;

	public CalendarComponent() {

		dataProvider = new ClubEventProvider();
		calendar = new Calendar<>(dataProvider).withMonth(Month.from(LocalDateTime.now()));
		calendar.setId("calendar.calendar");

		calendar.setCaption("Events");
		calendar.setSizeFull();
		calendar.addListener(ev -> calendarEvent(ev));
		head = new HeadComponent(() -> calendar.getStartDate(), () -> calendar.getEndDate(), dataProvider);
		head.updateMonthText(calendar.getStartDate());

		VerticalLayout layout = new VerticalLayout(head, calendar);
		layout.setSizeFull();
		setCompositionRoot(layout);
	}

	private void calendarEvent(Event ev) {
		log.debug("Event on calendar: {}", ev);
		if (ev instanceof BackwardEvent || ev instanceof ForwardEvent) {
			head.updateMonthText(calendar.getStartDate());
		}
	}

	public void setToday(ZonedDateTime date) {
		calendar.withDayInMonth(date);
	}

	public Registration setHandler(ItemClickHandler listener) {
		return calendar.setHandler(listener);
	}

	public void setItems(Collection<ClubEvent> items) {
		dataProvider.setItems(items);
		calendar.markAsDirty();
	}

	public static class ClubEventProvider extends BasicItemProvider<ClubEvent> {

		private static final long serialVersionUID = -5415397258827236704L;

		@Override
		public void setItems(Collection<ClubEvent> items) {
			super.setItems(items);
			fireItemSetChanged();
		}

	}

}
