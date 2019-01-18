package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

public enum Gender {

	MALE(1), FEMALE(2);
	private final int id;

	private Gender(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Gender valueOf(Integer id) {
		return valueOf(id.intValue());
	}

	public static Gender valueOf(int id) {
		for (Gender g : values()) {
			if (g.id == id) {
				return g;
			}
		}
		throw new IllegalArgumentException("No Gender for id=" + id + " defined.");
	}
}
