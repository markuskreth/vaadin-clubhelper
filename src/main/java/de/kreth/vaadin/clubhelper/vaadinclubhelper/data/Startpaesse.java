package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the startpaesse database table.
 * 
 */
@Entity
@Table(name="startpaesse")
@NamedQuery(name="Startpaesse.findAll", query="SELECT s FROM Startpaesse s")
public class Startpaesse implements Serializable {
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

	@Column(name="startpass_nr")
	private String startpassNr;

	//bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	//bi-directional many-to-one association to StartpassStartrechte
	@OneToMany(mappedBy="startpaesse")
	private List<StartpassStartrechte> startpassStartrechtes;

	public Startpaesse() {
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

	public String getStartpassNr() {
		return this.startpassNr;
	}

	public void setStartpassNr(String startpassNr) {
		this.startpassNr = startpassNr;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<StartpassStartrechte> getStartpassStartrechtes() {
		return this.startpassStartrechtes;
	}

	public void setStartpassStartrechtes(List<StartpassStartrechte> startpassStartrechtes) {
		this.startpassStartrechtes = startpassStartrechtes;
	}

	public StartpassStartrechte addStartpassStartrechte(StartpassStartrechte startpassStartrechte) {
		getStartpassStartrechtes().add(startpassStartrechte);
		startpassStartrechte.setStartpaesse(this);

		return startpassStartrechte;
	}

	public StartpassStartrechte removeStartpassStartrechte(StartpassStartrechte startpassStartrechte) {
		getStartpassStartrechtes().remove(startpassStartrechte);
		startpassStartrechte.setStartpaesse(null);

		return startpassStartrechte;
	}

}