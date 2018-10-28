package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the groupDef database table.
 * 
 */
@Entity(name = "groupDef")
@Table(name = "groupDef")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = GroupDef.QUERY_FINDALL, query = "SELECT g FROM groupDef g")
public class GroupDef extends BaseEntity implements Serializable {

	public final static String QUERY_FINDALL = "GroupDef.findAll";

	private static final long serialVersionUID = -2827542956463449518L;

	private String name;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable (name = "persongroup",
	        joinColumns = { @JoinColumn(name = "group_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "person_id") })
	private List<Persongroup> persongroups;

	public GroupDef() {
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
		return "GroupDef [id=" + getId() + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((persongroups == null) ? 0 : persongroups.hashCode());
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
		GroupDef other = (GroupDef) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (persongroups == null) {
			if (other.persongroups != null)
				return false;
		} else if (!persongroups.equals(other.persongroups))
			return false;
		return true;
	}

}