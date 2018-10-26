package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the persongroup database table.
 * 
 */
@Entity
@Table(name="persongroup")
@NamedQuery(name="Persongroup.findAll", query="SELECT p FROM Persongroup p")
public class Persongroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	//bi-directional many-to-one association to GroupDef
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupDef groupDef;

	public Persongroup() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getChanged() {
		return new Date(this.changed.getTime());
	}

	public void setChanged(Date changed) {
		this.changed = changed;
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
		int result = 1;
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((groupDef == null) ? 0 : groupDef.hashCode());
		result = prime * result + id;
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persongroup other = (Persongroup) obj;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (groupDef == null) {
			if (other.groupDef != null)
				return false;
		} else if (!groupDef.equals(other.groupDef))
			return false;
		if (id != other.id)
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

}