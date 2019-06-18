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
 * The persistent class for the adress database table.
 * 
 */
@Entity
@Table(name = "adress")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Adress.findAll", query = "SELECT a FROM Adress a")
@EqualsAndHashCode(callSuper = true)
public @Data class Adress extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8216273166570667412L;

	private String adress1;

	private String adress2;

	private String city;

	private String plz;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	@Override
	public String toString() {
		return "Adress [adress1=" + adress1 + ", adress2=" + adress2 + ", plz=" + plz + ", city=" + city + "]";
	}

}
