package org.futurepages.core.persistence;

import java.util.HashSet;
import java.util.Set;
import org.futurepages.filters.ExceptionFilter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.futurepages.core.action.Action;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.exceptions.FilterException;
import org.futurepages.core.input.InputWrapper;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.persistence.transaction.HibernateTransaction;
import org.futurepages.core.persistence.transaction.Transaction;


public class HibernateFilter extends InputWrapper implements AfterConsequenceFilter {



    private String sessionKey = HibernateManager.SESSION_KEY;
    private String transactionKey = HibernateManager.TRANS_KEY;
    private String factoryKey = HibernateManager.FACTORY_KEY;
    private boolean transactional = false;
    
    private Set<String> resultsForRollback = new HashSet<String>();

    public HibernateFilter() {
        
    }

    public HibernateFilter(boolean transactional) {
        setTransactional(transactional);
    }

    public HibernateFilter(SessionFactory sessionFactory, String factoryKey) {
        initKeys(factoryKey);
        HibernateManager.setSessionFactory(factoryKey, sessionFactory);
    }

    public HibernateFilter(SessionFactory sessionFactory, boolean transactional, String factoryKey) {
        this(sessionFactory,factoryKey);
        setTransactional(transactional);
    }

    public void setTransactional(boolean transactional) {

        this.transactional = transactional;

        addResultsForRollback(Action.ERROR, ExceptionFilter.EXCEPTION);
    }

    public void addResultsForRollback(String... results) {

        for (int i = 0; i < results.length; i++) {

            resultsForRollback.add(results[i]);
        }
    }

    public void setTransactional(boolean transactional, String transactionKey) {

        setTransactional(transactional);

        this.transactionKey = transactionKey;
    }

    public void setKey(String key) {
        this.sessionKey = key;
    }

    public String filter(InvocationChain chain) throws Exception {
        Action action = chain.getAction();

        super.setInput(action.getInput());

        action.setInput(this);

        String result = chain.invoke();

        // commit or rollback the transaction BEFORE consequence...

        if (transactional) {

            Transaction trans = HibernateManager.getTransactionTL(factoryKey).get();

            if (trans != null) {
                 HibernateManager.getSessionTL(factoryKey).set(null);

                 removeValue(transactionKey);

                boolean shouldRollback = result == null || resultsForRollback.contains(result);

                if (!shouldRollback) {

                    try {

                        trans.commit();

                    } catch (Exception e) {

                        e.printStackTrace();

                        throw new FilterException("Unable to commit hibernate transaction!", e);
                    }
                } else {

                    try {

                        trans.rollback();

                    } catch (Exception e) {

                        e.printStackTrace();

                        throw new FilterException("Unable to rollback hibernate transaction!", e);
                    }
                }
            }
        }

        return result;
    }

    public void afterConsequence(Action action, Consequence c,
            boolean conseqExecuted, boolean actionExecuted, String result) {

        Session session = HibernateManager.getSessionTL(factoryKey).get();

        if (session != null) {
            HibernateManager.getSessionTL(factoryKey).set(null);
            removeValue(sessionKey);
            if(session.isOpen()){ //colocado para corrigir Exception: Session is already closed.
                session.close();
            }
        }
    }

    private void initKeys(String factoryKey){
        this.factoryKey = factoryKey;
        if(!factoryKey.equals("default")){
            sessionKey = HibernateManager.SESSION_KEY_PREFIX+factoryKey;
        }
    }

    public void destroy() {
        HibernateManager.getSessionFactory(factoryKey).close();
    }

    /**
     * Verifies if the requested object is a Hibernate Session, return a
     * Hibernate Session if it is and return the content of the action's input
     * if not.
     */
    @Override
    public Object getValue(String key) {

        if (key.equals(sessionKey)) {
            Session session = HibernateManager.getSessionTL(factoryKey).get();
            if (session == null) {
                session = HibernateManager.getSessionFactory(factoryKey).openSession();
                HibernateManager.getSessionTL(factoryKey).set(session);

                if (transactional) {

                    org.hibernate.Transaction tx = session.beginTransaction();

                    Transaction trans = new HibernateTransaction(session, tx);

                    HibernateManager.getTransactionTL(factoryKey).set(trans);

                    setValue(transactionKey, trans);

                }

                setValue(key, session);

            }
            return session;
        }

        return super.getValue(key);
    }
}