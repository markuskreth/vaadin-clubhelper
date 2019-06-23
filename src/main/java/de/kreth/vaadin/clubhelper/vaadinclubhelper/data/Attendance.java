package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the attendance database table.
 * 
 */
@Entity
@Table(name = "attendance")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Attendance.findAll", query = "SELECT a FROM Attendance a")
public class Attendance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2385033161272078335L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "on_date")
	private Date onDate;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	public Date getOnDate() {
		return new Date(this.onDate.getTime());
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((onDate == null) ? 0 : onDate.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		Attendance other = (Attendance) obj;
		if (onDate == null) {
			if (other.onDate != null) {
				return false;
			}
		}
		else if (!onDate.equals(other.onDate)) {
			return false;
		}
		if (person == null) {
			if (other.person != null) {
				return false;
			}
		}
		else if (!person.equals(other.person)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Attendance [person=" + person + ", onDate=" + onDate + "]";
	}

}
