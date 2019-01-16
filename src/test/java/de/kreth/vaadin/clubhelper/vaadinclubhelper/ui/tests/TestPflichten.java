package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Pflicht;

public class TestPflichten {

	public static List<Pflicht> getFixedPflichten() {
		List<Pflicht> pflichten = new ArrayList<>();
		for (int i = 1; i < 9; i++) {
			pflichten.add(new Pflicht("P" + i, true, i, null));
		}

		pflichten.add(new Pflicht("M5", true, 15, null));
		pflichten.add(new Pflicht("M6", true, 16, null));
		pflichten.add(new Pflicht("M7", true, 17, null));
		return pflichten;
	}

	public static void main(String[] args) {
		Configuration config = HibernateHolder.configuration();
		SessionFactory sf = config.buildSessionFactory();
		EntityManager em = sf.createEntityManager();

		List<Pflicht> pflichten = getFixedPflichten();
		Date now = new Date();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (Pflicht pf : pflichten) {
			pf.setCreated(now);
			pf.setChanged(now);
			em.persist(pf);
		}
		tx.commit();
		em.close();
	}
}
