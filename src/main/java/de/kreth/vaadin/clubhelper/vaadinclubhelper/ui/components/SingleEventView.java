package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType.Type;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events.DataUpdatedEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events.DefaultDataUpdateHandler;

public class SingleEventView extends CustomComponent {

	private static final long serialVersionUID = 4701035948083549772L;

	private final TextField textTitle;
	private final TextField textLocation;

	private DateField startDate;

	private DateField endDate;
	private ComboBox<CompetitionType.Type> competitionType;

	private Binder<ClubEvent> binder;
	private DefaultDataUpdateHandler updateHandler = new DefaultDataUpdateHandler();

	private Button deleteButton;

	private CalendarAdapter calendarAdapter;

	private EventBusiness eventBusiness;
	private Runnable deletedHandler;

	public SingleEventView(boolean showCompetitionType) {
		setCaption("Gewählte Veranstaltung");
		addStyleName("bold-caption");
		setWidth(50.0f, Unit.PERCENTAGE);

		textTitle = new TextField();
		textTitle.setId("event.title");
		textTitle.setStyleName("title_label");
		textTitle.setCaption("Veranstaltung");
		textTitle.setEnabled(false);
		textTitle.setSizeFull();

		textLocation = new TextField();
		textLocation.setId("event.location");
		textLocation.setStyleName("title_label");
		textLocation.setCaption("Ort");
		textLocation.setEnabled(false);
		textLocation.setSizeFull();

		startDate = new DateField("Beginn");
		startDate.setEnabled(false);
		endDate = new DateField("Ende");
		endDate.setEnabled(false);

		endDate.addValueChangeListener(this::endDateVisibleCheck);

		textLocation.setHeight(endDate.getHeight(), endDate.getHeightUnits());

		binder = new Binder<>(ClubEvent.class);
		binder.forField(textTitle).bind(ClubEvent::getCaption, ClubEvent::setCaption);
		binder.forField(textLocation).bind(ClubEvent::getLocation, ClubEvent::setLocation);
		ZonedDateTimeConverter converter = new ZonedDateTimeConverter();

		binder.forField(startDate).withConverter(converter).bind(ClubEvent::getStart, ClubEvent::setStart);
		binder.forField(endDate).withConverter(converter).bind(ClubEvent::getEnd, ClubEvent::setEnd);
		binder.addStatusChangeListener(ev -> updateHandler.fireUpdateEvent());

		GridLayout layout;
		if (showCompetitionType) {
			competitionType = new ComboBox<>();
			competitionType.setItems(Type.values());
			binder.forField(competitionType).bind(ClubEvent::getType, ClubEvent::setType);
			layout = new GridLayout(2, 3);
		} else {
			layout = new GridLayout(2, 2);
		}

		deleteButton = new Button("Löschen");
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponents(textTitle, startDate, textLocation, endDate);
		if (showCompetitionType) {
			layout.addComponent(competitionType);
			deleteButton = new Button("Löschen");
			deleteButton.addClickListener(ev -> deleteEvent());
			layout.addComponent(deleteButton);
		}
		setCompositionRoot(layout);
	}

	private void deleteEvent() {
		ClubEvent bean = binder.getBean();

		ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Löschen bestätigen!").setMessage("Wollen Sie Termin \""
				+ bean.getCaption() + "\" vom " + bean.getStart() + "\" bis " + bean.getEnd()
				+ " wirklich löschen? Dieser Vorgang kann nicht rückgängig gemacht werden und betrifft auch den Online Google Calendar.")
				.setResultHandler(btn -> {
					if (btn == ConfirmDialog.Buttons.YES) {
						try {
							String host = getUI().getPage().getLocation().getHost();
							if (calendarAdapter.deleteEvent(host, bean.getOrganizerDisplayName(), bean.getId())) {
								eventBusiness.delete(bean);
								if (deletedHandler != null) {
									deletedHandler.run();
								}
							} else {
								Notification.show("Fehler beim Löschen von " + bean, "Bitte erneut versuchen.",
										Notification.Type.ERROR_MESSAGE);
							}
						} catch (IOException e) {
							Notification.show("Fehler beim Löschen von " + bean, e.toString(),
									Notification.Type.ERROR_MESSAGE);
						}
					}
				}).yesCancel().build();
		getUI().addWindow(dlg);

	}

	void endDateVisibleCheck(ValueChangeEvent<LocalDate> event) {
		ZonedDateTime start = binder.getBean().getStart();
		ZonedDateTime end = binder.getBean().getEnd();
		if (start.until(end, ChronoUnit.DAYS) > 0) {
			endDate.setValue(end.toLocalDate());
			endDate.setVisible(true);
		} else {
			endDate.setValue(null);
			endDate.setVisible(false);
		}
	}

	public void addDataUpdatedListener(DataUpdatedEvent ev) {
		updateHandler.add(ev);
	}

	public boolean remove(DataUpdatedEvent o) {
		return updateHandler.remove(o);
	}

	public void setDeletedHandler(Runnable deletedHandler) {
		this.deletedHandler = deletedHandler;
	}

	void setTitle(String value) {
		if (value == null) {
			value = "";
		}
		textTitle.setValue(value);
	}

	void setLocation(String value) {
		if (value == null) {
			value = "";
		}
		textLocation.setValue(value);
	}

	public void setEvent(ClubEvent ev) {

		binder.setBean(ev);

		if (ev != null) {
			deleteButton.setEnabled(true);
		} else {
			setTitle("");
			setLocation("");
			endDate.setVisible(false);
			deleteButton.setEnabled(false);
		}
	}

	public void setCalendarAdapter(CalendarAdapter calendarAdapter) {
		this.calendarAdapter = calendarAdapter;
	}

	public void setEventBusiness(EventBusiness eventBusiness) {
		this.eventBusiness = eventBusiness;
	}

}
