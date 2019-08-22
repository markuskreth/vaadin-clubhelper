package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.jasper.CalendarCreator;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.ClubEventProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class ExportCalendarMonthCommand extends AbstractExportAction {

	private transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uuuu");

	private Supplier<ZonedDateTime> startTime;

	public ExportCalendarMonthCommand(Supplier<ZonedDateTime> startTime, Supplier<ZonedDateTime> endTime,
			ClubEventProvider dataProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super(startTime, endTime, dataProvider, printConsumer);
		this.startTime = startTime;
	}

	@Override
	public String getLabel() {
		return "Export Monat";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.FILE_TABLE;
	}

	@Override
	protected String getTitle() {
		return dfMonth.format(startTime.get());
	}

	@Override
	protected JasperPrint createPrint(Map<LocalDate, StringBuilder> values, List<LocalDate> holidays)
			throws JRException {
		return CalendarCreator.createCalendar(new Date(startTime.get().toInstant().toEpochMilli()), values, holidays);
	}
}
