package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

public interface IDao<T> {

	void save(T obj);
	T update(T obj);
	List<T> list();
	T get(Object primaryKey);
}
