package org.futurepages.core.persistence.transaction;

/**
 * Defines the behaviour of a transaction.
 *
 * @author Sergio Oliveira
 */
public interface Transaction {
    
    /**
     * Begins the transaction.
     */
    public void begin() throws Exception;
    
    /**
     * Commits the transaction.
     */
    public void commit() throws Exception;
    
    /**
     * Rollbacks the transaction.
     */
    public void rollback() throws Exception;
    
    /**
     * Is the transaction still active, in other words,
     * is it still not commited and not rolledback ?
     *
     * @return true if the transaction was not commited and was not rolledback
     */
    public boolean isActive();
    
    /**
     * Was the transaction successfuly commited?
     *
     * @return true if the transaction was successfuly commited
     */
    public boolean wasCommited();
    
    /**
     * Was the transaction successfuly rolledback?
     *
     * @return true if the transaction was successfuly rolledback
     */
    public boolean wasRolledBack();
}