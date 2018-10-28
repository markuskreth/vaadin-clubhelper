package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the startpass_startrechte database table.
 * 
 */
@Entity
@Table(name="startpass_startrechte")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="StartpassStartrechte.findAll", query="SELECT s FROM StartpassStartrechte s")
public class StartpassStartrechte extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 292071407439270519L;

	private String fachgebiet;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="startrecht_beginn")
	private Date startrechtBeginn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="startrecht_ende")
	private Date startrechtEnde;

	@Column(name="verein_name")
	private String vereinName;

	//bi-directional many-to-one association to Startpaesse
	@ManyToOne
	@JoinColumn(name="startpass_id")
	private Startpaesse startpaesse;

	public String getFachgebiet() {
		return this.fachgebiet;
	}

	public void setFachgebiet(String fachgebiet) {
		this.fachgebiet = fachgebiet;
	}

	public Date getStartrechtBeginn() {
		return new Date(this.startrechtBeginn.getTime());
	}

	public void setStartrechtBeginn(Date startrechtBeginn) {
		this.startrechtBeginn = startrechtBeginn;
	}

	public Date getStartrechtEnde() {
		return new Date(this.startrechtEnde.getTime());
	}

	public void setStartrechtEnde(Date startrechtEnde) {
		this.startrechtEnde = startrechtEnde;
	}

	public String getVereinName() {
		return this.vereinName;
	}

	public void setVereinName(String vereinName) {
		this.vereinName = vereinName;
	}

	public Startpaesse getStartpaesse() {
		return this.startpaesse;
	}

	public void setStartpaesse(Startpaesse startpaesse) {
		this.startpaesse = startpaesse;
	}

}