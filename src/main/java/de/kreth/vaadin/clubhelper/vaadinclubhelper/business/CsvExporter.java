package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import org.apache.commons.csv.CSVFormat;

public class CsvExporter {

	private static final String HEAD_DATUM = "Datum";

	private static final String HEAD_CAPTION = "Datum";

	private static final String HEAD_ORT = "Ort";

	private static final String[] HEAD = { HEAD_DATUM, HEAD_CAPTION, HEAD_ORT };

	private final CSVFormat format = CSVFormat.RFC4180.withDelimiter(',')
			.withHeader(HEAD);
}
