package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CompetitionDetails implements Serializable {

	private static final long serialVersionUID = 2167132205051467946L;

	private Set<CompetitionGroup> cpGroups;
	
	public Set<CompetitionGroup> getCpGroups() {
		return cpGroups;
	}
	
	public void parseCompetitionGroups(String text) {
		cpGroups = new HashSet<>();
		String[] lines = text.split("\n");
		for (String line: lines) {
			cpGroups.add(CompetitionGroup.parseLine(line));
		}
	}
}
