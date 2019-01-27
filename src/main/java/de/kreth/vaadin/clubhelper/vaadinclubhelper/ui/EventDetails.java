package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung.EventMeldung;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventAltersgruppen;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;

public class EventDetails extends GridLayout implements NamedView {

	public static final String VIEW_NAME = "EventDetails";
	private static final long serialVersionUID = 8290150079638390995L;

	private final EventBusiness eventBusiness;
	private final PersonDao personDao;
	private final GroupDao groupDao;
	private final PflichtenDao pflichtenDao;

	private ClubEvent currentEvent;
	private SingleEventView eventView;
	private PersonGrid personGrid;
	private EventAltersgruppen eventAltersgruppen;

	public EventDetails(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness,
			PflichtenDao pflichtenDao) {
		super(3, 5);
		this.eventBusiness = eventBusiness;
		this.personDao = personDao;
		this.groupDao = groupDao;
		this.pflichtenDao = pflichtenDao;
	}

	@Override
	public void enter(ViewChangeEvent event) {

		currentEvent = eventBusiness.getCurrent();
		if (eventView == null) {

			Navigator navigator = event.getNavigator();

			eventView = new SingleEventView(true);
			eventView.addDataUpdatedListener(() -> eventBusiness.storeEventType());

			eventAltersgruppen = new EventAltersgruppen(pflichtenDao, eventBusiness);
			eventView.addDataUpdatedListener(eventAltersgruppen);

			personGrid = new PersonGrid(groupDao, personDao);
			personGrid.hideFilter();
			personGrid.setSelectionMode(SelectionMode.NONE);

			Button back = new Button("Zurück");
			back.addClickListener(ev -> navigator.navigateTo(((NamedView) event.getOldView()).getViewName()));
			Button createMeldung = new Button("Meldung");
			createMeldung.addClickListener(ev -> show(eventBusiness.createMeldung()));

			HorizontalLayout buttonLayout = new HorizontalLayout(back, createMeldung);
			buttonLayout.setMargin(true);
			buttonLayout.setSpacing(true);

			addComponent(eventView, 0, 0);
			addComponent(eventAltersgruppen, 1, 0);
			addComponent(personGrid, 2, 0);
			addComponent(buttonLayout, 0, 4, 2, 4);
			setSizeFull();
		} else {
			eventAltersgruppen.updateData();
		}
		eventView.setEvent(currentEvent);
		personGrid.setEvent(currentEvent);
	}

	private void show(EventMeldung createMeldung) {
		VerticalLayout content = new VerticalLayout();
		content.addComponent(new Label(createMeldung.toString(), ContentMode.PREFORMATTED));

		Window dlg = new Window("Meldung für " + eventBusiness.getCurrent().getCaption());
		dlg.setContent(content);
		dlg.center();
		getUI().addWindow(dlg);

	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
