package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the adress database table.
 * 
 */
@Entity
@Table(name="adress")
@NamedQuery(name="Adress.findAll", query="SELECT a FROM Adress a")
public class Adress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String adress1;

	private String adress2;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	private String city;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private String plz;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdress1() {
		return this.adress1;
	}

	public void setAdress1(String adress1) {
		this.adress1 = adress1;
	}

	public String getAdress2() {
		return this.adress2;
	}

	public void setAdress2(String adress2) {
		this.adress2 = adress2;
	}

	public Date getChanged() {
		return new Date(this.changed.getTime());
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCreated() {
		return new Date(this.created.getTime());
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return new Date(this.deleted.getTime());
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public String getPlz() {
		return this.plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}