package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung.EventMeldung;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.CreateMeldungCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.DeleteEventCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarMonthCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarYearCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCsvCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LogoutCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.SwitchViewCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ConfirmDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;
import net.sf.jasperreports.engine.JasperPrint;

class LoggedinMenuitemState implements MenuItemState {

	private ClubNavigator navigator2;

	private SecurityVerifier securityVerifier;

	private ClubhelperNavigation navigator;

	private ApplicationContext context;

	private ClubEventProvider dataProvider;

	private Supplier<ZonedDateTime> startProvider;

	private Supplier<ZonedDateTime> endProvider;

	private BiConsumer<String, JasperPrint> printConsumer;

	private MenuItem openPersonMenuItem;

	private MenuItem calendarMenuItem;

	private UI ui;

	private MenuItem eventDetailItem;

	private EventBusiness eventBusiness;

	private MenuItem createMeldungMenuItem;

	private MenuItem deleteMenuItem;

	public LoggedinMenuitemState(ApplicationContext context, UI ui, Supplier<ZonedDateTime> startProvider,
			Supplier<ZonedDateTime> endProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super();
		this.ui = ui;
		this.context = context;
		this.startProvider = startProvider;
		this.endProvider = endProvider;
		this.printConsumer = printConsumer;
		this.navigator = context.getBean(ClubhelperNavigation.class);
		this.dataProvider = context.getBean(ClubEventProvider.class);
		this.eventBusiness = context.getBean(EventBusiness.class);
		navigator.add(ev -> setSelectedMenuItem(ev.getNewView()));

		View current = navigator.getNavigator().getCurrentView();
		ClubhelperViews view = ClubhelperViews.byView(current);

		eventBusiness.add(ev -> setSelectedMenuItem(view));
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {

		menuBar.getAllMainMenus().forEach(m -> m.setVisible(false));

		MenuItem fileMenuItem = menuBar.getFileMenuItem();
		fileMenuItem.setVisible(true);
		CommandWrapper logout = new CommandWrapper(new LogoutCommand(navigator2, securityVerifier));
		fileMenuItem.addItem(logout.getLabel(), logout);

		prepareEditMenu(menuBar);

		prepareViewMenu(menuBar);

		menuBar.getSettingsItem().setVisible(false);

		View current = navigator.getNavigator().getCurrentView();
		ClubhelperViews view = ClubhelperViews.byView(current);
		setSelectedMenuItem(view);
	}

	private void prepareViewMenu(ClubhelperMenuBar menuBar) {
		MenuItem viewMenu = menuBar.getViewMenuItem();
		viewMenu.setVisible(true);

		CommandWrapper calendarView = new CommandWrapper(
				new SwitchViewCommand(context, "Hauptansicht", VaadinIcons.CALENDAR, ClubhelperViews.MainView));
		calendarMenuItem = viewMenu.addItem(calendarView.getLabel(), calendarView);
		calendarMenuItem.setCheckable(true);

		CommandWrapper openPersonEditor = new CommandWrapper(
				new SwitchViewCommand(context, "Personen verwalten", VaadinIcons.EDIT, ClubhelperViews.PersonEditView));
		openPersonMenuItem = viewMenu.addItem(openPersonEditor.getLabel(), openPersonEditor);
		openPersonMenuItem.setCheckable(true);

		CommandWrapper detailViewCommand = new CommandWrapper(
				new SwitchViewCommand(context, "Veranstaltung Detail", null, ClubhelperViews.EventDetails));
		eventDetailItem = viewMenu.addItem(detailViewCommand.getLabel(), detailViewCommand);
		eventDetailItem.setCheckable(true);
	}

	private void prepareEditMenu(ClubhelperMenuBar menuBar) {
		MenuItem editMenu = menuBar.getEditMenuItem();
		editMenu.setVisible(true);

		CommandWrapper exportCalendarMonthCommand = new CommandWrapper(
				new ExportCalendarMonthCommand(startProvider, endProvider, dataProvider, printConsumer));
		editMenu.addItem(exportCalendarMonthCommand.getLabel(), exportCalendarMonthCommand);
		ClubCommand exportCalendarYearCommand = new ExportCalendarYearCommand(startProvider, endProvider, dataProvider,
				printConsumer);
		editMenu.addItem(exportCalendarYearCommand.getLabel(), new CommandWrapper(exportCalendarYearCommand));
		ClubCommand exportCsvCommand = new ExportCsvCommand(menuBar, context);
		editMenu.addItem(exportCsvCommand.getLabel(), ev -> exportCsvCommand.execute());

		CreateMeldungCommand createMeldungCommand = new CreateMeldungCommand(context, this::show);
		createMeldungMenuItem = editMenu.addItem(createMeldungCommand.getLabel(),
				new CommandWrapper(createMeldungCommand));

		CommandWrapper deleeteEvent = new CommandWrapper(new DeleteEventCommand(this::deleteEvent));
		deleteMenuItem = editMenu.addItem(deleeteEvent.getLabel(), deleeteEvent);
	}

	protected void setSelectedMenuItem(ClubhelperViews view) {

		if (eventBusiness.getCurrent() != null) {
			createMeldungMenuItem.setEnabled(true);
			eventDetailItem.setVisible(true);
			deleteMenuItem.setEnabled(true);
		}
		else {
			createMeldungMenuItem.setEnabled(false);
			eventDetailItem.setVisible(false);
			deleteMenuItem.setEnabled(false);
		}

		for (MenuItem item : Arrays.asList(openPersonMenuItem, calendarMenuItem, eventDetailItem)) {
			item.setChecked(false);
			item.setEnabled(true);
		}
		if (ClubhelperViews.PersonEditView == view) {
			openPersonMenuItem.setChecked(true);
			openPersonMenuItem.setEnabled(false);
		}
		else if (ClubhelperViews.MainView == view) {
			calendarMenuItem.setChecked(true);
			calendarMenuItem.setEnabled(false);
		}
		else if (ClubhelperViews.EventDetails.equals(view)) {
			eventDetailItem.setChecked(true);
			eventDetailItem.setEnabled(false);
		}
	}

	private void show(EventMeldung createMeldung) {
		VerticalLayout content = new VerticalLayout();
		content.addComponent(new Label(createMeldung.toString(), ContentMode.PREFORMATTED));
		Window dlg = new Window("Meldung für " + eventBusiness.getCurrent().getCaption());
		dlg.setContent(content);
		dlg.center();
		ui.addWindow(dlg);

	}

	private void deleteEvent() {
		ClubEvent bean = eventBusiness.getCurrent();

		ConfirmDialog dlg = ConfirmDialog.builder().setCaption("Löschen bestätigen!").setMessage("Wollen Sie Termin \""
				+ bean.getCaption() + "\" vom " + bean.getStart() + "\" bis " + bean.getEnd()
				+ " wirklich löschen? Dieser Vorgang kann nicht rückgängig gemacht werden und betrifft auch den Online Google Calendar.")
				.setResultHandler(btn -> {
					if (btn == ConfirmDialog.Buttons.YES) {
						try {
							String host = ui.getPage().getLocation().getHost();

							CalendarAdapter calendarAdapter = context.getBean(CalendarAdapter.class);
							if (calendarAdapter.deleteEvent(host, bean.getOrganizerDisplayName(), bean.getId())) {
								eventBusiness.delete(bean);

								View current = navigator.getNavigator().getCurrentView();
								ClubhelperViews view = ClubhelperViews.byView(current);
								if (ClubhelperViews.EventDetails.equals(view)) {
									navigator.getNavigator().back();
								}
							}
							else {
								Notification.show("Fehler beim Löschen von " + bean, "Bitte erneut versuchen.",
										Notification.Type.ERROR_MESSAGE);
							}
						}
						catch (IOException e) {
							Notification.show("Fehler beim Löschen von " + bean, e.toString(),
									Notification.Type.ERROR_MESSAGE);
						}
					}
				}).yesCancel().build();
		ui.addWindow(dlg);

	}

}
