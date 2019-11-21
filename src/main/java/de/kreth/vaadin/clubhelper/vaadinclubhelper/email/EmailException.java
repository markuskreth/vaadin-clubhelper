package de.kreth.vaadin.clubhelper.vaadinclubhelper.email;

import java.util.List;

public class EmailException extends RuntimeException {

	private final List<String> failedEmails;

	public EmailException(String message, List<String> failedEmails) {
		super(message);
		this.failedEmails = failedEmails;
	}

	public EmailException(String message, List<String> failedEmails, Throwable cause) {
		super(message, cause);
		this.failedEmails = failedEmails;
	}

	public List<String> getFailedEmails() {
		return failedEmails;
	}

}
