package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.BackwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ForwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;

import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class CalendarComponent extends CustomComponent implements CalendarView {

	private static final long serialVersionUID = -9152173211931554059L;

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	private final transient List<Consumer<ZonedDateTime>> dateUpdateEvents;

	private final ClubEventProvider dataProvider;

	private final Calendar<ClubEvent> calendar;

	public CalendarComponent(ClubEventProvider dataProvider) {

		this.dataProvider = dataProvider;
		dateUpdateEvents = new ArrayList<>();

		calendar = new Calendar<>(dataProvider).withMonth(Month.from(LocalDateTime.now()));
		calendar.setId("calendar.calendar");

		calendar.setCaption("Events");
		calendar.setSizeFull();
		calendar.addListener(this::calendarEvent);

		setCompositionRoot(calendar);
	}

	@Override
	public boolean add(Consumer<ZonedDateTime> e) {
		return dateUpdateEvents.add(e);
	}

	@Override
	public boolean remove(Consumer<ZonedDateTime> o) {
		return dateUpdateEvents.remove(o);
	}

	private void calendarEvent(Event ev) {
		log.debug("Event on calendar: {}", ev);
		if (ev instanceof BackwardEvent || ev instanceof ForwardEvent) {
			for (Consumer<ZonedDateTime> l : dateUpdateEvents) {
				l.accept(calendar.getStartDate());
			}
		}
	}

	@Override
	public void setToday(ZonedDateTime date) {
		calendar.withDayInMonth(date);
	}

	@Override
	public Registration setHandler(ItemClickHandler listener) {
		return calendar.setHandler(listener);
	}

	@Override
	public void setItems(Collection<ClubEvent> items) {
		dataProvider.setItems(items);
		calendar.markAsDirty();
	}

	@Override
	public ZonedDateTime getStartDate() {
		return calendar.getStartDate();
	}

	@Override
	public ZonedDateTime getEndDate() {
		return calendar.getEndDate();
	}

}
