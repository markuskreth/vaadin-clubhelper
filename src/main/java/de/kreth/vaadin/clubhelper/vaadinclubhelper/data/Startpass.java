package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the startpaesse database table.
 * 
 */
@Entity
@Table(name = "startpaesse")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Startpass.findAll", query = "SELECT s FROM Startpass s")
public class Startpass extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1464510869007022357L;

	@Column(name = "startpass_nr")
	private String startpassNr;

	@OneToOne
	@JoinColumn(name = "person_id")
	private Person person;

	// bi-directional many-to-one association to StartpassStartrechte
	@OneToMany(mappedBy = "startpaesse")
	private List<StartpassStartrechte> startpassStartrechtes;

	public Startpass() {
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

	@Override
	public String toString() {
		return "Startpass [startpassNr=" + startpassNr + "]";
	}

}