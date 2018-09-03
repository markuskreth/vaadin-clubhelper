package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name = "person")
@NamedQuery(name = Person.QUERY_FINDALL, query = "SELECT p FROM Person p")
public class Person implements Serializable {

	public final static String QUERY_FINDALL = "Person.findAll";

	private static final long serialVersionUID = -8361264400619997123L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date birth;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	private String password;

	private String prename;

	private String surname;

	private String username;

	// bi-directional many-to-one association to Adress
	@OneToMany(mappedBy = "person")
	private List<Adress> adresses;

	// bi-directional many-to-one association to Attendance
	@OneToMany(mappedBy = "person")
	private List<Attendance> attendances;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "person")
	private List<Contact> contacts;

	// bi-directional many-to-many association to Persongroup
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable (name = "persongroup", 
	        joinColumns = { @JoinColumn(name = "person_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private List<GroupDef> groups;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person1Bean")
	private List<Relative> relatives1;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person2Bean")
	private List<Relative> relatives2;

	// bi-directional many-to-one association to Startpaesse
	@OneToMany(mappedBy = "person")
	private List<Startpaesse> startpaesses;

	@ManyToMany(cascade = { CascadeType.MERGE,  CascadeType.REFRESH, CascadeType.REMOVE,  CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(
	        name = "clubevent_has_person", 
	        joinColumns = { @JoinColumn(name = "person_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "clubevent_id") }
	    )
	private Set<ClubEvent> events;
	
	public Person() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBirth() {
		return this.birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrename() {
		return this.prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Adress> getAdresses() {
		return this.adresses;
	}

	public void setAdresses(List<Adress> adresses) {
		this.adresses = adresses;
	}

	public Adress addAdress(Adress adress) {
		getAdresses().add(adress);
		adress.setPerson(this);

		return adress;
	}

	public Adress removeAdress(Adress adress) {
		getAdresses().remove(adress);
		adress.setPerson(null);

		return adress;
	}

	public List<Attendance> getAttendances() {
		return this.attendances;
	}

	public void setAttendances(List<Attendance> attendances) {
		this.attendances = attendances;
	}

	public Attendance addAttendance(Attendance attendance) {
		getAttendances().add(attendance);
		attendance.setPerson(this);

		return attendance;
	}

	public Attendance removeAttendance(Attendance attendance) {
		getAttendances().remove(attendance);
		attendance.setPerson(null);

		return attendance;
	}

	public List<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public Contact addContact(Contact contact) {
		getContacts().add(contact);
		contact.setPerson(this);

		return contact;
	}

	public Contact removeContact(Contact contact) {
		getContacts().remove(contact);
		contact.setPerson(null);

		return contact;
	}

	public List<GroupDef> getPersongroups() {
		return this.groups;
	}

	public void setPersongroups(List<GroupDef> persongroups) {
		this.groups = persongroups;
	}

	public void addPersongroup(GroupDef persongroup) {
		getPersongroups().add(persongroup);
	}

	public void removePersongroup(GroupDef persongroup) {
		getPersongroups().remove(persongroup);
	}

	public List<Relative> getRelatives1() {
		return this.relatives1;
	}

	public void setRelatives1(List<Relative> relatives1) {
		this.relatives1 = relatives1;
	}

	public Relative addRelatives1(Relative relatives1) {
		getRelatives1().add(relatives1);
		relatives1.setPerson1Bean(this);

		return relatives1;
	}

	public Relative removeRelatives1(Relative relatives1) {
		getRelatives1().remove(relatives1);
		relatives1.setPerson1Bean(null);

		return relatives1;
	}

	public List<Relative> getRelatives2() {
		return this.relatives2;
	}

	public void setRelatives2(List<Relative> relatives2) {
		this.relatives2 = relatives2;
	}

	public Relative addRelatives2(Relative relatives2) {
		getRelatives2().add(relatives2);
		relatives2.setPerson2Bean(this);

		return relatives2;
	}

	public Relative removeRelatives2(Relative relatives2) {
		getRelatives2().remove(relatives2);
		relatives2.setPerson2Bean(null);

		return relatives2;
	}

	public List<Startpaesse> getStartpaesses() {
		return this.startpaesses;
	}

	public void setStartpaesses(List<Startpaesse> startpaesses) {
		this.startpaesses = startpaesses;
	}

	public Startpaesse addStartpaess(Startpaesse startpaess) {
		getStartpaesses().add(startpaess);
		startpaess.setPerson(this);

		return startpaess;
	}

	public Startpaesse removeStartpaess(Startpaesse startpaess) {
		getStartpaesses().remove(startpaess);
		startpaess.setPerson(null);
		return startpaess;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + id;
		result = prime * result + ((prename == null) ? 0 : prename.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (id != other.id)
			return false;
		if (prename == null) {
			if (other.prename != null)
				return false;
		} else if (!prename.equals(other.prename))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", prename=" + prename + ", surname=" + surname + "]";
	}

}