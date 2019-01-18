package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.vaadin.addon.calendar.item.BasicItem;

// Entity must not be used, this class is persisted by ClubEvent.hbm.xml
@Entity
public class ClubEvent extends BasicItem implements EntityAccessor {

	private static final long serialVersionUID = -3600971939167437577L;

	@Id
	private String id;
	private String location;
	private String iCalUID;

	private String organizerDisplayName;
	@ManyToMany
	private Set<Person> persons;
	private Set<Altersgruppe> altersgruppen;

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

	@Override
	public String getStyleName() {
		return organizerDisplayName;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
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

	public Set<Altersgruppe> getAltersgruppen() {
		return altersgruppen;
	}

	public void setAltersgruppen(Set<Altersgruppe> altersgruppen) {
		this.altersgruppen = altersgruppen;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((iCalUID == null) ? 0 : iCalUID.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((organizerDisplayName == null) ? 0 : organizerDisplayName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClubEvent other = (ClubEvent) obj;
		if (iCalUID == null) {
			if (other.iCalUID != null)
				return false;
		} else if (!iCalUID.equals(other.iCalUID)) {
			return false;
		}
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (organizerDisplayName == null) {
			if (other.organizerDisplayName != null)
				return false;
		} else if (!organizerDisplayName.equals(other.organizerDisplayName)) {
			return false;
		}
		return true;
	}

	public static ZonedDateTime toZoned(Date parse) {
		if (parse != null) {
			Instant instant = parse.toInstant();
			return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
		} else {
			return null;
		}
	}

	@Override
	public boolean hasValidId() {
		return id != null && !id.isBlank();
	}

}
