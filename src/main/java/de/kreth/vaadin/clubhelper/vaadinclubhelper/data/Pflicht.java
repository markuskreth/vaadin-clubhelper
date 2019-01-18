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

	private static final long serialVersionUID = -1309514158086518524L;

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

	public void setOrdered(int order) {
		this.ordered = order;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
