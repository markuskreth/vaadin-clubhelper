package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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

public class ExportCalendarYearCommand extends AbstractExportAction {

	private Supplier<ZonedDateTime> startTime;

	public ExportCalendarYearCommand(Supplier<ZonedDateTime> startTime, Supplier<ZonedDateTime> endTime,
			ClubEventProvider dataProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super(() -> startTime.get().withDayOfYear(1), () -> startTime.get().withMonth(12).withDayOfMonth(31),
				dataProvider, printConsumer);
		this.startTime = startTime;
	}

	@Override
	public String getLabel() {
		return "Export Jahr";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.FILE_TABLE;
	}

	@Override
	protected String getTitle() {
		return "Jahr " + startTime.get().getYear();
	}

	@Override
	protected JasperPrint createPrint(Map<LocalDate, StringBuilder> values, List<LocalDate> holidays)
			throws JRException {
		return CalendarCreator.createYearCalendar(startTime.get().getYear(), values, holidays);
	}
}
