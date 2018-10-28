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


/**
 * The persistent class for the relative database table.
 * 
 */
@Entity
@Table(name="relative")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="Relative.findAll", query="SELECT r FROM Relative r")
public class Relative extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1331008393583211773L;

	@Column(name="TO_PERSON1_RELATION")
	private String toPerson1Relation;

	@Column(name="TO_PERSON2_RELATION")
	private String toPerson2Relation;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person1")
	private Person person1Bean;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person2")
	private Person person2Bean;

	public Relative() {
	}

	public String getToPerson1Relation() {
		return this.toPerson1Relation;
	}

	public void setToPerson1Relation(String toPerson1Relation) {
		this.toPerson1Relation = toPerson1Relation;
	}

	public String getToPerson2Relation() {
		return this.toPerson2Relation;
	}

	public void setToPerson2Relation(String toPerson2Relation) {
		this.toPerson2Relation = toPerson2Relation;
	}

	public Person getPerson1Bean() {
		return this.person1Bean;
	}

	public void setPerson1Bean(Person person1Bean) {
		this.person1Bean = person1Bean;
	}

	public Person getPerson2Bean() {
		return this.person2Bean;
	}

	public void setPerson2Bean(Person person2Bean) {
		this.person2Bean = person2Bean;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((person1Bean == null) ? 0 : person1Bean.hashCode());
		result = prime * result + ((person2Bean == null) ? 0 : person2Bean.hashCode());
		result = prime * result + ((toPerson1Relation == null) ? 0 : toPerson1Relation.hashCode());
		result = prime * result + ((toPerson2Relation == null) ? 0 : toPerson2Relation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relative other = (Relative) obj;
		if (person1Bean == null) {
			if (other.person1Bean != null)
				return false;
		} else if (!person1Bean.equals(other.person1Bean))
			return false;
		if (person2Bean == null) {
			if (other.person2Bean != null)
				return false;
		} else if (!person2Bean.equals(other.person2Bean))
			return false;
		if (toPerson1Relation == null) {
			if (other.toPerson1Relation != null)
				return false;
		} else if (!toPerson1Relation.equals(other.toPerson1Relation))
			return false;
		if (toPerson2Relation == null) {
			if (other.toPerson2Relation != null)
				return false;
		} else if (!toPerson2Relation.equals(other.toPerson2Relation))
			return false;
		return true;
	}

}