package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.time.ZonedDateTime;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.MenuBar.MenuItem;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarMonthCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarYearCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCsvCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LogoutCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.SwitchViewCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ClubEventProvider;
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

	public LoggedinMenuitemState(ApplicationContext context, Supplier<ZonedDateTime> startProvider,
			Supplier<ZonedDateTime> endProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super();
		this.navigator = context.getBean(ClubhelperNavigation.class);
		this.context = context;
		this.dataProvider = context.getBean(ClubEventProvider.class);
		this.startProvider = startProvider;
		this.endProvider = endProvider;
		this.printConsumer = printConsumer;
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {

		MenuItem fileMenuItem = menuBar.getFileMenuItem();
		fileMenuItem.setVisible(true);
		CommandWrapper logout = new CommandWrapper(new LogoutCommand(navigator2, securityVerifier));
		fileMenuItem.addItem(logout.getLabel(), logout);

		MenuItem editMenu = menuBar.getEditMenuItem();

		CommandWrapper exportCalendarMonthCommand = new CommandWrapper(
				new ExportCalendarMonthCommand(startProvider, endProvider, dataProvider, printConsumer));
		editMenu.addItem(exportCalendarMonthCommand.getLabel(), exportCalendarMonthCommand);
		ClubCommand exportCalendarYearCommand = new ExportCalendarYearCommand(startProvider, endProvider, dataProvider,
				printConsumer);
		editMenu.addItem(exportCalendarYearCommand.getLabel(), new CommandWrapper(exportCalendarYearCommand));
		ClubCommand exportCsvCommand = new ExportCsvCommand(menuBar, context);
		editMenu.addItem(exportCsvCommand.getLabel(), ev -> exportCsvCommand.execute());

		MenuItem viewMenu = menuBar.getViewMenuItem();
		viewMenu.setVisible(true);
		CommandWrapper openPersonEditor = new CommandWrapper(
				new SwitchViewCommand(context, "Personen verwalten", VaadinIcons.EDIT, ClubhelperViews.PersonEditView));
		MenuItem openPersonMenuItem = viewMenu.addItem(openPersonEditor.getLabel(), openPersonEditor);
		openPersonMenuItem.setCheckable(true);
		CommandWrapper calendarView = new CommandWrapper(
				new SwitchViewCommand(context, "Hauptansicht", VaadinIcons.CALENDAR, ClubhelperViews.MainView));
		MenuItem calendarMenuItem = viewMenu.addItem(calendarView.getLabel(), calendarView);
		calendarMenuItem.setCheckable(true);
		View current = navigator.getNavigator().getCurrentView();
		ClubhelperViews view = ClubhelperViews.byView(current);
		if (ClubhelperViews.PersonEditView == view) {
			openPersonMenuItem.setChecked(true);
		}
		else {
			calendarMenuItem.setChecked(true);
		}
	}

}
