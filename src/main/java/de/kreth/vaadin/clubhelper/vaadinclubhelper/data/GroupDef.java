package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the groupDef database table.
 * 
 */
@Entity(name = "groupDef")
@Table(name = "groupDef")
@NamedQuery(name = GroupDef.QUERY_FINDALL, query = "SELECT g FROM groupDef g")
public class GroupDef implements Serializable {

	public final static String QUERY_FINDALL = "GroupDef.findAll";

	private static final long serialVersionUID = -2827542956463449518L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private String name;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable (name = "persongroup",
	        joinColumns = { @JoinColumn(name = "group_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "person_id") })
	private List<Persongroup> persongroups;

	public GroupDef() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getChanged() {
		return this.changed;
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Persongroup> getPersongroups() {
		return this.persongroups;
	}

	public void setPersongroups(List<Persongroup> persongroups) {
		this.persongroups = persongroups;
	}

	public Persongroup addPersongroup(Persongroup persongroup) {
		getPersongroups().add(persongroup);
		persongroup.setGroupDef(this);

		return persongroup;
	}

	public Persongroup removePersongroup(Persongroup persongroup) {
		getPersongroups().remove(persongroup);
		persongroup.setGroupDef(null);

		return persongroup;
	}

	@Override
	public String toString() {
		return "GroupDef [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		GroupDef other = (GroupDef) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}