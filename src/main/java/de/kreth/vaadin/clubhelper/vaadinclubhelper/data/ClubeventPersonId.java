package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
public @Data class ClubeventPersonId implements Serializable {

	private static final long serialVersionUID = -7964369346971364916L;

	@Column(name = "clubevent_id")
	private String clubEventId;

	@Column(name = "person_id")
	private int personId;

	@Override
	public String toString() {
		return "ID [clubEventId=" + clubEventId + ", personId=" + personId + "]";
	}

}
