package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public @Data class Person extends BaseEntity implements Serializable {

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
		group.addPersongroup(this);
		this.groups.add(group);
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

	@Override
	public String toString() {
		return "Person [id=" + getId() + ", prename=" + prename + ", surname=" + surname + "]";
	}

}
