package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.time.ZonedDateTime;

public class ClubEventBuilder {

	private final ClubEvent current = new ClubEvent();

	public static ClubEvent createEmpty() {
		return new ClubEvent();
	}

	public static ClubEventBuilder builder() {
		ClubEventBuilder bld = new ClubEventBuilder();
		return bld;
	}

	public ClubEventBuilder withCaption(String caption) {
		current.setCaption(caption);
		return this;
	}

	public ClubEventBuilder withLocation(String location) {
		current.setLocation(location);
		return this;
	}

	public ClubEventBuilder withiCalUID(String iCalUID) {
		current.setiCalUID(iCalUID);
		return this;

	}

	public ClubEventBuilder withId(String id) {
		current.setId(id);
		return this;
	}

	public ClubEventBuilder withOrganizerDisplayName(
			String organizerDisplayName) {
		current.setOrganizerDisplayName(organizerDisplayName);
		return this;
	}

	public ClubEventBuilder withDescription(String description) {
		current.setDescription(description);
		return this;
	}

	public ClubEventBuilder withEnd(ZonedDateTime end) {
		current.setEnd(end);
		return this;
	}

	public ClubEventBuilder withStart(ZonedDateTime start) {
		current.setStart(start);
		return this;
	}

	public ClubEventBuilder withStyleName(String styleName) {
		current.setStyleName(styleName);
		return this;
	}

	public ClubEventBuilder withAllDay(boolean isAllDay) {
		current.setAllDay(isAllDay);
		return this;
	}

	public ClubEvent build() {
		return current;
	}
}
