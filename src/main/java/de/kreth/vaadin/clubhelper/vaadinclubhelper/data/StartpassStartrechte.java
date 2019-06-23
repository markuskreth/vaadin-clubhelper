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

/**
 * The persistent class for the startpass_startrechte database table.
 * 
 */
@Entity
@Table(name = "startpass_startrechte")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "StartpassStartrechte.findAll", query = "SELECT s FROM StartpassStartrechte s")
public class StartpassStartrechte extends BaseEntity implements Serializable {

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

	public String getFachgebiet() {
		return fachgebiet;
	}

	public void setFachgebiet(String fachgebiet) {
		this.fachgebiet = fachgebiet;
	}

	public String getVereinName() {
		return vereinName;
	}

	public void setVereinName(String vereinName) {
		this.vereinName = vereinName;
	}

	public Startpass getStartpaesse() {
		return startpaesse;
	}

	public void setStartpaesse(Startpass startpaesse) {
		this.startpaesse = startpaesse;
	}

	public void setStartrechtBeginn(Date startrechtBeginn) {
		this.startrechtBeginn = startrechtBeginn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fachgebiet == null) ? 0 : fachgebiet.hashCode());
		result = prime * result + ((startrechtBeginn == null) ? 0 : startrechtBeginn.hashCode());
		result = prime * result + ((startrechtEnde == null) ? 0 : startrechtEnde.hashCode());
		result = prime * result + ((vereinName == null) ? 0 : vereinName.hashCode());
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
		StartpassStartrechte other = (StartpassStartrechte) obj;
		if (fachgebiet == null) {
			if (other.fachgebiet != null) {
				return false;
			}
		}
		else if (!fachgebiet.equals(other.fachgebiet)) {
			return false;
		}
		if (startrechtBeginn == null) {
			if (other.startrechtBeginn != null) {
				return false;
			}
		}
		else if (!startrechtBeginn.equals(other.startrechtBeginn)) {
			return false;
		}
		if (startrechtEnde == null) {
			if (other.startrechtEnde != null) {
				return false;
			}
		}
		else if (!startrechtEnde.equals(other.startrechtEnde)) {
			return false;
		}
		if (vereinName == null) {
			if (other.vereinName != null) {
				return false;
			}
		}
		else if (!vereinName.equals(other.vereinName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "StartpassStartrechte [fachgebiet=" + fachgebiet + ", startrechtBeginn=" + startrechtBeginn
				+ ", startrechtEnde=" + startrechtEnde + ", vereinName=" + vereinName + ", startpaesse=" + startpaesse
				+ "]";
	}

}
