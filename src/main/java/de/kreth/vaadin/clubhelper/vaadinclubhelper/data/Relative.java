package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the relative database table.
 * 
 */
@Entity
@Table(name="relative")
@NamedQuery(name="Relative.findAll", query="SELECT r FROM Relative r")
public class Relative implements Serializable {
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

	@Column(name="TO_PERSON1_RELATION")
	private String toPerson1Relation;

	@Column(name="TO_PERSON2_RELATION")
	private String toPerson2Relation;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person1")
	private Person person1Bean;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="person2")
	private Person person2Bean;

	public Relative() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getChanged() {
		return this.changed;
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public String getToPerson1Relation() {
		return this.toPerson1Relation;
	}

	public void setToPerson1Relation(String toPerson1Relation) {
		this.toPerson1Relation = toPerson1Relation;
	}

	public String getToPerson2Relation() {
		return this.toPerson2Relation;
	}

	public void setToPerson2Relation(String toPerson2Relation) {
		this.toPerson2Relation = toPerson2Relation;
	}

	public Person getPerson1Bean() {
		return this.person1Bean;
	}

	public void setPerson1Bean(Person person1Bean) {
		this.person1Bean = person1Bean;
	}

	public Person getPerson2Bean() {
		return this.person2Bean;
	}

	public void setPerson2Bean(Person person2Bean) {
		this.person2Bean = person2Bean;
	}

}