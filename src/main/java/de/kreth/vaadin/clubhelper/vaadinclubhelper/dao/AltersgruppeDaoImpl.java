package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;

@Repository
public class AltersgruppeDaoImpl extends AbstractDaoImpl<Altersgruppe> implements AltersgruppeDao {

	private static final long serialVersionUID = 746136196155133506L;

	public AltersgruppeDaoImpl() {
		super(Altersgruppe.class);
	}

}
