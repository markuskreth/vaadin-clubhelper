package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class CsvExporter {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	private static final String HEAD_DATUM = "Datum";

	private static final String HEAD_CAPTION = "Bezeichnung";

	private static final String HEAD_ORT = "Ort";

	private static final String HEAD_TEILNEHMER = "Teilnehmer";

	private static final String[] HEAD = { HEAD_DATUM, HEAD_CAPTION, HEAD_ORT, HEAD_TEILNEHMER };

	private final CSVFormat format = CSVFormat.RFC4180.withDelimiter(',')
			.withHeader(HEAD);

	public void export(List<ClubEvent> items, Writer out) throws IOException {
		CSVPrinter printer = format.print(out);
		printer.printRecords(Arrays.asList(HEAD));

		for (ClubEvent event : items) {
			String start = FORMATTER.format(event.getStart());
			String end = FORMATTER.format(event.getEnd());
			if (start.equals(end) == false) {
				start = start + "-" + end;
			}

			StringBuilder competitors = new StringBuilder();

			for (Person p : event.getPersons()) {
				if (competitors.length() > 0) {
					competitors.append(",");
				}
				competitors.append(p.getPrename()).append(" ").append(p.getSurname());
			}
			printer.printRecord(start, event.getCaption(), event.getLocation(), competitors.toString());
		}
	}
}
