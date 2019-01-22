package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the persongroup database table.
 * 
 */
@Entity
@Table(name = "persongroup")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Persongroup.findAll", query = "SELECT p FROM Persongroup p")
public class Persongroup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2466223964213904302L;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	// bi-directional many-to-one association to GroupDef
	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupDef groupDef;

	public Persongroup() {
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public GroupDef getGroupDef() {
		return this.groupDef;
	}

	public void setGroupDef(GroupDef groupDef) {
		this.groupDef = groupDef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((groupDef == null) ? 0 : groupDef.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		Persongroup other = (Persongroup) obj;
		if (groupDef == null) {
			if (other.groupDef != null)
				return false;
		} else if (!groupDef.equals(other.groupDef))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

}