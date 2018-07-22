package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the groupDef database table.
 * 
 */
@Entity
@Table(name="groupDef")
@NamedQuery(name="GroupDef.findAll", query="SELECT g FROM GroupDef g")
public class GroupDef implements Serializable {
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

	private String name;

	//bi-directional many-to-one association to Persongroup
	@OneToMany(mappedBy="groupDef")
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

}