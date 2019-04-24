package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper.CalendarCreator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityGroups;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation.ClubNavigator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class HeadView extends HorizontalLayout {

	private static final long serialVersionUID = -7915475211371903028L;

	protected transient final Logger log = LoggerFactory.getLogger(getClass());

	protected transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private final ClubEventProvider dataProvider;

	private int monthItemId;

	private final Button personLabel;

	protected final Label monthName;

	private final Function<Component, ZonedDateTime> startTime;

	private final Function<Component, ZonedDateTime> endTime;

	private final ClubNavigator navigator;

	private final SecurityVerifier securityVerifier;

	public HeadView(ClubNavigator navigator, Function<Component, ZonedDateTime> startTime,
			Function<Component, ZonedDateTime> endTime, ClubEventProvider dataProvider,
			SecurityVerifier securityVerifier) {

		this.navigator = navigator;
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
			MenuItem menuItem = contextMenu.addItem("Export Monat", ev1 -> calendarExport(button, ev1));
			monthItemId = menuItem.getId();
			contextMenu.addItem("Export Jahr", ev1 -> calendarExport(button, ev1));
			if (securityVerifier.isLoggedin()
					&& securityVerifier.isPermitted(SecurityGroups.ADMIN, SecurityGroups.UEBUNGSLEITER)) {
				contextMenu.addItem("Personen verwalten",
						ev1 -> navigator.navigateTo(ClubhelperViews.PersonEditView.name()));
			}
			contextMenu.open(50, 50);
			break;
		case "head.user":
			if (securityVerifier.getLoggedinPerson() != null) {

				contextMenu.addItem("Abmelden", ev1 -> {
					securityVerifier.setLoggedinPerson(null);
					navigator.navigateTo(ClubhelperViews.MainView.name());
				});
			}
			else {
				contextMenu.addItem("Anmelden", ev1 -> navigator.navigateTo(ClubhelperViews.LoginUI.name()));
			}
			int width = getUI().getPage().getBrowserWindowWidth();

			contextMenu.open(width - 150, 50);
			break;
		default:
			break;
		}

	}

	private void calendarExport(Button source, MenuItem ev1) {

		boolean monthOnly = ev1.getId() == monthItemId;
		List<ClubEvent> items;
		ZonedDateTime start;
		ZonedDateTime end;
		if (monthOnly) {
			start = startTime.apply(source);
			end = endTime.apply(source);
			items = dataProvider.getItems(start, end);
		}
		else {
			start = startTime.apply(source).withDayOfYear(1);
			end = start.withMonth(12).withDayOfMonth(31);
			items = dataProvider.getItems(start, end);
		}

		Map<LocalDate, StringBuilder> values = new HashMap<>();
		List<LocalDate> holidays = CalendarCreator.filterHolidays(items);

		log.debug("exporting Calendar from {} to {}, itemCount = {}", start, end, items.size());

		for (ClubEvent ev : items) {

			ZonedDateTime evStart = ev.getStart();
			ZonedDateTime evEnd = ev.getEnd();

			log.trace("Added to eventsd: {}", ev);

			CalendarCreator.iterateDays(evStart.toLocalDate(), evEnd.toLocalDate(), day -> {

				StringBuilder content;
				if (values.containsKey(day)) {
					content = values.get(day);
					content.append("\n");
				}
				else {
					content = new StringBuilder();
					values.put(day, content);
				}
				content.append(ev.getCaption());
				if (ev.getLocation() != null && ev.getLocation().isBlank() == false) {
					content.append(" (").append(ev.getLocation()).append(")");
				}
			});
		}

		String calendarMonth;
		if (monthOnly) {
			calendarMonth = dfMonth.format(start);
		}
		else {
			calendarMonth = "Jahr " + start.getYear();
		}

		try {
			JasperPrint print;
			if (monthOnly) {
				print = CalendarCreator.createCalendar(new Date(start.toInstant().toEpochMilli()), values, holidays);
			}
			else {
				print = CalendarCreator.createYearCalendar(start.getYear(), values, holidays);
			}
			log.trace("Created Jasper print for {}", calendarMonth);
			Window window = new Window();
			window.setCaption("View PDF");
			AbstractComponent e = createEmbedded(calendarMonth, print);
			window.setContent(e);
			window.setModal(true);
			window.setWidth("50%");
			window.setHeight("90%");
			personLabel.getUI().addWindow(window);
			log.trace("Added pdf window for {}", calendarMonth);
		}
		catch (JRException | IOException | RuntimeException e) {
			log.error("Error Creating Jasper Report for {}", calendarMonth, e);
			Notification.show("Fehler bei PDF: " + e);
		}
	}

	private AbstractComponent createEmbedded(String title, JasperPrint print) throws IOException {

		final PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream(in);

		final StreamResource resource = new StreamResource(() -> in, title);
		resource.setMIMEType("application/pdf");

		BrowserFrame c = new BrowserFrame("PDF invoice", resource);
		c.setSizeFull();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		try {
			exec.execute(() -> {
				try {
					JasperExportManager.exportReportToPdfStream(print, out);
				}
				catch (JRException e) {
					throw new RuntimeException("Error on Export to Pdf.", e);
				}
				finally {
				}
			});
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
		exec.shutdown();
		return c;
	}

}
