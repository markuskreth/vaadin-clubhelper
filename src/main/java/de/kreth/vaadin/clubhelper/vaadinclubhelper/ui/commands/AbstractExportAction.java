package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Notification;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper.CalendarCreator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public abstract class AbstractExportAction implements ClubCommand {

	protected final transient Logger log = LoggerFactory.getLogger(getClass());

	private transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private final Supplier<ZonedDateTime> startTime;

	private final Supplier<ZonedDateTime> endTime;

	private final ClubEventProvider dataProvider;

	private final BiConsumer<String, JasperPrint> printConsumer;

	public AbstractExportAction(Supplier<ZonedDateTime> startTime,
			Supplier<ZonedDateTime> endTime, ClubEventProvider dataProvider,
			BiConsumer<String, JasperPrint> printConsumer) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.dataProvider = dataProvider;
		this.printConsumer = printConsumer;

	}

	@Override
	public void execute() {

		boolean monthOnly = getMonthOnly();
		List<ClubEvent> items;
		ZonedDateTime start;
		ZonedDateTime end;
		if (monthOnly) {
			start = startTime.get();
			end = endTime.get();
			items = dataProvider.getItems(start, end);
		}
		else {
			start = startTime.get().withDayOfYear(1);
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

			printConsumer.accept(calendarMonth, print);
			log.trace("Added pdf window for {}", calendarMonth);
		}
		catch (JRException | RuntimeException e) {
			log.error("Error Creating Jasper Report for {}", calendarMonth, e);
			Notification.show("Fehler bei PDF: " + e);
		}
	}

	protected abstract boolean getMonthOnly();

}
