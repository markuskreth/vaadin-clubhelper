package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

public class ClubeventHasPerson {

	private String clubEventId;
	private int personId;
	private String comment;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ClubeventHasPerson [clubEventId=" + clubEventId + ", personId=" + personId + "]";
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
		ClubeventHasPerson other = (ClubeventHasPerson) obj;
		if (clubEventId == null) {
			if (other.clubEventId != null)
				return false;
		} else if (!clubEventId.equals(other.clubEventId))
			return false;
		if (personId != other.personId)
			return false;
		return true;
	}

}
