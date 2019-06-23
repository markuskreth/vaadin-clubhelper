package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the version database table.
 * 
 */
@Entity
@Table(name = "version")
@NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v")
public class Version implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + id;
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Version other = (Version) obj;
		if (deleted == null) {
			if (other.deleted != null) {
				return false;
			}
		}
		else if (!deleted.equals(other.deleted)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Version [id=");
		stringBuilder.append(id);
		stringBuilder.append(", version=");
		stringBuilder.append(version);
		if (deleted != null) {
			stringBuilder.append(", deleted=");
			stringBuilder.append(deleted);
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

}
