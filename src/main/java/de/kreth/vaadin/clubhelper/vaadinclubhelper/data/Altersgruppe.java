package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "altersgruppe")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Altersgruppe.findAll", query = "SELECT a FROM Altersgruppe a")
public class Altersgruppe extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 126215772910869273L;

	private String bezeichnung;

	private int start;

	private int end;

	@ManyToOne
	private Pflicht pflicht;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private ClubEvent clubEvent;

	@Override
	public String toString() {
		return "Altersgruppe [bezeichnung=" + bezeichnung + ", pflicht=" + pflicht + ", jahre=" + start + " - " + end
				+ "]";
	}

	public boolean isBetween(Temporal startDate) {
		int year = startDate.get(ChronoField.YEAR);
		return year >= start && year <= end;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Pflicht getPflicht() {
		return pflicht;
	}

	public void setPflicht(Pflicht pflicht) {
		this.pflicht = pflicht;
	}

	public ClubEvent getClubEvent() {
		return clubEvent;
	}

	public void setClubEvent(ClubEvent clubEvent) {
		this.clubEvent = clubEvent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + ((clubEvent == null) ? 0 : clubEvent.hashCode());
		result = prime * result + end;
		result = prime * result + ((pflicht == null) ? 0 : pflicht.hashCode());
		result = prime * result + start;
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
		Altersgruppe other = (Altersgruppe) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null) {
				return false;
			}
		}
		else if (!bezeichnung.equals(other.bezeichnung)) {
			return false;
		}
		if (clubEvent == null) {
			if (other.clubEvent != null) {
				return false;
			}
		}
		else if (!clubEvent.equals(other.clubEvent)) {
			return false;
		}
		if (end != other.end) {
			return false;
		}
		if (pflicht == null) {
			if (other.pflicht != null) {
				return false;
			}
		}
		else if (!pflicht.equals(other.pflicht)) {
			return false;
		}
		if (start != other.start) {
			return false;
		}
		return true;
	}

}
