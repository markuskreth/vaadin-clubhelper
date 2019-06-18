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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the groupDef database table.
 * 
 */
@Entity
@Table(name = "groupdef")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = GroupDef.QUERY_FINDALL, query = "SELECT g FROM GroupDef g")
@EqualsAndHashCode(callSuper = true)
public @Data class GroupDef extends BaseEntity implements Serializable {

	public final static String QUERY_FINDALL = "GroupDef.findAll";

	private static final long serialVersionUID = -2827542956463449518L;

	private String name;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
	private List<Person> persongroups;

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

}
