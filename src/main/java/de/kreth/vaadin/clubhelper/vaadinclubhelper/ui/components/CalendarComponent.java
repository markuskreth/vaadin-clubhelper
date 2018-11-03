package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.BackwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ForwardEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class CalendarComponent extends CustomComponent {

	private static final long serialVersionUID = -9152173211931554059L;
	private transient final Logger log = LoggerFactory.getLogger(getClass()); 

	private transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private Label monthName;

	private ClubEventProvider dataProvider;
	private Calendar<ClubEvent> calendar;

	public CalendarComponent() {

		monthName = new Label();
		monthName.setStyleName("title_label");

		Button popupButton = new Button("Menu");
		popupButton.addClickListener(ev -> openPopupMenu(ev));
		
		HorizontalLayout head = new HorizontalLayout(monthName, popupButton);
		
		dataProvider = new ClubEventProvider();
		calendar = new Calendar<>(dataProvider)
				.withMonth(Month.from(LocalDateTime.now()));
		calendar.setCaption("Events");
		calendar.setSizeFull();
		calendar.addListener(ev -> calendarEvent(ev));

		updateMonthText(calendar.getStartDate());

		VerticalLayout layout = new VerticalLayout(head, calendar);
		layout.setSizeFull();
		setCompositionRoot(layout);
	}

	private void calendarEvent(Event ev) {
		log.debug("Event on calendar: {}", ev);
		if (ev instanceof BackwardEvent || ev instanceof ForwardEvent) {
			updateMonthText(calendar.getStartDate());
		}
	}

	public Registration setHandler(ItemClickHandler listener) {
		return calendar.setHandler(listener);
	}

	private void openPopupMenu(ClickEvent ev) {
		ContextMenu contextMenu = new ContextMenu(ev.getButton(), true);
		contextMenu.addItem("Export", ev1 -> calendarExport(ev1));
		contextMenu.open(210, 40);
	}

	private void calendarExport(MenuItem ev1) {

		ZonedDateTime start = calendar.getStartDate();
		ZonedDateTime end = calendar.getEndDate();
		log.debug("exporting Calendar from {} to {}", start, end);
		List<ClubEvent> items = dataProvider.getItems(start, end);
		Map<Integer, StringBuilder> values = new HashMap<>();
		Set<Integer> holidays = new HashSet<>();
		
		for (ClubEvent ev : items) {

			String calendarName = ev.getOrganizerDisplayName();
			if ("Schulferien".equals(calendarName)) {
				int endDayOfMonth = ev.getEnd().getDayOfMonth();
				for (int dayOfMonth = ev.getStart().getDayOfMonth(); dayOfMonth<=endDayOfMonth; dayOfMonth++) {
					holidays.add(dayOfMonth);
				}
			} else {
				StringBuilder content;

				int dayOfMonth = ev.getStart().getDayOfMonth();
				int endDayOfMonth = ev.getEnd().getDayOfMonth();
				for (;dayOfMonth<=endDayOfMonth; dayOfMonth++) {

					if (values.containsKey(dayOfMonth)) {
						content = values.get(dayOfMonth);
						content.append("\n");
					} else {
						content = new StringBuilder();
						values.put(dayOfMonth, content);
					}
					content.append(ev.getCaption());
				}
			}
		}
		
		try {
			JasperPrint print = CalendarCreator.createCalendar(new Date(start.toInstant().toEpochMilli()), values, holidays);
		    Window window = new Window();
		    window.setCaption("View PDF");
		    AbstractComponent e = createEmbedded(dfMonth.format(start), print);
		    window.setContent(e);
		    window.setModal(true);
		    window.setWidth("50%");
		    window.setHeight("90%");
		    monthName.getUI().addWindow(window);
		} catch (JRException e) {
			log.error("Error Creating Jasper Report.", e);
			Notification.show("Fehler bei PDF: " + e);
		} catch (IOException e1) {
			log.error("Error Creating Jasper Report.", e1);
			Notification.show("Fehler bei PDF: " + e1);
		}
	}

	private AbstractComponent createEmbedded(String title, JasperPrint print) throws IOException, JRException {

		PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream(in);

		final StreamResource resource = new StreamResource(() -> in, title);
	    resource.setMIMEType("application/pdf");
	    
		BrowserFrame c = new BrowserFrame("PDF invoice", resource);
	    c.setSizeFull();
	    
	    ExecutorService exec = Executors.newSingleThreadExecutor();
	    exec.execute(() -> {
	    	try {
				JasperExportManager.exportReportToPdfStream(print, out);
			} catch (JRException e) {
				log.error("Error on Export to Pdf.", e);
				throw new RuntimeException(e);
			}
	    });
	    exec.shutdown();
	    return c;
	}

	private void updateMonthText(ZonedDateTime startDate) {
		String monthValue = dfMonth.format(startDate);
		log.debug("Changed Month title to {}", monthValue);
		monthName.setValue(monthValue);
	}

	public void setItems(Collection<ClubEvent> items) {
		dataProvider.setItems(items);
		calendar.markAsDirty();
	}

	static class ClubEventProvider extends BasicItemProvider<ClubEvent> {

		private static final long serialVersionUID = -5415397258827236704L;

		@Override
		public void setItems(Collection<ClubEvent> items) {
			super.setItems(items);
			fireItemSetChanged();
		}

	}

}
