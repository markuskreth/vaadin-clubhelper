package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = Person.QUERY_FINDALL, query = "SELECT p FROM Person p")
@NamedQuery(name = Person.QUERY_FINDLOGIN, query = "FROM Person WHERE username = :username AND password = :password")
public class Person extends BaseEntity implements Serializable {

	public static final String SESSION_LOGIN = "SESSION_LOGIN_USER";

	public static final String QUERY_FINDALL = "Person.findAll";
	public static final String QUERY_FINDLOGIN = "Person.findLogin";

	private static final long serialVersionUID = -8361264400619997123L;

	private LocalDate birth;

	private String prename;
	private String surname;

	private String username;
	private String password;

	private Integer gender;

	@OneToOne(mappedBy = "person")
	private Startpass startpass;

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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "persongroup", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
	private Set<GroupDef> groups;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person1Bean")
	private List<Relative> relatives1;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person2Bean")
	private List<Relative> relatives2;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "persons")
	private Set<ClubEvent> events;

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
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

	public Gender getGender() {
		if (gender == null) {
			return null;
		}
		return Gender.valueOf(gender);
	}

	public void setGender(Gender gender) {
		if (gender == null) {
			this.gender = null;
		} else {
			this.gender = gender.getId();
		}
	}

	public Set<GroupDef> getGroups() {
		return groups;
	}

	public void setGroups(Set<GroupDef> groups) {
		this.groups = groups;
	}

	public void add(GroupDef group) {
		if (this.groups == null) {
			this.groups = new HashSet<>();
		}
		this.groups.add(group);
	}

	public Set<ClubEvent> getEvents() {
		return events;
	}

	public void setEvents(Set<ClubEvent> events) {
		this.events = events;
	}

	public void add(ClubEvent ev) {
		if (this.events == null) {
			this.events = new HashSet<>();
		}
		this.events.add(ev);
	}

	public void remove(ClubEvent clubEvent) {
		events.remove(clubEvent);
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

	public Set<GroupDef> getPersongroups() {
		return this.groups;
	}

	public void setPersongroups(Collection<GroupDef> persongroups) {
		if (persongroups instanceof Set) {
			this.groups = (Set<GroupDef>) persongroups;
		} else {
			this.groups = new HashSet<>(persongroups);
		}
	}

	public void addPersongroup(GroupDef persongroup) {
		if (this.groups == null) {
			this.groups = new HashSet<>();
		}
		this.groups.add(persongroup);
	}

	public void removePersongroup(GroupDef persongroup) {
		if (this.groups == null) {
			this.groups.remove(persongroup);
		}
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

	public Startpass getStartpass() {
		return startpass;
	}

	public void setStartpass(Startpass startpass) {
		this.startpass = startpass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((birth == null) ? 0 : birth.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((prename == null) ? 0 : prename.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Person other = (Person) obj;
		if (birth == null) {
			if (other.birth != null) {
				return false;
			}
		} else if (!birth.equals(other.birth)) {
			return false;
		}
		if (groups == null) {
			if (other.groups != null) {
				return false;
			}
		} else if (!groups.equals(other.groups)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (prename == null) {
			if (other.prename != null) {
				return false;
			}
		} else if (!prename.equals(other.prename)) {
			return false;
		}
		if (surname == null) {
			if (other.surname != null) {
				return false;
			}
		} else if (!surname.equals(other.surname)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + getId() + ", prename=" + prename + ", surname=" + surname + "]";
	}

}