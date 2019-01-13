package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the startpaesse database table.
 * 
 */
@Entity
@Table(name="startpaesse")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="Startpaesse.findAll", query="SELECT s FROM Startpaesse s")
public class Startpaesse extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1464510869007022357L;

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