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
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.CreateMeldungCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.DeleteEventCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LogoutCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.SwitchViewCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ConfirmDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Status für authentifizierte User mit Menuitems für jedes View
 * @author markus
 *
 */
class LoggedinMenuitemState extends LoggedOffState {

	private SecurityVerifier securityVerifier;

	private ClubhelperNavigation navigator;

	private ApplicationContext context;

	private MenuItem openPersonMenuItem;

	private MenuItem calendarMenuItem;

	private UI ui;

	private MenuItem eventDetailItem;

	private EventBusiness eventBusiness;

	private MenuItem createMeldungMenuItem;

	private MenuItem deleteMenuItem;

	private MenuItem exportEmailsMenuItem;

	public LoggedinMenuitemState(ApplicationContext context, UI ui, Supplier<ZonedDateTime> startProvider,
			Supplier<ZonedDateTime> endProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super(context, startProvider, endProvider, printConsumer);
		this.ui = ui;
		this.context = context;
		this.navigator = context.getBean(ClubhelperNavigation.class);
		this.eventBusiness = context.getBean(EventBusiness.class);
		navigator.add(ev -> setSelectedMenuItem(ev.getNewView()));

		View current = navigator.getNavigator().getCurrentView();
		ClubhelperViews view = ClubhelperViews.byView(current);

		eventBusiness.add(ev -> setSelectedMenuItem(view));
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {
		super.applyMenuStates(menuBar);

		prepareEditMenu(menuBar);

		prepareViewMenu(menuBar);

		menuBar.getSettingsItem().setVisible(false);

		View current = navigator.getNavigator().getCurrentView();
		ClubhelperViews view = ClubhelperViews.byView(current);
		setSelectedMenuItem(view);
	}

	@Override
	protected ClubCommand loginOutCommand() {
		return new LogoutCommand(navigator.getNavigator(), securityVerifier);
	}

	private void prepareViewMenu(ClubhelperMenuBar menuBar) {
		MenuItem viewMenu = menuBar.getViewMenuItem();
		viewMenu.setVisible(true);

		CommandWrapper calendarView = new CommandWrapper(
				new SwitchViewCommand(context, "Hauptansicht", VaadinIcons.CALENDAR, ClubhelperViews.MainView));
		calendarMenuItem = calendarView.addTo(viewMenu);
		calendarMenuItem.setCheckable(true);

		CommandWrapper openPersonEditor = new CommandWrapper(
				new SwitchViewCommand(context, "Personen verwalten", VaadinIcons.EDIT, ClubhelperViews.PersonEditView));
		openPersonMenuItem = openPersonEditor.addTo(viewMenu);
		openPersonMenuItem.setCheckable(true);

		CommandWrapper detailViewCommand = new CommandWrapper(
				new SwitchViewCommand(context, "Veranstaltung Detail", null, ClubhelperViews.EventDetails));
		eventDetailItem = detailViewCommand.addTo(viewMenu);
		eventDetailItem.setCheckable(true);
	}

	private void prepareEditMenu(ClubhelperMenuBar menuBar) {
		MenuItem editMenu = menuBar.getEditMenuItem();
		editMenu.setVisible(true);

		CommandWrapper createMeldungCommand = new CommandWrapper(new CreateMeldungCommand(context, this::show));
		createMeldungMenuItem = createMeldungCommand.addTo(editMenu);

		CommandWrapper exportEmails = new CommandWrapper(new SwitchViewCommand(context, "Emails exportieren",
				VaadinIcons.CALENDAR, ClubhelperViews.ExportEmails));
		exportEmailsMenuItem = exportEmails.addTo(editMenu);

		CommandWrapper deleeteEvent = new CommandWrapper(new DeleteEventCommand(this::deleteEvent));
		deleteMenuItem = deleeteEvent.addTo(editMenu);

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

		exportEmailsMenuItem.setEnabled(false);

		if (ClubhelperViews.PersonEditView == view) {
			openPersonMenuItem.setChecked(true);
			openPersonMenuItem.setEnabled(false);
		}
		else if (ClubhelperViews.MainView == view) {
			calendarMenuItem.setChecked(true);
			calendarMenuItem.setEnabled(false);
			exportEmailsMenuItem.setEnabled(true);
		}
		else if (ClubhelperViews.EventDetails.equals(view)) {
			eventDetailItem.setChecked(true);
			eventDetailItem.setEnabled(false);
		}
	}

	private void show(String preformattedText) {
		VerticalLayout content = new VerticalLayout();
		content.addComponent(new Label(preformattedText, ContentMode.PREFORMATTED));
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
