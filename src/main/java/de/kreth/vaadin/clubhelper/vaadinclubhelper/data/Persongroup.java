package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the persongroup database table.
 * 
 */
@Entity
@Table(name = "persongroup")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "Persongroup.findAll", query = "SELECT p FROM Persongroup p")
@EqualsAndHashCode(callSuper = true)
public @Data class Persongroup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2466223964213904302L;

	// bi-directional many-to-one association to Person
	@ManyToOne
	private Person person;

	// bi-directional many-to-one association to GroupDef
	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupDef groupDef;

}
