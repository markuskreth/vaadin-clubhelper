package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.function.Consumer;

import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;

import com.vaadin.shared.Registration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public interface CalendarView {

	boolean add(Consumer<ZonedDateTime> e);

	boolean remove(Consumer<ZonedDateTime> o);

	void setToday(ZonedDateTime date);

	Registration setHandler(ItemClickHandler listener);

	void setItems(Collection<ClubEvent> items);

	ZonedDateTime getStartDate();

	ZonedDateTime getEndDate();

}
