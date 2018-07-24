package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDaoImpl<T> implements IDao<T> {

	@Autowired
	EntityManager em;

	private final Class<T> entityClass;

	public AbstractDaoImpl(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	public void save(T obj) {
		em.persist(obj);
	}

	@Override
	public List<T> list() {

		TypedQuery<T> query = em.createNamedQuery(
				entityClass.getSimpleName() + ".findAll", entityClass);
		return query.getResultList();
	}

}
