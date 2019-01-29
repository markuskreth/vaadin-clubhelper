package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the adress database table.
 * 
 */
@Entity
@Table(name = "adress")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Adress.findAll", query = "SELECT a FROM Adress a")
public class Adress extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8216273166570667412L;

	private String adress1;

	private String adress2;

	private String city;

	private String plz;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	public String getAdress1() {
		return this.adress1;
	}

	public void setAdress1(String adress1) {
		this.adress1 = adress1;
	}

	public String getAdress2() {
		return this.adress2;
	}

	public void setAdress2(String adress2) {
		this.adress2 = adress2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPlz() {
		return this.plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adress1 == null) ? 0 : adress1.hashCode());
		result = prime * result + ((adress2 == null) ? 0 : adress2.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((plz == null) ? 0 : plz.hashCode());
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
		Adress other = (Adress) obj;
		if (adress1 == null) {
			if (other.adress1 != null) {
				return false;
			}
		} else if (!adress1.equals(other.adress1)) {
			return false;
		}
		if (adress2 == null) {
			if (other.adress2 != null) {
				return false;
			}
		} else if (!adress2.equals(other.adress2)) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (person == null) {
			if (other.person != null) {
				return false;
			}
		} else if (!person.equals(other.person)) {
			return false;
		}
		if (plz == null) {
			if (other.plz != null) {
				return false;
			}
		} else if (!plz.equals(other.plz)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Adress [adress1=" + adress1 + ", adress2=" + adress2 + ", plz=" + plz + ", city=" + city + "]";
	}

}
