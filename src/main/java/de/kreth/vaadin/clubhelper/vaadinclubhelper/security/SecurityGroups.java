package de.kreth.vaadin.clubhelper.vaadinclubhelper.security;

public enum SecurityGroups {

	ADMIN("ADMIN"), AKTIVE("Aktive"), WETTKAEMPFER("Wettkämpfer"), UEBUNGSLEITER("Übungsleiter"),
	KAMPFRICHTER("Kampfrichter");

	private final String value;

	private SecurityGroups(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static SecurityGroups byValue(String value) {
		for (SecurityGroups g : values()) {
			if (g.value.equalsIgnoreCase(value)) {
				return g;
			}
		}
		throw new IllegalArgumentException("No SecurityGroups defined for value " + value);
	}
}
