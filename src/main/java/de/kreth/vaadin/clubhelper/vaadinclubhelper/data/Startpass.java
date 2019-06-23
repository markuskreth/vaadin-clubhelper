package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.ArrayList;
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

	@Column(name = "startpass_nr")
	private String startpassNr;

	@OneToOne
	@JoinColumn(name = "person_id")
	private Person person;

	// bi-directional many-to-one association to StartpassStartrechte
	@OneToMany(mappedBy = "startpaesse")
	private List<StartpassStartrechte> startpassStartrechte;

	public StartpassStartrechte addStartpassStartrechte(StartpassStartrechte startpassStartrechte) {
		if (this.startpassStartrechte == null) {
			this.startpassStartrechte = new ArrayList<>();
		}
		this.startpassStartrechte.add(startpassStartrechte);
		startpassStartrechte.setStartpaesse(this);

		return startpassStartrechte;
	}

	public StartpassStartrechte removeStartpassStartrechte(StartpassStartrechte startpassStartrechte) {
		if (this.startpassStartrechte == null) {
			this.startpassStartrechte = new ArrayList<>();
		}
		this.startpassStartrechte.remove(startpassStartrechte);
		startpassStartrechte.setStartpaesse(null);

		return startpassStartrechte;
	}

	public String getStartpassNr() {
		return startpassNr;
	}

	public void setStartpassNr(String startpassNr) {
		this.startpassNr = startpassNr;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<StartpassStartrechte> getStartpassStartrechte() {
		return startpassStartrechte;
	}

	public void setStartpassStartrechte(List<StartpassStartrechte> startpassStartrechte) {
		this.startpassStartrechte = startpassStartrechte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((startpassNr == null) ? 0 : startpassNr.hashCode());
		result = prime * result + ((startpassStartrechte == null) ? 0 : startpassStartrechte.hashCode());
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
		Startpass other = (Startpass) obj;
		if (startpassNr == null) {
			if (other.startpassNr != null) {
				return false;
			}
		}
		else if (!startpassNr.equals(other.startpassNr)) {
			return false;
		}
		if (startpassStartrechte == null) {
			if (other.startpassStartrechte != null) {
				return false;
			}
		}
		else if (!startpassStartrechte.equals(other.startpassStartrechte)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Startpass [startpassNr=" + startpassNr + "]";
	}

}
