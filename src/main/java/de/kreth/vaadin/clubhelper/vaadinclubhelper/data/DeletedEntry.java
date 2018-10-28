package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the deleted_entries database table.
 * 
 */
@Entity
@Table(name="deleted_entries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name="DeletedEntry.findAll", query="SELECT d FROM DeletedEntry d")
public class DeletedEntry extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4271189592258131845L;

	private int entryId;

	private String tablename;

	public DeletedEntry() {
	}

	public int getEntryId() {
		return this.entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

}