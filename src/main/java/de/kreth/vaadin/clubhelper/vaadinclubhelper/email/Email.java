package de.kreth.vaadin.clubhelper.vaadinclubhelper.email;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Email {

	private final String subject;

	private final String message;

	private final List<String> emails;

	private final List<Path> attachements;

	private Email(Builder builder) {
		this.subject = builder.subject;
		this.message = builder.message;
		this.emails = Collections.unmodifiableList(builder.emails);
		this.attachements = Collections.unmodifiableList(builder.attachements);
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getEmails() {
		return emails;
	}

	public List<Path> getAttachements() {
		return attachements;
	}

	@Override
	public String toString() {
		return "Email [subject=" + subject + ", emails=" + emails + "]";
	}

	public static class Builder {

		private String subject;

		private String message;

		private List<String> emails;

		private List<Path> attachements;

		private Builder() {
			emails = new ArrayList<>();
			attachements = new ArrayList<>();
		}

		public Builder setSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder addEmails(List<String> emails) {
			this.emails.addAll(emails);
			return this;
		}

		public boolean addAttachment(Path e) {
			return attachements.add(e);
		}

		public boolean addAttachments(Collection<? extends Path> c) {
			return attachements.addAll(c);
		}

		public Email build() {
			return new Email(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}
