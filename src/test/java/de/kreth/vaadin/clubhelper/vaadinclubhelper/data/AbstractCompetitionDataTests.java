package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class AbstractCompetitionDataTests {

	public AbstractCompetitionDataTests() {
		super();
	}

	public String getGroupTable1() throws IOException, URISyntaxException {
		URL uri = getClass().getResource("CompetitionGroupsBezirksEinzelMS2018.txt");
		return FileUtils.readFileToString(new File(uri.toURI()), Charset.defaultCharset());
	}

	public String getGroupTable2() throws IOException, URISyntaxException {
		URL uri = getClass().getResource("CompetitionGroupsLM2018Trampolin.txt");
		return FileUtils.readFileToString(new File(uri.toURI()), Charset.defaultCharset());
	}

}