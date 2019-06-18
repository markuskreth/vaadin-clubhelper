package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the contact database table.
 * 
 */
@Entity
@Table(name = "contact")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c WHERE c.deleted is not null")
@EqualsAndHashCode(callSuper = true)
public @Data class Contact extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7631864028095077913L;

	private String type;

	private String value;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	@Override
	public String toString() {
		return "Contact [type=" + type + ", value=" + value + "]";
	}

}
