package org.futurepages.core.persistence.transaction;

import java.sql.Connection;

public class JdbcTransaction implements Transaction {
    
    private boolean active = false;
    private boolean commited = false;
    private boolean rolledback = false;
    
    private Connection conn;
    private boolean oldAutoCommit;
    
    public JdbcTransaction() { }
    
    public JdbcTransaction(Connection conn) {
        this.conn = conn;
    }
    
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
    
    public void begin() throws Exception {
        if (active || commited || rolledback) throw new IllegalStateException("Cannot begin transaction again!");
        if (conn == null) throw new IllegalStateException("JdbcTransaction does not have a connection!");
        oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        active = true;
    }
    
    public void commit() throws Exception {
    	if (rolledback) throw new IllegalStateException("Tried to commit but transaction is already rolledback!");
        if (!commited && active) conn.commit();
        commited = true;
        active = false;
        if (conn!= null) conn.setAutoCommit(oldAutoCommit);
    }
    
    public void rollback() throws Exception {
    	if (commited) throw new IllegalStateException("Tried to rollback but transaction is already commited!");
        if (!rolledback && active) conn.rollback();
        rolledback = true;
        active = false;
        if (conn != null) conn.setAutoCommit(oldAutoCommit);
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
}

        
        