package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the adress database table.
 * 
 */
@Entity
@Table(name="adress")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="Adress.findAll", query="SELECT a FROM Adress a")
public class Adress extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8216273166570667412L;

	private String adress1;

	private String adress2;

	private String city;

	private String plz;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

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

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
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