package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@MappedSuperclass
public @Data abstract class BaseEntity implements EntityAccessor {

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
	public boolean hasValidId() {
		return id > 0;
	}

}
