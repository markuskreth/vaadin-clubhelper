package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity implements EntityAccessor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	public Date getChanged() {
		if (changed == null) {
			return null;
		}
		return new Date(this.changed.getTime());
	}

	@Override
	public void setChanged(Date changed) {
		this.changed = new Date(changed.getTime());
	}

	public Date getCreated() {
		if (created == null) {
			return null;
		}
		return new Date(this.created.getTime());
	}

	@Override
	public void setCreated(Date created) {
		this.created = new Date(created.getTime());
	}

	public Date getDeleted() {
		if (deleted == null) {
			return null;
		}
		return new Date(this.deleted.getTime());
	}

	public void setDeleted(Date deleted) {
		this.deleted = new Date(deleted.getTime());
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean hasValidId() {
		return id > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changed == null) ? 0 : changed.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + id;
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
		BaseEntity other = (BaseEntity) obj;
		if (changed == null) {
			if (other.changed != null) {
				return false;
			}
		}
		else if (!changed.equals(other.changed)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		}
		else if (!created.equals(other.created)) {
			return false;
		}
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
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("BaseEntity [id=");
		stringBuilder.append(id);
		stringBuilder.append(", changed=");
		stringBuilder.append(changed);
		if (deleted != null) {
			stringBuilder.append(", deleted=");
			stringBuilder.append(deleted);
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

}
