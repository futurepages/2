package org.futurepages.core.persistence.transaction;

import org.hibernate.Session;

/**
 * @author Rubem Azenha
 */
public class HibernateTransaction implements Transaction {
	
	private Session session = null;
	private org.hibernate.Transaction transaction = null;
    private boolean active = false;
    private boolean commited = false;
    private boolean rolledback = false;
    
    public HibernateTransaction() { }
    
	public HibernateTransaction(Session session) {
        this.session = session;
    }
    
    public HibernateTransaction(Session session, org.hibernate.Transaction transaction) {
        this(session);
        this.transaction = transaction;
        this.active = true;
    }
    
    public void setSession(Session session) {
        this.session = session;
    }
    
    public void setTransaction(org.hibernate.Transaction transaction) {
        this.transaction = transaction;
    }
	
	public void begin() throws Exception {
        if (active || commited || rolledback) throw new IllegalStateException("Cannot begin transaction again!");
        if (session == null) throw new IllegalStateException("HibernateTransaction does not have a hibernate session!");
        
        // by Sergio: check if transaction was passed in constructor...
        if (transaction == null) {
    		transaction = session.beginTransaction();
        }
		active = true;
	}

	public void commit() throws Exception {
		if (rolledback) throw new IllegalStateException("Tried to commit but transaction is already rolledback!");
		if (!commited && active) transaction.commit();
		active = false;
		commited = true;
	}

	public void rollback() throws Exception {
		if (commited) throw new IllegalStateException("Tried to rollback but transaction is already commited!");
		if (!rolledback && active) transaction.rollback();
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

	public Session getSession() {
		return session;
	}

	public org.hibernate.Transaction getTransaction() {
		return transaction;
	}
}