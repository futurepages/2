package org.futurepages.core.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import org.futurepages.core.pagination.PaginationSlice;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class Dao extends HQLProvider {

    public static SQLQuery createSQLQuery(String sql) {
        return Dao.session().createSQLQuery(sql);
    }

    private static int calcNumPages(final long numRows, int pageSize) {
        int totalPages = (int) Math.ceil(numRows / (double) pageSize);
        return totalPages;
    }

    public static Session session() {
        return HibernateManager.getSession();
    }
    
    public static void clearSession() {
        Session session = session();
		if (session.isDirty()) {        	
            session.clear();
        }
    }

    public static void open() {
        if (!session().isOpen()) {        	
            session().getSessionFactory().openSession();
        }
    }

    public static void close() {
        if (session().isOpen()) {
            session().close();
        }
    }

    public static Criteria createCriteria(Class entityClass) {
        return session().createCriteria(entityClass);
    }

    public static String getIdName(Class entity) {
        return session().getSessionFactory().getClassMetadata(entity).getIdentifierPropertyName();
    }

    public static Class getIdType(Class entity) {
        return session().getSessionFactory().getClassMetadata(entity).getIdentifierType().getClass();

    }

    public static boolean isTransactionActive() {
        return session().getTransaction().isActive();
    }

    public static Object uniqueResult(String function, Class entity, String where) {
        Object obj = session().createQuery(select(function) + from(entity) + where(where)).uniqueResult();
        return obj;
    }

    public static Object getMinField(String field, Class entity, String where) {
        Object result = uniqueResult(min(field), entity, where);
        return result;
    }

    public static Object getMaxField(String field, Class entity, String where) {
        Object result = uniqueResult(max(field), entity, where);
        return result;
    }

    public static long getNextLong(String field, Class entity) {
        Long newId = (Long) uniqueResult(max(field) + "+1", entity, null);
        if (newId != null) {
            return newId;
        }
        return 1;
    }

    public static Object updateField(Class entity, String field, String expression, String whereClause) {
        session().createQuery(updateSetting(entity) + field + EQUALS + expression + where(whereClause)).executeUpdate();
        return session().createQuery(select(field) + from(entity) + where(whereClause)).uniqueResult();
    }

    public static Object incrementField(Class entity, String field, String whereClause, Integer quantidade) {
        return updateField(entity, field, field + "+(" + quantidade + ")", whereClause);
    }

    public static Object decrementField(Class entity, String field, String whereClause, Integer quantidade) {
        return updateField(entity, field, field + "-(" + quantidade + ")", whereClause);
    }

    public static Object incrementField(Class entity, String field, String whereClause) {
        return Dao.incrementField(entity, field, whereClause, 1);
    }

    public static Object decrementField(Class entity, String field, String whereClause) {
        return updateField(entity, field, field + "-1", whereClause);
    }

    public static long getNextLongId(Class entity) {
        Long newId = (Long) uniqueResult(max(getIdName(entity)) + "+1", entity, null);
        if (newId != null) {
            return newId;
        }
        return (long) 1;
    }

    public static int getNextIntId(Class entity) {
        Integer newId = (Integer) uniqueResult(max(getIdName(entity)) + "+1", entity, null);
        if (newId != null) {
            return newId;
        }
        return (int) 1;
    }

    public static <T extends Serializable> List<T> list(Class<T> entity) {
        return list(entity, null);
    }

    public static <T extends Serializable> List<T> listWithJoin(Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
        return listWithJoin(entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
    }

    public static <T extends Serializable> List<T> listWithJoin(String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
        String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
        Query query = session().createQuery(select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
        return query.list();
    }

    public static <T extends Serializable> List<T> list(Class<T> entity, String whereClause, String... orderClauses) {
        return list(from(entity) + where(whereClause) + orderBy(orderClauses));
    }

    public static <T extends Serializable> List<T> list(String hqlQuery) {
        Query query = session().createQuery(hqlQuery);
        return (List<T>) query.list();
    }

    private static int calcPage(int page, int totalPages) {
        if (page > totalPages) {
            page = totalPages;
        }
        return page;
    }

    public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, Class<T> entity, String whereClause, String... orderClauses) {
        final long numRows = Dao.numRows(entity, whereClause);
        int totalPages = calcNumPages(numRows, pageSize);
        page = calcPage(page, totalPages);
        List<T> list = listPage(page, pageSize, from(entity) + where(whereClause) + orderBy(orderClauses));
        return new PaginationSlice<T>(numRows, pageSize, totalPages, page, list);
    }

    public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize,String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
        final long numRows = Dao.numRows(fromAndJoin, whereClause);
        int totalPages = calcNumPages(numRows, pageSize);
        page = calcPage(page, totalPages);
        List<T> list = listPage(page, pageSize, select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
        return new PaginationSlice<T>(numRows, pageSize, totalPages, page, list);
    }

    public static <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
        return paginationSliceWithJoin(page, pageSize, entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
    }

    public static <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
        String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
        final long numRows = Dao.numRows(fromAndJoin, whereClause);
        int totalPages = calcNumPages(numRows, pageSize);

        page = calcPage(page, totalPages);
        List<T> list = listPage(page, pageSize, select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
        return new PaginationSlice<T>(numRows, pageSize, totalPages, page, list);
    }

    private static <T> String fromAndJoin(String entityAlias, Class<T> entity, String joinClause) {
        return from(entity) + as(entityAlias) + (joinClause != null ? join(joinClause) : "");
    }

    public static <T extends Serializable> List<T> listPage(int page, int pageSize, String hqlQuery) {
        Query query = session().createQuery(hqlQuery);
        query.setFirstResult((page * pageSize) - pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    /* TOP LIST*/
    public static <T extends Serializable> List<T> topListWithJoin(int topSize, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
        return topListWithJoin(topSize, entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
    }

    public static <T extends Serializable> List<T> topListWithJoin(int topSize, String entityAlias, Class<T> entity, String joinClause, String where, String... order) {
        String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
        String strQuery = select(entityAlias) + fromAndJoin + where(where) + orderBy(order);
        Query query = session().createQuery(strQuery);
        query.setMaxResults(topSize);
        return query.list();

    }

    public static <T extends Serializable> List<T> topList(int topSize, String entityAlias, String fromAndJoin, String where, String... order) {
        String strQuery = select(entityAlias) + fromAndJoin + where(where) + orderBy(order);
        Query query = session().createQuery(strQuery);
        query.setMaxResults(topSize);
        return query.list();

    }


    public static <T extends Serializable> List<T> topList(int topSize, Class<T> entity, String where, String... order) {
        Query query = session().createQuery(from(entity) + where(where) + orderBy(order));        
        query.setMaxResults(topSize);
        return query.list();
    }

    public static <T extends Serializable> List<T> topList(int topSize, String hqlQuery) {
        Query query = session().createQuery(hqlQuery);
        query.setMaxResults(topSize);
        return query.list();
    }

    public static <T extends Serializable> List<T> listReports(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
        Query query = session().createQuery(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        return query.list();
    }

    public static <T extends Serializable> T getReport(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
        Query query = session().createQuery(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        return (T) query.uniqueResult();
    }

    public static <T extends Serializable> LinkedHashMap map(Class<T> entity, String key, String value, String where, String... order) {
        Query query = session().createQuery(select(key + "," + value) + from(entity) + where(where) + orderBy(order));
        List<Object[]> results = query.list();
        LinkedHashMap map = new LinkedHashMap();
        for (Object[] result : results) {
            map.put(result[0], result[1]);
        }
        return map;
    }

    public static <T extends Serializable> LinkedHashMap mapGrouped(Class<T> entity, String key, String value, String where, String... order) {
        Query query = session().createQuery(select(key + "," + value) + from(entity) + where(where) + groupBy(key) + orderBy(order));
        List<Object[]> results = query.list();
        LinkedHashMap map = new LinkedHashMap();
        for (Object[] result : results) {
            map.put(result[0], result[1]);
        }
        return map;
    }

    public static <T extends Serializable> Map mapGrouped(Class<T> entity, String entityAlias, String join, String key, String value, String where) {
        Query query = session().createQuery(select(key + "," + value) + from(entity) + as(entityAlias) + join(join) + where(where) + groupBy(key));
        List<Object[]> results = query.list();
        Map map = new HashMap();
        for (Object[] result : results) {
            map.put(result[0], result[1]);
        }
        return map;
    }

    public static Object reportTotal(Class entity, String functions, String where, Class reportClass) {
        Query query = session().createQuery(select(functions) + from(entity) + where(where));
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        return query.uniqueResult();
    }

    public static <T extends Serializable> List<T> reportPage(int page, int pageSize, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
        Query query = session().createQuery(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        query.setFirstResult((page * pageSize) - pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    public static <T extends Serializable> List<T> topReportWithJoin(int topSize, Class entity,
            Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {

        return topReportWithJoin(topSize, entity.getSimpleName().toLowerCase(), entity,
                reportClass, fields, joinClause, whereClause, group, orderClauses);
    }

    public static <T extends Serializable> List<T> topReportWithJoin(int topSize, String entityAlias, Class entity,
            Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {

        String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
        String strQuery = select(fields) + fromAndJoin + where(whereClause) + groupBy(group) + orderBy(orderClauses);
        Query query = session().createQuery(strQuery);
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        query.setMaxResults(topSize);
        return query.list();
    }

    public static <T extends Serializable> List<T> topReport(Integer topSize, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
        Query query = session().createQuery(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
        query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
        if (topSize != null) {
            query.setMaxResults(topSize);
        }
        return query.list();
    }

    public static <T extends Serializable> List<T> report(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
        return topReport(null, entity, reportClass, fields, where, group, order);
    }

    public static long numRows(Class entity, String whereClause) {
        Long res = (Long) session().createQuery(select(count("*")) + from(entity) + where(whereClause)).uniqueResult();
        return res.longValue();
    }

    public static long numRows(String fromAndJoins, String whereClause) {
        Long res = (Long) session().createQuery(select(count("*")) + fromAndJoins + where(whereClause)).uniqueResult();
        return res.longValue();
    }

    public static long numRows(String where, String group, Class entity) {
        Long res = (Long) session().createQuery(select(count(distinct(group))) + from(entity) + where(where)).uniqueResult();
        return res.longValue();
    }

    /**
     * /////////////////////////////////////////////////
     * M�TODOS TRANSACIONAIS
     * /////////////////////////////////////////////////
     */
    public static void beginTransaction() {
    	System.out.println("Dao.beginTransaction()");
        session().getTransaction().begin();
    }

    public static void rollBackTransaction() {
        session().getTransaction().rollback();
    }

    public static void commitTransaction() {
    	System.out.println("Dao.commitTransaction()");
        session().getTransaction().commit();
    }

    public static <T extends Serializable> T saveTransaction(T obj) {
        Dao.beginTransaction();
        session().save(obj);
        Dao.commitTransaction();
        return obj;
    }

    public static <T extends Serializable> Collection<T> saveTransaction(Collection<T> objs) {
        Dao.beginTransaction();
        for (T obj : objs) {
            save(obj);
        }
        Dao.commitTransaction();
        return objs;
    }

    public static <T extends Serializable> T updateTransaction(T obj) {
        Dao.beginTransaction();
        session().update(obj);
        Dao.commitTransaction();
        return obj;
    }

    public static <T extends Serializable> T saveOrUpdateTransaction(T obj) {
        Dao.beginTransaction();
        session().saveOrUpdate(obj);
        Dao.commitTransaction();
        return obj;
    }

    public static <T extends Serializable> T deleteTransaction(T obj) {
        Dao.beginTransaction();
        session().delete(obj);
        Dao.commitTransaction();
        return obj;
    }

    public static <T extends Serializable> void deleteTransaction(Collection<T> objs) {
        Dao.beginTransaction();
        for (T obj : objs) {
            delete(obj);
        }
        Dao.commitTransaction();
    }

    public static <T extends Serializable> T save(T obj) {
        session().save(obj);
        return obj;
    }

    public static <T extends Serializable> void save(T[] arr) {
        for (T obj : arr) {
            save(obj);
        }
    }

    public static <T extends Serializable> void save(Collection<T> objs) {
        for (T obj : objs) {
            save(obj);
        }
    }

    public static <T extends Serializable> T update(T obj) {
        session().update(obj);
        return obj;
    }

    public static <T extends Serializable> void update(T[] arr) {
        for (T obj : arr) {
            session().update(obj);
        }
    }

    public static <T extends Serializable> void update(Collection<T> objs) {
        for (T obj : objs) {
            update(obj);
        }
    }

    public static <T extends Serializable> T delete(T obj) {
        session().delete(obj);
        return obj;
    }

    public static <T extends Serializable> void delete(T[] arr) {
        for (T obj : arr) {
            session().delete(obj);
        }
    }

    public static <T extends Serializable> void delete(Collection<T> objs) {
        for (T obj : objs) {
            delete(obj);
        }
    }

    public static <T extends Serializable> T saveOrUpdate(T obj) {
        session().saveOrUpdate(obj);
        return obj;
    }

    public static <T extends Serializable> T get(Class<T> entity, Serializable id) {
        return (T) session().get(entity, id);
    }

    public static <T> T uniqueResult(Class<T> entity, String where) {
        return (T) session().createQuery(from(entity) + where(where)).uniqueResult();

    }

    public static <T> T uniqueResult(String hqlQuery) {
        Query query = session().createQuery(hqlQuery);
        return (T) query.uniqueResult();
    }

    public static <T extends Serializable> T load(Class<T> entity, Serializable id) {
        return (T) session().load(entity, id);
    }

    public static <T extends Serializable> void evict(T obj) {
        session().evict(obj);
    }

    public static <T extends Serializable> void persist(T obj) {
        session().persist(obj);
    }

    public static <T extends Serializable> T refresh(T obj) {
        session().refresh(obj);
        return obj;
    }

    public static <T extends Serializable> T merge(T obj) {
        session().merge(obj);
        return obj;
    }
}
