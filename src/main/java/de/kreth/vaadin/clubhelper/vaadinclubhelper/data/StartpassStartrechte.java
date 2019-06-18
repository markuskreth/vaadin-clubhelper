package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the startpass_startrechte database table.
 * 
 */
@Entity
@Table(name = "startpass_startrechte")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "StartpassStartrechte.findAll", query = "SELECT s FROM StartpassStartrechte s")
@EqualsAndHashCode(callSuper = false)
public @Data class StartpassStartrechte extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 292071407439270519L;

	private String fachgebiet;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startrecht_beginn")
	private Date startrechtBeginn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startrecht_ende")
	private Date startrechtEnde;

	@Column(name = "verein_name")
	private String vereinName;

	// bi-directional many-to-one association to Startpaesse
	@ManyToOne
	@JoinColumn(name = "startpass_id")
	private Startpass startpaesse;

	public Date getStartrechtBeginn() {
		return new Date(this.startrechtBeginn.getTime());
	}

	public Date getStartrechtEnde() {
		return new Date(this.startrechtEnde.getTime());
	}

	public void setStartrechtEnde(Date startrechtEnde) {
		this.startrechtEnde = startrechtEnde;
	}

}
