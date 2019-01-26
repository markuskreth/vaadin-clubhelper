package de.kreth.vaadin.clubhelper.vaadinclubhelper.data;

import java.util.Date;

public interface EntityAccessor {

	boolean hasValidId();

	void setChanged(Date changed);

	void setCreated(Date created);

}
