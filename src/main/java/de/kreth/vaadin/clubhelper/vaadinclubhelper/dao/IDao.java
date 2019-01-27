package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.io.Serializable;
import java.util.List;

public interface IDao<T> extends Serializable {

	void save(T obj);

	List<T> listAll();

	T get(Object primaryKey);
}
