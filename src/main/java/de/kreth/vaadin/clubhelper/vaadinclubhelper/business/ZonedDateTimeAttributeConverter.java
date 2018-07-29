package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter
		implements
			AttributeConverter<ZonedDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(ZonedDateTime attribute) {
		return Date.from(attribute.toInstant());
	}

	@Override
	public ZonedDateTime convertToEntityAttribute(Date dbData) {
		return ZonedDateTime.ofInstant(dbData.toInstant(),
				ZoneId.from(dbData.toInstant()));
	}

}
