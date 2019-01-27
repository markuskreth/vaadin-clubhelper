package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Pflicht;

@Repository
@Transactional
public class PflichtenDaoImpl extends AbstractDaoImpl<Pflicht> implements PflichtenDao {

	private static final long serialVersionUID = 5660723465403552334L;

	public PflichtenDaoImpl() {
		super(Pflicht.class);
	}

}
