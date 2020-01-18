package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "notes")
@NamedQuery(name = PersonNote.QUERY_FINDALL, query = "SELECT p FROM Person p WHERE p.deleted is  null")
public class PersonNote {

	public static final String QUERY_FINDALL = "PersonNote.findAll";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	private Person person;

	private String notekey;

	private String notetext;

	public PersonNote() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getNotekey() {
		return notekey;
	}

	public void setNotekey(String notekey) {
		this.notekey = notekey;
	}

	public String getNotetext() {
		return notetext;
	}

	public void setNotetext(String notetext) {
		this.notetext = notetext;
	}

	@Override
	public String toString() {
		return "PersonNote [id=" + id + ", person=" + person + ", notekey=" + notekey + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((notekey == null) ? 0 : notekey.hashCode());
		result = prime * result + ((notetext == null) ? 0 : notetext.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		PersonNote other = (PersonNote) obj;
		if (id != other.id) {
			return false;
		}
		if (notekey == null) {
			if (other.notekey != null) {
				return false;
			}
		}
		else if (!notekey.equals(other.notekey)) {
			return false;
		}
		if (notetext == null) {
			if (other.notetext != null) {
				return false;
			}
		}
		else if (!notetext.equals(other.notetext)) {
			return false;
		}
		if (person == null) {
			if (other.person != null) {
				return false;
			}
		}
		else if (!person.equals(other.person)) {
			return false;
		}
		return true;
	}

}
