package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import org.basilbourque.timecolumnrenderers.ZonedDateTimeRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

@Component
public class EventGrid extends Grid<ClubEvent> {

	private static final long serialVersionUID = -5435770187868470290L;

	private final transient DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	private ConfigurableFilterDataProvider<ClubEvent, Void, SerializablePredicate<ClubEvent>> eventDataProvider;

	private transient EventBusiness business;

	public EventGrid(@Autowired EventBusiness eventBusiness) {

		this.business = eventBusiness;

		setCaption("Veranstaltungen");
		setSizeFull();
		setSelectionMode(SelectionMode.NONE);
		addColumn(ClubEvent::getStart, dt -> {
			return dt != null ? df.format(dt) : "";
		}).setCaption("Start");

		addComponentColumn(ev -> {
			Label l = new Label();
			l.setHeight("15px");
			l.setWidth("15px");
			l.addStyleName(ev.getOrganizerDisplayName());
			return l;
		}).setSortable(true).setHidable(false);
		addColumn(ClubEvent::getStart).setCaption("Start")
				.setRenderer(new ZonedDateTimeRenderer(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
				.setSortable(true).setHidable(true);
		addColumn(ClubEvent::getCaption).setCaption("Bezeichnung");
		addColumn(ClubEvent::getLocation).setCaption("Ort");

		List<ClubEvent> loadEvents = business.loadEvents();

		eventDataProvider = DataProvider
				.ofCollection(loadEvents).withConfigurableFilter();
		eventDataProvider.setFilter(this::filter);
		setDataProvider(eventDataProvider);
	}

	private boolean filter(ClubEvent ev) {
		return ev.getStart().isAfter(ZonedDateTime.now().minusMonths(1).withDayOfMonth(1));
	}

}
