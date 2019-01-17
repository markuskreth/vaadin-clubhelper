package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;

@Repository
public class AltersgruppeDaoImpl extends AbstractDaoImpl<Altersgruppe> implements AltersgruppeDao {

	public AltersgruppeDaoImpl() {
		super(Altersgruppe.class);
	}

}
