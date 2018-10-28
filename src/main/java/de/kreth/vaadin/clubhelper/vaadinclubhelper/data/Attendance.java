package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the attendance database table.
 * 
 */
@Entity
@Table(name="attendance")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="Attendance.findAll", query="SELECT a FROM Attendance a")
public class Attendance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2385033161272078335L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="on_date")
	private Date onDate;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	public Date getOnDate() {
		return new Date(this.onDate.getTime());
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}