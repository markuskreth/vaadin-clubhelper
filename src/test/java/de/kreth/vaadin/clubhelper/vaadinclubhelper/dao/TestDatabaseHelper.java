package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public interface TestDatabaseHelper {

	List<Person> insertPersons(int count);

	List<Person> createPersons(int count);

	ClubEvent creteEvent();

	List<ClubeventHasPerson> loadEventPersons();

	List<ClubEvent> allClubEvent();

	Person testInsertPerson();

	public static void afterCommit(final Runnable r) {

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				r.run();
			}
		});
	}

	public static void waitForCommit() {
		afterCommit(() -> {
		});
	}
}
