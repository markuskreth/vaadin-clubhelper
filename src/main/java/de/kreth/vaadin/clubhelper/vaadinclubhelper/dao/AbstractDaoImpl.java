package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;

public abstract class AbstractDaoImpl<T extends EntityAccessor> implements IDao<T> {

	@Autowired
	protected EntityManager entityManager;

	private final Class<T> entityClass;

	public AbstractDaoImpl(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	@Transactional
	public void save(T obj) {

		if (entityManager.contains(obj) || obj.hasValidId()) {
			entityManager.merge(obj);
		} else {
			entityManager.persist(obj);
		}
	}

	@Override
	@Transactional
	public T update(T obj) {
		return entityManager.merge(obj);
	}

	@Override
	public T get(Object primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	@Override
	public List<T> listAll() {
		TypedQuery<T> query = entityManager.createNamedQuery(entityClass.getSimpleName() + ".findAll", entityClass);
		return query.getResultList();
	}

}
