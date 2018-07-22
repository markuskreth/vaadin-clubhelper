package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

public interface IDao<T> {

	void save(T obj);
	List<T> list();
}
