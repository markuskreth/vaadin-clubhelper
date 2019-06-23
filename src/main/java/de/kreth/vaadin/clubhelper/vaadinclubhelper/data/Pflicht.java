package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pflichten")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Pflicht.findAll", query = "SELECT p FROM Pflicht p")
public class Pflicht extends BaseEntity implements Serializable, Comparable<Pflicht> {

	private String name;

	private boolean fixed;

	private int ordered;

	private String comment;

	public Pflicht() {
	}

	public Pflicht(String name, boolean fixed, int ordered, String comment) {
		this.name = name;
		this.fixed = fixed;
		this.ordered = ordered;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public int getOrdered() {
		return ordered;
	}

	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (fixed ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ordered;
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
		Pflicht other = (Pflicht) obj;
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		}
		else if (!comment.equals(other.comment)) {
			return false;
		}
		if (fixed != other.fixed) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (ordered != other.ordered) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Pflicht o) {
		return Integer.compare(ordered, o.ordered);
	}
}
