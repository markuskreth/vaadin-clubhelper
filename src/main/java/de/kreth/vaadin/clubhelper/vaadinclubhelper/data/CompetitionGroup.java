package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.Files;

public class CompetitionGroup implements Serializable {

	private static final long serialVersionUID = 8670759590642549124L;
	public static final int OPEN_END_MAX_YEAR = 9999;
	public static final int OPEN_END_MIN_YEAR = 0;

	private static final Pattern YEAR_PATTERN = Pattern.compile("[^a-zA-Z](\\d{2,4})");
	private static final Pattern COMPULSORY_PATTERN = Pattern.compile("[PMW] ?\\d{1,2}\\s?$");
	private static final Rules RULES = new Rules();
	
	private int minBirthYear;
	private int maxBirthYear = OPEN_END_MAX_YEAR;
	private String compulsory;
	
	private CompetitionGroup() {
	}
	
	CompetitionGroup(int oldest, int youngest) {
		this.maxBirthYear = youngest;
		this.minBirthYear = oldest;
	}
	
	public int getOldestBirthYear() {
		return minBirthYear;
	}
	
	public int getYoungestBirthYear() {
		return maxBirthYear;
	}
	
	public static CompetitionGroup parseLine(String line) {
		CompetitionGroup competitionGroup = new CompetitionGroup();

		Matcher matcher = YEAR_PATTERN.matcher(line);
		if (matcher.find()) {
			competitionGroup.minBirthYear = Integer.parseInt(matcher.group(1));
		}
		if (matcher.find()) {
			competitionGroup.maxBirthYear = Integer.parseInt(matcher.group(1));
		} else {
			if (RULES.isYoungestOnly(line)) {
				competitionGroup.maxBirthYear = competitionGroup.minBirthYear;
				competitionGroup.minBirthYear = OPEN_END_MIN_YEAR;
			}
		}

		if (competitionGroup.maxBirthYear < competitionGroup.minBirthYear) {
			int tmp = competitionGroup.maxBirthYear;
			competitionGroup.maxBirthYear = competitionGroup.minBirthYear;
			competitionGroup.minBirthYear = tmp;
		}
		matcher = COMPULSORY_PATTERN.matcher(line);
		if (matcher.find()) {
			competitionGroup.compulsory = matcher.group().trim();
		}
		return competitionGroup;
	}

	private static class Rules {
		final String youngest;
		
		public Rules() {
			URL uri = getClass().getResource("CompetitionGroupRules.txt");
			try {
				List<String> lines = Files.readLines(new File(uri.toURI()), Charset.defaultCharset());
				youngest = lines.get(0);
			} catch (IOException | URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}
		
		public boolean isYoungestOnly(String line) {
			return line.toLowerCase().contains(youngest);
		}
	}

	public boolean isBirthyearInGroup(int birthYear) {
		return maxBirthYear >= birthYear && minBirthYear <= birthYear;
	}

	public String getCompulsory() {
		return compulsory;
	}
}
