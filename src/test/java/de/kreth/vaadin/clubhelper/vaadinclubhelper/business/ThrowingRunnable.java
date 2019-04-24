package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

public interface ThrowingRunnable<E extends Exception> {

	void run() throws E;
}
