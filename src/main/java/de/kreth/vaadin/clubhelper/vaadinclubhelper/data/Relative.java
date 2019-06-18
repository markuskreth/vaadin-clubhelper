package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the relative database table.
 * 
 */
@Entity
@Table(name = "relative")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = Relative.QUERY_FINDALL, query = "SELECT r FROM Relative r")
@EqualsAndHashCode(callSuper = false)
public @Data class Relative extends BaseEntity implements Serializable {

	public static final String QUERY_FINDALL = "Relative.findAll";

	private static final long serialVersionUID = -1331008393583211773L;

	@Column(name = "TO_PERSON1_RELATION")
	private String toPerson1Relation;

	@Column(name = "TO_PERSON2_RELATION")
	private String toPerson2Relation;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "person1")
	private Person person1Bean;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "person2")
	private Person person2Bean;

	public Relation getRelationTo(Person person) {
		if (person == null) {
			return null;
		}
		if (person.equals(person1Bean)) {
			return new Relation(person2Bean, toPerson2Relation);
		}
		else if (person.equals(person2Bean)) {
			return new Relation(person1Bean, toPerson1Relation);
		}
		else {
			return null;
		}
	}

}
