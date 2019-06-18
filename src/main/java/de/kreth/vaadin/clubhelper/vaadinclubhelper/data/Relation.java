package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Relation {

	public enum RelationType {
		RELATIONSHIP("Partner"),
		SIBLINGS("Geschwister"),
		PARENT("Elternteil"),
		CHILD("Kind");

		private final String localized;

		private RelationType(String localized) {
			this.localized = localized;
		}

		public String getLocalized() {
			return localized;
		}
	}

	private Person person;

	private String relation;

	public Relation(Person person, String relation) {
		super();
		this.person = person;
		this.relation = relation;
	}

	public RelationType getRelation() {
		return RelationType.valueOf(relation);
	}

}
