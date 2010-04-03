package org.futurepages.core.persistence.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author Marvin H Froeder (velo.br@gmail.com)
 */
public class JpaTransaction implements Transaction {

	private EntityManager session = null;

	private EntityTransaction transaction = null;

	private boolean active = false;

	private boolean commited = false;

	private boolean rolledback = false;

	public JpaTransaction() {
	}

	public JpaTransaction(EntityManager session) {
		this.session = session;
	}

	public JpaTransaction(EntityManager session, EntityTransaction transaction) {
		this(session);
		this.transaction = transaction;
	}

	public void setSession(EntityManager session) {
		this.session = session;
	}

	public void setTransaction(EntityTransaction transaction) {
		this.transaction = transaction;
	}

	public void begin() throws Exception {
		if (active || commited || rolledback)
			throw new IllegalStateException("Cannot begin transaction again!");
		if (session == null)
			throw new IllegalStateException(
					"HibernateTransaction does not have a hibernate session!");

		// by Sergio: check if transaction was passed in constructor...
		if (transaction == null) {
			transaction = session.getTransaction();
		}
		active = true;
	}

	public void commit() throws Exception {
		if (rolledback)
			throw new IllegalStateException(
					"Tried to commit but transaction is already rolledback!");
		if (!commited && active)
			transaction.commit();
		active = false;
		commited = true;
	}

	public void rollback() throws Exception {
		if (commited)
			throw new IllegalStateException(
					"Tried to rollback but transaction is already commited!");
		if (!rolledback && active)
			transaction.rollback();
		active = false;
		rolledback = true;
	}

	public boolean isActive() {
		return active;
	}

	public boolean wasCommited() {
		return commited;
	}

	public boolean wasRolledBack() {
		return rolledback;
	}

	public EntityManager getSession() {
		return session;
	}

	public EntityTransaction getTransaction() {
		return transaction;
	}
}