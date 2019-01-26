package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;

public abstract class AbstractDaoImpl<T extends EntityAccessor> implements IDao<T> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

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

		Date now = new Date();
		obj.setChanged(now);
		if (entityManager.contains(obj) || obj.hasValidId()) {

			entityManager.merge(obj);
		} else {
			obj.setCreated(now);
			entityManager.persist(obj);
		}
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
