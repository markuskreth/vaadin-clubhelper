package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "altersgruppe")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Altersgruppe.findAll", query = "SELECT a FROM Altersgruppe a")
@EqualsAndHashCode(callSuper = true)
public @Data class Altersgruppe extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 126215772910869273L;

	private String bezeichnung;

	private int start;

	private int end;

	@ManyToOne
	private Pflicht pflicht;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private ClubEvent clubEvent;

	@Override
	public String toString() {
		return "Altersgruppe [bezeichnung=" + bezeichnung + ", pflicht=" + pflicht + ", jahre=" + start + " - " + end
				+ "]";
	}

	public boolean isBetween(Temporal startDate) {
		int year = startDate.get(ChronoField.YEAR);
		return year >= start && year <= end;
	}

}
