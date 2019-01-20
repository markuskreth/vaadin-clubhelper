package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clubevent_addon")
public class CompetitionType {

	@Id
	private String id;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return Type.valueOf(type);
	}

	public void setType(Type type) {
		this.type = type.name();
	}

	public static enum Type {
		EINZEL, SYNCHRON, DOPPELMINI, MANNSCHAFT, LIGA
	}
}
