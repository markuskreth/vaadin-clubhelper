package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "clubevent_addon")
public class CompetitionType implements Serializable {

	@Id
	private String id;

	@Column(name = "competition_type", nullable = false, length = 45)
	private String type;

	@OneToOne(mappedBy = "competitionType")
	@JoinColumn(name = "id")
	@MapsId
	private ClubEvent clubEvent;

	public Type getType() {
		return Type.valueOf(type);
	}

	public void setType(Type type) {
		this.type = type.name();
	}

	public void setClubEvent(ClubEvent clubEvent) {
		this.clubEvent = clubEvent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClubEvent getClubEvent() {
		return clubEvent;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static enum Type {
		EINZEL,
		SYNCHRON,
		DOPPELMINI,
		MANNSCHAFT,
		LIGA
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CompetitionType other = (CompetitionType) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		}
		else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return type;
	}
}
