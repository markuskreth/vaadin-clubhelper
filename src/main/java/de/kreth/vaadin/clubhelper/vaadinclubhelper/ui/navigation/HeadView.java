package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityGroups;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ClubCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarMonthCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCalendarYearCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.ExportCsvCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LoginCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.LogoutCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.OpenPersonEditorCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class HeadView extends HorizontalLayout {

	protected transient final Logger log = LoggerFactory.getLogger(getClass());

	protected transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private final ClubEventProvider dataProvider;

	private final Button personLabel;

	protected final Label monthName;

	private final Function<Component, ZonedDateTime> startTime;

	private final Function<Component, ZonedDateTime> endTime;

	private final ClubNavigator navigator;

	private final SecurityVerifier securityVerifier;

	private ApplicationContext context;

	public HeadView(ApplicationContext context, ClubNavigator navigator2,
			Function<Component, ZonedDateTime> startTime,
			Function<Component, ZonedDateTime> endTime, ClubEventProvider dataProvider,
			SecurityVerifier securityVerifier) {

		this.context = context;
		this.navigator = navigator2;
		this.securityVerifier = securityVerifier;
		this.startTime = startTime;
		this.endTime = endTime;

		Button popupButton = new Button(VaadinIcons.MENU);
		popupButton.setId("head.menu");
		popupButton.addClickListener(ev -> openPopupMenu(ev));
		popupButton.setWidth(null);

		monthName = new Label();
		monthName.setId("calendar.month");
		monthName.setStyleName("title_caption");
		monthName.setWidth("");

		personLabel = new Button(VaadinIcons.USER);
		personLabel.setId("head.user");
		personLabel.addClickListener(this::openPopupMenu);

		addComponent(popupButton);
		addComponent(monthName);
		addComponent(personLabel);

		setComponentAlignment(popupButton, Alignment.MIDDLE_LEFT);
		setComponentAlignment(monthName, Alignment.MIDDLE_CENTER);
		setComponentAlignment(personLabel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(monthName, 1.0f);

		this.dataProvider = dataProvider;
	}

	public void updateLoggedinPerson() {

		Person loggedinPerson = securityVerifier.getLoggedinPerson();
		if (loggedinPerson != null) {
			personLabel.setCaption(loggedinPerson.getSurname() + ", " + loggedinPerson.getPrename());
		}
		else {
			personLabel.setCaption("");
		}
	}

	private void openPopupMenu(ClickEvent ev) {
		Button button = ev.getButton();

		ContextMenu contextMenu = new ContextMenu(button, true);

		switch (button.getId()) {
		case "head.menu":

			Supplier<ZonedDateTime> start = () -> startTime.apply(button);
			Supplier<ZonedDateTime> end = () -> endTime.apply(button);

			ClubCommand exportCalendarMonthCommand = new ExportCalendarMonthCommand(start, end,
					dataProvider, this::showPrint);
			contextMenu.addItem(exportCalendarMonthCommand.getLabel(),
					ev1 -> exportCalendarMonthCommand.execute());

			ClubCommand exportCalendarYearCommand = new ExportCalendarYearCommand(start, end,
					dataProvider, this::showPrint);
			contextMenu.addItem(exportCalendarYearCommand.getLabel(), ev1 -> exportCalendarYearCommand.execute());

			ClubCommand exportCsvCommand = new ExportCsvCommand(button, context);
			contextMenu.addItem(exportCsvCommand.getLabel(), ev1 -> exportCsvCommand.execute());

			if (securityVerifier.isLoggedin()
					&& securityVerifier.isPermitted(SecurityGroups.ADMIN, SecurityGroups.UEBUNGSLEITER)) {

				ClubCommand openPersonEditor = new OpenPersonEditorCommand(navigator);
				contextMenu.addItem(openPersonEditor.getLabel(),
						ev1 -> openPersonEditor.execute());
			}
			contextMenu.open(50, 50);
			break;
		case "head.user":
			if (securityVerifier.isLoggedin()) {

				LogoutCommand logoutCommand = new LogoutCommand(navigator, securityVerifier);
				contextMenu.addItem(logoutCommand.getLabel(), ev1 -> {
					logoutCommand.execute();
				});
			}
			else {
				LoginCommand loginCommand = new LoginCommand(navigator);
				contextMenu.addItem(loginCommand.getLabel(), ev1 -> {
					loginCommand.execute();
				});
			}
			int width = getUI().getPage().getBrowserWindowWidth();

			contextMenu.open(width - 150, 50);
			break;
		default:
			break;
		}

	}

	private void showPrint(String calendarMonth, JasperPrint print) {
		Window window = new Window();
		window.setCaption("View PDF");
		AbstractComponent e;
		try {
			e = createEmbedded(calendarMonth, print);
		}
		catch (Exception e1) {

			return;
		}
		window.setContent(e);
		window.setModal(true);
		window.setWidth("50%");
		window.setHeight("90%");
		personLabel.getUI().addWindow(window);
	}

	private AbstractComponent createEmbedded(String title, JasperPrint print) throws IOException, JRException {

		final PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream(in);

		final StreamResource resource = new StreamResource(() -> in, title);
		resource.setMIMEType("application/pdf");

		BrowserFrame c = new BrowserFrame("PDF invoice", resource);

		c.setSizeFull();

		try {
			JasperExportManager.exportReportToPdfStream(print, out);
		}
		finally {
			try {
				out.close();
				in.close();
			}
			catch (IOException e) {
				log.warn("Error closing Jasper output stream.", e);
			}

		}
		return c;
	}

}
