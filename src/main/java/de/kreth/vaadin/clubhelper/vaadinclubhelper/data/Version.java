package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the version database table.
 * 
 */
@Entity
@Table(name="version")
@NamedQuery(name="Version.findAll", query="SELECT v FROM Version v")
public class Version implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private int version;

	public Version() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDeleted() {
		return new Date(this.deleted.getTime());
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}