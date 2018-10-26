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
@NamedQuery(name="StartpassStartrechte.findAll", query="SELECT s FROM StartpassStartrechte s")
public class StartpassStartrechte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

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

	public StartpassStartrechte() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getChanged() {
		return new Date(this.changed.getTime());
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public Date getCreated() {
		return new Date(this.created.getTime());
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return new Date(this.deleted.getTime());
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

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