package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;

public abstract class AbstractDaoImpl<T extends EntityAccessor> implements IDao<T> {

	@Autowired
	protected EntityManager em;

	private final Class<T> entityClass;

	public AbstractDaoImpl(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	@Transactional
	public void save(T obj) {

		if (em.contains(obj) || obj.hasValidId()) {
			em.merge(obj);
		} else {
			em.persist(obj);
		}
	}

	@Override
	@Transactional
	public T update(T obj) {
		return em.merge(obj);
	}

	@Override
	public T get(Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	@Override
	public List<T> listAll() {
		TypedQuery<T> query = em.createNamedQuery(entityClass.getSimpleName() + ".findAll", entityClass);
		return query.getResultList();
	}

}
