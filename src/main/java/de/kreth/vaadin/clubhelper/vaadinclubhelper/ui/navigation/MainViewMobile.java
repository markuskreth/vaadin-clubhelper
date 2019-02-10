package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;

public class MainViewMobile extends MainView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3293470536470926668L;

	private Grid<ClubEvent> eventGrid;

	private HeadView head;

	private HorizontalLayout eventButtonLayout;

	public MainViewMobile(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness,
			SecurityVerifier securityGroupVerifier) {
		super(personDao, groupDao, eventBusiness, securityGroupVerifier);
	}

	@Override
	public void initUI(ViewChangeEvent event) {
		super.initUI(event);
		if (!securityVerifier.isLoggedin()) {
			navigator.navigateTo(ClubhelperViews.LoginUI);
			return;
		}

		head = new HeadView(navigator, component -> showDateTimeDialog(component, "Startdatum"),
				component -> showDateTimeDialog(component, "Endedatum"), new ClubEventProvider(), securityVerifier);

		eventGrid = new Grid<>();
		eventGrid.setCaption("Veranstaltungen");
		eventGrid.setSizeFull();
		eventGrid.setSelectionMode(SelectionMode.SINGLE);

		eventGrid.addComponentColumn(ev -> {
			Label l = new Label();
			l.setHeight("15px");
			l.setWidth("15px");
			l.addStyleName(ev.getOrganizerDisplayName());
			return l;
		}).setSortable(true).setHidable(false);
		eventGrid.addColumn(ClubEvent::getCaption).setCaption("Name").setSortable(true);
		eventGrid.addColumn(ClubEvent::getStart).setCaption("Start").setSortable(true).setHidable(true);
		ConfigurableFilterDataProvider<ClubEvent, Void, SerializablePredicate<ClubEvent>> eventDataProvider = DataProvider
				.ofCollection(eventBusiness.loadEvents()).withConfigurableFilter();
		eventDataProvider.setFilter(this::filter);
		eventGrid.setDataProvider(eventDataProvider);

		eventGrid.addSelectionListener(select -> {
			Optional<ClubEvent> item = select.getFirstSelectedItem();
			personGrid.setVisible(item.isPresent());
			eventButtonLayout.setVisible(item.isPresent());
			if (item.isPresent()) {
				ClubEvent ev = item.get();
				eventBusiness.setSelected(ev);
				personGrid.setEvent(ev);
			}

		});

		Button eventDetails = new Button("Veranstaltung Details", ev -> {
			navigator.navigateTo(ClubhelperViews.EventDetails);
		});
		eventDetails.setId("person.eventDetails");

		eventButtonLayout = new HorizontalLayout();
		eventButtonLayout.setSpacing(true);
		eventButtonLayout.addComponents(eventDetails);
		eventButtonLayout.setVisible(false);

		addComponent(head);
		addComponent(eventGrid);
		addComponent(eventButtonLayout);
		addComponent(personGrid);
	}

	private ZonedDateTime showDateTimeDialog(Component source, String caption) {
		Window window = new Window();
		window.setCaption(caption);
		window.setModal(true);

		DateField dateField = new DateField();
		window.setContent(dateField);
		source.getUI().addWindow(window);
		LocalDate value = dateField.getValue();
		return ZonedDateTime.from(value);
//		if (caption.toLowerCase().contains("Start")) {
//			return ZonedDateTime.now().withDayOfMonth(1);
//		} else {
//			return ZonedDateTime.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
//		}
	}

	private boolean filter(ClubEvent ev) {
		return ev.getStart().isAfter(ZonedDateTime.now().minusDays(10));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
	}

	@Override
	public void openDetailForEvent(ClubEvent ev) {
		super.openDetailForEvent(ev);

	}

	@Override
	public void detailClosed() {
		super.detailClosed();
	}

}
