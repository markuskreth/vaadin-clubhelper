package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.time.ZonedDateTime;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import net.sf.jasperreports.engine.JasperPrint;

public class ExportCalendarYearCommand extends AbstractExportAction {

	public ExportCalendarYearCommand(Supplier<ZonedDateTime> startTime, Supplier<ZonedDateTime> endTime,
			ClubEventProvider dataProvider, BiConsumer<String, JasperPrint> printConsumer) {
		super(startTime, endTime, dataProvider, printConsumer);
	}

	@Override
	public String getLabel() {
		return "Export Jahr";
	}

	@Override
	public Resource getIcon() {
		return null;
	}

	@Override
	protected boolean getMonthOnly() {
		return false;
	}

}
