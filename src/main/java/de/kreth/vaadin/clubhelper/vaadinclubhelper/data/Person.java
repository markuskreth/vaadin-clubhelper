package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.persistence.Table;

/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = Person.QUERY_FINDALL, query = "SELECT p FROM Person p")
public class Person extends BaseEntity implements Serializable {

	public final static String QUERY_FINDALL = "Person.findAll";

	private static final long serialVersionUID = -8361264400619997123L;

	private LocalDate birth;

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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "persongroup", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
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

	public List<GroupDef> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDef> groups) {
		this.groups = groups;
	}

	public void add(GroupDef group) {

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
	public String toString() {
		return "Person [id=" + getId() + ", prename=" + prename + ", surname=" + surname + "]";
	}

}