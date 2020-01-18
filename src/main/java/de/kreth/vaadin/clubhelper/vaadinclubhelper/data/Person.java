package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@NamedQuery(name = Person.QUERY_FINDALL, query = "SELECT p FROM Person p WHERE p.deleted is  null")
@NamedQuery(name = Person.QUERY_FINDLOGIN, query = "FROM Person WHERE username = :username AND password = :password AND deleted is"
		+ " null")
public class Person extends BaseEntity implements Serializable {

	public static final String SESSION_LOGIN = "SESSION_LOGIN_USER";

	public static final String QUERY_FINDALL = "Person.findAll";

	public static final String QUERY_FINDLOGIN = "Person.findLogin";

	private static final long serialVersionUID = -8361264400619997123L;

	@Basic
	private LocalDate birth;

	private String prename;

	private String surname;

	private String username;

	private String password;

	private Integer gender;

	@OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private Startpass startpass;

	// bi-directional many-to-one association to Adress
	@OneToMany(mappedBy = "person", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Adress> adresses;

	// bi-directional many-to-one association to Attendance
	@OneToMany(mappedBy = "person", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Attendance> attendances;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "person", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Contact> contacts;

	@OneToMany(mappedBy = "person", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<PersonNote> notes;

	// bi-directional many-to-many association to Persongroup
	@ManyToMany(targetEntity = GroupDef.class, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH })
	@JoinTable(name = "persongroup", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
	private Set<GroupDef> groups;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person1Bean", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Relative> relatives1;

	// bi-directional many-to-one association to Relative
	@OneToMany(mappedBy = "person2Bean", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Relative> relatives2;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "persons")
	private Set<ClubEvent> events;

	public Gender getGender() {
		if (gender == null) {
			return null;
		}
		return Gender.valueOf(gender);
	}

	public void setGender(Gender gender) {
		if (gender == null) {
			this.gender = null;
		}
		else {
			this.gender = gender.getId();
		}
	}

	public void add(GroupDef group) {
		if (this.groups == null) {
			this.groups = new HashSet<>();
		}
		this.groups.add(group);
	}

	public void remove(GroupDef g) {
		if (this.groups != null) {
			this.groups.remove(g);
		}
	}

	public void add(ClubEvent ev) {
		if (this.events == null) {
			this.events = new HashSet<>();
		}
		this.events.add(ev);
	}

	public void add(Contact contact) {
		contact.setPerson(this);
		if (contacts == null) {
			contacts = new ArrayList<>();
		}
		contacts.add(contact);
	}

	public void remove(ClubEvent clubEvent) {
		events.remove(clubEvent);
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Adress> getAdresses() {
		if (adresses == null) {
			return Collections.emptyList();
		}
		return adresses;
	}

	public void setAdresses(List<Adress> adresses) {
		this.adresses = adresses;
	}

	public List<Attendance> getAttendances() {
		if (attendances == null) {
			return Collections.emptyList();
		}
		return attendances;
	}

	public void setAttendances(List<Attendance> attendances) {
		this.attendances = attendances;
	}

	public List<Contact> getContacts() {
		if (contacts == null) {
			return Collections.emptyList();
		}
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public Set<GroupDef> getGroups() {
		if (groups == null) {
			return Collections.emptySet();
		}
		return groups;
	}

	public void setGroups(Set<GroupDef> groups) {
		this.groups = groups;
	}

	public List<Relative> getRelatives1() {
		if (relatives1 == null) {
			return Collections.emptyList();
		}
		return relatives1;
	}

	public void setRelatives1(List<Relative> relatives1) {
		this.relatives1 = relatives1;
	}

	public List<Relative> getRelatives2() {
		if (relatives2 == null) {
			return Collections.emptyList();
		}
		return relatives2;
	}

	public void setRelatives2(List<Relative> relatives2) {
		this.relatives2 = relatives2;
	}

	public Set<ClubEvent> getEvents() {
		if (events == null) {
			return Collections.emptySet();
		}
		return events;
	}

	public void setEvents(Set<ClubEvent> events) {
		this.events = events;
	}

	public Startpass getStartpass() {
		return startpass;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
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

	public void setPersongroups(Collection<GroupDef> persongroups) {
		if (persongroups instanceof Set) {
			this.groups = (Set<GroupDef>) persongroups;
		}
		else {
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
		if (this.groups != null) {
			this.groups.remove(persongroup);
		}
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

	public void setStartpass(Startpass startpass) {
		this.startpass = startpass;
	}

	public void setStartpass(String startpass) {
		if (this.startpass == null) {
			if (startpass == null || startpass.isBlank()) {
				return;
			}
			this.startpass = new Startpass();
			this.startpass.setPerson(this);
		}
		this.startpass.setStartpassNr(startpass);
	}

	public List<PersonNote> getNotes() {
		if (notes == null) {
			notes = new ArrayList<>();
		}
		return notes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adresses == null) ? 0 : adresses.hashCode());
		result = prime * result + ((attendances == null) ? 0 : attendances.hashCode());
		result = prime * result + ((birth == null) ? 0 : birth.hashCode());
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((prename == null) ? 0 : prename.hashCode());
		result = prime * result + ((relatives1 == null) ? 0 : relatives1.hashCode());
		result = prime * result + ((relatives2 == null) ? 0 : relatives2.hashCode());
		result = prime * result + ((startpass == null) ? 0 : startpass.hashCode());
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
		if (adresses == null) {
			if (other.adresses != null) {
				return false;
			}
		}
		else if (!adresses.equals(other.adresses)) {
			return false;
		}
		if (attendances == null) {
			if (other.attendances != null) {
				return false;
			}
		}
		else if (!attendances.equals(other.attendances)) {
			return false;
		}
		if (birth == null) {
			if (other.birth != null) {
				return false;
			}
		}
		else if (!birth.equals(other.birth)) {
			return false;
		}
		if (contacts == null) {
			if (other.contacts != null) {
				return false;
			}
		}
		else if (!contacts.equals(other.contacts)) {
			return false;
		}
		if (events == null) {
			if (other.events != null) {
				return false;
			}
		}
		else if (!events.equals(other.events)) {
			return false;
		}
		if (gender == null) {
			if (other.gender != null) {
				return false;
			}
		}
		else if (!gender.equals(other.gender)) {
			return false;
		}
		if (groups == null) {
			if (other.groups != null) {
				return false;
			}
		}
		else if (!groups.equals(other.groups)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		}
		else if (!password.equals(other.password)) {
			return false;
		}
		if (prename == null) {
			if (other.prename != null) {
				return false;
			}
		}
		else if (!prename.equals(other.prename)) {
			return false;
		}
		if (relatives1 == null) {
			if (other.relatives1 != null) {
				return false;
			}
		}
		else if (!relatives1.equals(other.relatives1)) {
			return false;
		}
		if (relatives2 == null) {
			if (other.relatives2 != null) {
				return false;
			}
		}
		else if (!relatives2.equals(other.relatives2)) {
			return false;
		}
		if (startpass == null) {
			if (other.startpass != null) {
				return false;
			}
		}
		else if (!startpass.equals(other.startpass)) {
			return false;
		}
		if (surname == null) {
			if (other.surname != null) {
				return false;
			}
		}
		else if (!surname.equals(other.surname)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		}
		else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + getId() + ", prename=" + prename + ", surname=" + surname + "]";
	}

	public boolean hasGroup(GroupDef g) {
		return groups != null && groups.contains(g);
	}

	public boolean hasAnyGroup() {
		return groups != null && !groups.isEmpty();
	}

	public boolean hasGroup(String value) {
		for (GroupDef g : groups) {
			if (g.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

}
