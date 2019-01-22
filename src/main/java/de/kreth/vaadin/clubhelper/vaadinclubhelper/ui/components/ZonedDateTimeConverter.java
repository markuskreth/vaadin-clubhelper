package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class ZonedDateTimeConverter implements Converter<LocalDate, ZonedDateTime> {

	private static final long serialVersionUID = 7275177272115739588L;

	@Override
	public Result<ZonedDateTime> convertToModel(LocalDate value, ValueContext context) {
		return Result.ok(ZonedDateTime.of(value, LocalTime.MIDNIGHT, ZoneId.systemDefault()));
	}

	@Override
	public LocalDate convertToPresentation(ZonedDateTime value, ValueContext context) {
		return value.toLocalDate();
	}
}
