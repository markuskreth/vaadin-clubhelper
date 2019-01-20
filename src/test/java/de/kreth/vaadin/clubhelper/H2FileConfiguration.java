package de.kreth.vaadin.clubhelper;

import java.io.File;

public class H2FileConfiguration extends H2MemoryConfiguration {

	@Override
	public String getUrl() {
		File f = new File("./database");
		System.out.println("Databasepath: " + f.getAbsolutePath());
		return "jdbc:h2:file:" + f.getAbsolutePath();
	}
}
