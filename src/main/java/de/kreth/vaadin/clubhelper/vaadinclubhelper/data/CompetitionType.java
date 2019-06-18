package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "clubevent_addon")
public @Data class CompetitionType implements Serializable {

	private static final long serialVersionUID = -6198405472773618194L;

	@Id
	private String id;

	@Column(name = "competition_type", nullable = false, length = 45)
	private String type;

	@OneToOne(mappedBy = "competitionType")
	@JoinColumn(name = "id")
	@MapsId
	private ClubEvent clubEvent;

	public Type getType() {
		return Type.valueOf(type);
	}

	public void setType(Type type) {
		this.type = type.name();
	}

	public void setClubEvent(ClubEvent clubEvent) {
		this.clubEvent = clubEvent;
	}

	public static enum Type {
		EINZEL,
		SYNCHRON,
		DOPPELMINI,
		MANNSCHAFT,
		LIGA
	}

	@Override
	public String toString() {
		return type;
	}
}
