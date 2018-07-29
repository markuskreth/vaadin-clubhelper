package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDaoImpl<T> implements IDao<T> {

	@Autowired
	EntityManager em;

	private final Class<T> entityClass;

	public AbstractDaoImpl(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	@Transactional
	public void save(T obj) {
		// EntityTransaction tx = em.getTransaction();
		// tx.begin();
		// try {
		em.persist(obj);
		// tx.commit();
		// } catch (Exception e) {
		// tx.rollback();
		// }
	}

	@Override
	public List<T> list() {

		TypedQuery<T> query = em.createNamedQuery(
				entityClass.getSimpleName() + ".findAll", entityClass);
		return query.getResultList();
	}

}
