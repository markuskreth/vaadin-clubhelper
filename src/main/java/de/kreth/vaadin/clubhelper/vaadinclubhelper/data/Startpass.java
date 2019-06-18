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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the startpaesse database table.
 * 
 */
@Entity
@Table(name = "startpaesse")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Startpass.findAll", query = "SELECT s FROM Startpass s")
@EqualsAndHashCode(callSuper = true)
public @Data class Startpass extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1464510869007022357L;

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

	@Override
	public String toString() {
		return "Startpass [startpassNr=" + startpassNr + "]";
	}

}
