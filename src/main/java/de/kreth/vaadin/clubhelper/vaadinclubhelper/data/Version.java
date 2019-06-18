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

import lombok.Data;

/**
 * The persistent class for the version database table.
 * 
 */
@Entity
@Table(name = "version")
@NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v")
public @Data class Version implements Serializable {

	private static final long serialVersionUID = 1964331485558854626L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private int version;

}
