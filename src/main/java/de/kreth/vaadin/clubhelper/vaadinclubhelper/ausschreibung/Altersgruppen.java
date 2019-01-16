package de.kreth.vaadin.clubhelper.vaadinclubhelper.ausschreibung;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;

public class Altersgruppen {

	private List<Altersgruppe> values;

	public Altersgruppen() {
		this.values = new ArrayList<>();
	}

	public static Altersgruppen parse(boolean hasCaption, String inputText) {

		Altersgruppen result = new Altersgruppen();

		List<String> lines = inputText.lines().collect(Collectors.toList());
		if (hasCaption) {
			lines.remove(0);
		}
		for (String line : lines) {
			Altersgruppe g = new Altersgruppe();
			g.setBezeichnung(line);
			result.getValues().add(g);
		}
		return result;
	}

	public List<Altersgruppe> getValues() {
		return values;
	}

}
