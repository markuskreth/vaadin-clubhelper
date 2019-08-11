package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.basilbourque.timecolumnrenderers.ZonedDateTimeRenderer;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.AbstractRenderer;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import elemental.js.json.JsJsonFactory;
import elemental.json.JsonArray;
import elemental.json.JsonValue;

public class EventGrid extends Grid<ClubEvent> {

	private ConfigurableFilterDataProvider<ClubEvent, Void, SerializablePredicate<ClubEvent>> eventDataProvider;

	private transient EventBusiness business;

	public EventGrid(EventBusiness eventBusiness) {
		this(eventBusiness, false);
	}

	public EventGrid(EventBusiness eventBusiness, boolean withCompetitors) {

		this.business = eventBusiness;

		setSizeFull();
		setSelectionMode(SelectionMode.NONE);

		addComponentColumn(ev -> {
			Label l = new Label();
			l.setHeight("15px");
			l.setWidth("15px");
			l.addStyleName(ev.getOrganizerDisplayName());
			return l;
		}).setSortable(true).setHidable(false);
		addColumn(ClubEvent::getStart).setCaption("Start")
				.setRenderer(new TestRenderer(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
				.setSortable(true).setHidable(true);
		addColumn(ClubEvent::getCaption).setCaption("Bezeichnung");
		addColumn(ClubEvent::getLocation).setCaption("Ort");
		if (withCompetitors) {
//			addColumn(ClubEvent::getPersons).setCaption("Teilnehmer").setRenderer(createRenderer());
		}

		List<ClubEvent> loadEvents = business.loadEvents();

		eventDataProvider = DataProvider
				.ofCollection(loadEvents).withConfigurableFilter();
		eventDataProvider.setFilter(this::filter);
		setDataProvider(eventDataProvider);
	}

	class TestRenderer extends ZonedDateTimeRenderer {

		protected TestRenderer(DateTimeFormatter formatter) {
			super(formatter);
		}

		@Override
		public JsonValue encode(ZonedDateTime value) {
			JsonValue encodeed = super.encode(value);
			return encodeed;
		}
	}

	private static final Set<Person> set = new HashSet<>();

	class PersonSetRenderer extends AbstractRenderer<Object, Set<Person>> {

		@SuppressWarnings("unchecked")
		protected PersonSetRenderer() {
			super((Class<Set<Person>>) set.getClass());
		}

		@Override
		public JsonValue encode(Set<Person> value) {
			JsJsonFactory jsonFactory = new JsJsonFactory();
			JsonArray personArray = jsonFactory.createArray();
			int index = 0;
			for (Person p : value) {
				personArray.set(index, p.getPrename() + " " + p.getSurname());
				index++;
			}
			return personArray;
		}

	}

	private boolean filter(ClubEvent ev) {
		return ev.getStart().isAfter(ZonedDateTime.now().minusMonths(1).withDayOfMonth(1));
	}

}
