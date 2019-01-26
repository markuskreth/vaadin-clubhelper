package de.kreth.vaadin.clubhelper;

public class MysqlTestConfiguration extends MysqlLocalConfiguration {

	@Override
	public String getUrl() {
		return "jdbc:mysql://localhost/test?useUnicode=yes&characterEncoding=utf8&serverTimezone=UTC&useSSL=FALSE";
	}

	@Override
	public String getUsername() {
		return "markus";
	}

	@Override
	public String getPassword() {
		return "0773";
	}
}
