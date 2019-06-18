package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.vaadin.addon.calendar.item.BasicItem;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Entity must not be used, this class is persisted by ClubEvent.hbm.xml
@Entity
/**
 * Calendar Event item corresponding to google calendar events.
 * @author markus
 *
 */
@EqualsAndHashCode(callSuper = true)
public @Data class ClubEvent extends BasicItem implements EntityAccessor {

	private static final long serialVersionUID = -3600971939167437577L;

	@Id
	private String id;

	private String location;

	private String iCalUID;

	private String organizerDisplayName;

	@ManyToMany
	private Set<Person> persons;

	@OneToMany
	private Set<Altersgruppe> altersgruppen;

	@OneToOne
	@JoinColumn(name = "id", nullable = true)
	private CompetitionType competitionType;

	@Override
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

	public Type getType() {
		if (competitionType != null) {
			return competitionType.getType();
		}
		else {
			return null;
		}
	}

	public void setType(Type competitionType) {
		if (this.competitionType == null) {
			this.competitionType = new CompetitionType();
			this.competitionType.setClubEvent(this);
		}
		this.competitionType.setType(competitionType);
	}

	@Override
	public String getStyleName() {
		return organizerDisplayName;
	}

	public void add(Person p) {
		if (this.persons == null) {
			this.persons = new HashSet<>();
		}
		if (this.persons.contains(p)) {
			return;
		}
		this.persons.add(p);
		p.add(this);
	}

	public void remove(Person person) {
		persons.remove(person);
		person.remove(this);
	}

	@Transient
	public String toDisplayString() {
		return "ClubEvent [Caption=" + getCaption() + ", Start=" + getStart() + ", location=" + location + "]";
	}

	@Override
	public String toString() {
		return "ClubEvent [id=" + id + ", getCaption()=" + getCaption() + ", iCalUID=" + iCalUID + ", location="
				+ location + ", organizerDisplayName=" + organizerDisplayName + ", getDescription()=" + getDescription()
				+ ", getEnd()=" + getEnd() + ", getStart()=" + getStart() + ", isAllDay()=" + isAllDay() + "]";
	}

	public static ZonedDateTime toZoned(Date parse) {
		if (parse != null) {
			Instant instant = parse.toInstant();
			return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		else {
			return null;
		}
	}

	@Override
	public boolean hasValidId() {
		return id != null && !id.isBlank();
	}

	@Override
	public void setChanged(Date changed) {
		// no ChangeDate stored
	}

	@Override
	public void setCreated(Date created) {
		// noCreateDateStored
	}

}
