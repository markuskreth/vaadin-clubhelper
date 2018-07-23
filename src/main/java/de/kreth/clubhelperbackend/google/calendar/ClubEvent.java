package de.kreth.clubhelperbackend.google.calendar;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.vaadin.addon.calendar.item.BasicItem;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class ClubEvent extends BasicItem {

	private static final long serialVersionUID = -3600971939167437577L;
	private String location;

	ClubEvent() {
	}

	public String getLocation() {
		return location;
	}

	public static ClubEvent parse(Event ev) {
		ClubEvent clubEvent = new ClubEvent();
		clubEvent.setCaption(ev.getSummary());
		clubEvent.setStart(toZoned(parse(ev.getStart())));

		if (clubEvent.getStart() == null) {
			clubEvent.setStart(toZoned(parse(ev.getOriginalStartTime())));
		}
		clubEvent.setEnd(toZoned(adjustExcludedEndDate(ev)));
		clubEvent.setDescription(ev.getDescription());
		clubEvent.location = ev.getLocation();

		return clubEvent;
	}

	private static ZonedDateTime toZoned(Date parse) {
		if (parse != null) {
			Instant instant = parse.toInstant();
			return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
		} else {
			return null;
		}
	}

	public static Date parse(EventDateTime date) {
		if (date != null) {
			if (date.getDateTime() != null) {
				return new Date(date.getDateTime().getValue());
			} else if (date.getDate() != null) {
				return new Date(date.getDate().getValue());
			}
		}
		return null;
	}

	public static Date adjustExcludedEndDate(
			com.google.api.services.calendar.model.Event e) {
		if (e.isEndTimeUnspecified() == false) {
			EventDateTime end = e.getEnd();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(end.getDate() != null
					? end.getDate().getValue()
					: end.getDateTime().getValue());
			if (startIsDateOnly(e)) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			return calendar.getTime();
		}
		return null;
	}

	public static boolean startIsDateOnly(
			com.google.api.services.calendar.model.Event e) {

		EventDateTime start = e.getStart();
		if (start == null) {
			start = e.getOriginalStartTime();
		}
		return (start.getDate() != null || (start.getDateTime() != null
				&& start.getDateTime().isDateOnly()));
	}

}
