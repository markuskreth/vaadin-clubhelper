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

// Entity must not be used, this class is persisted by ClubEvent.hbm.xml
@Entity
/**
 * Calendar Event item corresponding to google calendar events.
 * @author markus
 *
 */
public class ClubEvent extends BasicItem implements EntityAccessor {

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

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getiCalUID() {
		return iCalUID;
	}

	public void setiCalUID(String iCalUID) {
		this.iCalUID = iCalUID;
	}

	public String getOrganizerDisplayName() {
		return organizerDisplayName;
	}

	public void setOrganizerDisplayName(String organizerDisplayName) {
		this.organizerDisplayName = organizerDisplayName;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	public Set<Altersgruppe> getAltersgruppen() {
		return altersgruppen;
	}

	public void setAltersgruppen(Set<Altersgruppe> altersgruppen) {
		this.altersgruppen = altersgruppen;
	}

	public CompetitionType getCompetitionType() {
		return competitionType;
	}

	public void setCompetitionType(CompetitionType competitionType) {
		this.competitionType = competitionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((competitionType == null) ? 0 : competitionType.hashCode());
		result = prime * result + ((iCalUID == null) ? 0 : iCalUID.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClubEvent other = (ClubEvent) obj;
		if (competitionType == null) {
			if (other.competitionType != null) {
				return false;
			}
		}
		else if (!competitionType.equals(other.competitionType)) {
			return false;
		}
		if (iCalUID == null) {
			if (other.iCalUID != null) {
				return false;
			}
		}
		else if (!iCalUID.equals(other.iCalUID)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		}
		else if (!location.equals(other.location)) {
			return false;
		}
		return true;
	}

}
