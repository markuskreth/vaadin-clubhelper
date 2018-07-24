package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;

@Repository
public class GroupDaoImpl extends AbstractDaoImpl<GroupDef>
		implements
			GroupDao {

	public GroupDaoImpl() {
		super(GroupDef.class);
	}

}
