package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ClubeventPersonId implements Serializable {

	private static final long serialVersionUID = -7964369346971364916L;

	@Column(name = "clubevent_id")
	private String clubEventId;

	@Column(name = "person_id")
	private int personId;

	public String getClubEventId() {
		return clubEventId;
	}

	public void setClubEventId(String clubEventId) {
		this.clubEventId = clubEventId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	@Override
	public String toString() {
		return "ID [clubEventId=" + clubEventId + ", personId=" + personId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clubEventId == null) ? 0 : clubEventId.hashCode());
		result = prime * result + personId;
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
		ClubeventPersonId other = (ClubeventPersonId) obj;
		if (clubEventId == null) {
			if (other.clubEventId != null)
				return false;
		} else if (!clubEventId.equals(other.clubEventId)) {
			return false;
		}
		if (personId != other.personId)
			return false;
		return true;
	}

}