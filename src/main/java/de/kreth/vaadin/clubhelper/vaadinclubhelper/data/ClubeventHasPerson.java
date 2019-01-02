package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "clubevent_has_person")
public class ClubeventHasPerson {

	@EmbeddedId
	private ClubeventPersonId clubeventPersonId = new ClubeventPersonId();
	private String comment;

	public String getClubEventId() {
		return clubeventPersonId.getClubEventId();
	}

	public void setClubEventId(String clubEventId) {
		this.clubeventPersonId.setClubEventId(clubEventId);
	}

	public int getPersonId() {
		return clubeventPersonId.getPersonId();
	}

	public void setPersonId(int personId) {
		this.clubeventPersonId.setPersonId(personId);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ClubeventHasPerson [" + clubeventPersonId + ", comment=" + comment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clubeventPersonId == null) ? 0 : clubeventPersonId.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
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
		ClubeventHasPerson other = (ClubeventHasPerson) obj;
		if (clubeventPersonId == null) {
			if (other.clubeventPersonId != null)
				return false;
		} else if (!clubeventPersonId.equals(other.clubeventPersonId))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		return true;
	}

}
