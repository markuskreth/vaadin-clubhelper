package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.time.ZonedDateTime;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;

import com.vaadin.ui.MenuBar.MenuItem;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarMonthCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarYearCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCsvCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LoginCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import net.sf.jasperreports.engine.JasperPrint;

class LoggedOffState implements MenuItemState {

	private final ClubNavigator navigator;

	private final ApplicationContext context;

	private final ClubEventProvider dataProvider;

	private final Supplier<ZonedDateTime> startProvider;

	private final Supplier<ZonedDateTime> endProvider;

	private BiConsumer<String, JasperPrint> printConsumer;

	public LoggedOffState(ApplicationContext context, Supplier<ZonedDateTime> startProvider,
			Supplier<ZonedDateTime> endProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super();
		this.navigator = context.getBean(ClubhelperNavigation.class).getNavigator();
		this.context = context;
		this.dataProvider = context.getBean(ClubEventProvider.class);
		this.startProvider = startProvider;
		this.endProvider = endProvider;
		this.printConsumer = printConsumer;
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {

		for (MenuItem item : menuBar.getAllMainMenus()) {
			item.setVisible(false);
		}
		MenuItem fileMenu = menuBar.getFileMenuItem();
		fileMenu.setVisible(true);

		CommandWrapper exportCalendarMonthCommand = new CommandWrapper(
				new ExportCalendarMonthCommand(startProvider, endProvider, dataProvider, printConsumer));
		fileMenu.addItem(exportCalendarMonthCommand.getLabel(), exportCalendarMonthCommand);
		ClubCommand exportCalendarYearCommand = new ExportCalendarYearCommand(startProvider, endProvider, dataProvider,
				printConsumer);
		fileMenu.addItem(exportCalendarYearCommand.getLabel(), new CommandWrapper(exportCalendarYearCommand));
		ClubCommand exportCsvCommand = new ExportCsvCommand(menuBar, context);
		fileMenu.addItem(exportCsvCommand.getLabel(), ev -> exportCsvCommand.execute());

		fileMenu.addSeparator();
		ClubCommand loginCommand = loginOutCommand();
		fileMenu.addItem(loginCommand.getLabel(), new CommandWrapper(loginCommand));

	}

	protected ClubCommand loginOutCommand() {
		return new LoginCommand(navigator);
	}
}
