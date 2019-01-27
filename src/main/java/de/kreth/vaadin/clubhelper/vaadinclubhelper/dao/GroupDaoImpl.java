package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;

@Repository
public class GroupDaoImpl extends AbstractDaoImpl<GroupDef> implements GroupDao {

	private static final long serialVersionUID = -6675545206382142963L;

	public GroupDaoImpl() {
		super(GroupDef.class);
	}

}
