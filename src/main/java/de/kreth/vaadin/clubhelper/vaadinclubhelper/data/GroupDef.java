package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
	private List<Person> persongroups;

	public GroupDef() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getPersongroups() {
		return this.persongroups;
	}

	public void setPersongroups(List<Person> persongroups) {
		this.persongroups = persongroups;
	}

	public void addPersongroup(Person persongroup) {
		persongroups.add(persongroup);
	}

	public void removePersongroup(Person persongroup) {
		getPersongroups().remove(persongroup);
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GroupDef other = (GroupDef) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}