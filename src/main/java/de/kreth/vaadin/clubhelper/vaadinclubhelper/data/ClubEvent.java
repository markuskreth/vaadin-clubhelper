package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Transient;

import org.vaadin.addon.calendar.item.BasicItem;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

public class ClubEvent extends BasicItem {

	private static final long serialVersionUID = -3600971939167437577L;
	private String location;
	private String iCalUID;

	private String id;
	private String organizerDisplayName;

	ClubEvent() {
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setiCalUID(String iCalUID) {
		this.iCalUID = iCalUID;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrganizerDisplayName(String organizerDisplayName) {
		this.organizerDisplayName = organizerDisplayName;
	}

	public String getCaption() {
		return super.getCaption();
	}

	@Override
	public String getDescription() {

		return super.getDescription();
	}

	@Override
	public ZonedDateTime getEnd() {
		return super.getEnd();
	}

	@Override
	public ZonedDateTime getStart() {
		return super.getStart();
	}

	@Override
	public boolean isAllDay() {
		return super.isAllDay();
	}

	public String getLocation() {
		return location;
	}

	public String getId() {
		return id;
	}

	public String getiCalUID() {
		return iCalUID;
	}

	public String getOrganizerDisplayName() {
		return organizerDisplayName;
	}

	@Transient
	public String toDisplayString() {
		return "ClubEvent [Caption=" + getCaption() + ", Start=" + getStart()
				+ ", location=" + location + "]";
	}

	@Override
	public String toString() {
		return "ClubEvent [id=" + id + ", getCaption()=" + getCaption() + ", iCalUID=" + iCalUID + ", location="
				+ location + ", organizerDisplayName=" + organizerDisplayName
				+ ", getDescription()="
				+ getDescription() + ", getEnd()=" + getEnd() + ", getStart()="
				+ getStart() + ", isAllDay()=" + isAllDay() + "]";
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
		clubEvent.iCalUID = ev.getICalUID();
		clubEvent.id = ev.getId();
		clubEvent.organizerDisplayName = ev.getOrganizer().getDisplayName();
		clubEvent.setAllDay(startIsDateOnly(ev));

		return clubEvent;
	}

	public static ZonedDateTime toZoned(Date parse) {
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
