package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the contact database table.
 * 
 */
@Entity
@Table(name="contact")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="Contact.findAll", query="SELECT c FROM Contact c")
public class Contact extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7631864028095077913L;

	private String type;

	private String value;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	public Contact() {
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}