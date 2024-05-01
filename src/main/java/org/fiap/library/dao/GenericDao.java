package org.fiap.library.dao;

import org.fiap.library.utils.HibernateDataUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

abstract sealed class GenericDao<T>
        implements AutoCloseable
        permits AuthorDao, BookDao, PublisherDao {
    private T entity;
    public GenericDao() {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        try {
            entity = (T) Class.forName(className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }    protected final Session _session = Objects.isNull(this._session)
            || !this._session.isOpen()
            ? this.openSessionFactory() : this._session;

    private Session openSessionFactory() {
        return HibernateDataUtils
                .getSessionFactory()
                .openSession();
    }

    public void save(T entity) {
        Transaction trans = _session.beginTransaction();
        _session.merge(entity);
        trans.commit();
    }

    @SuppressWarnings("unchecked")
    public T get(T entity) {
        return (T) _session.find(entity.getClass(), entity);
    }

    @SuppressWarnings("unchecked")
    public T getById(String id) {
        var query = _session.createSelectionQuery(String.format("FROM %s t WHERE t.id = :entityId",
                ((Object) entity).getClass().getName()));
        query.setParameter("entityId", id);
        return (T) query.stream().findAny().orElse(null);
    }

    public void delete(T entity) {
        Transaction trans = _session.beginTransaction();
        _session.remove(entity);
        trans.commit();
        ;
    }

    public Set<?> getAll() {
        var query = _session.createQuery(String.format("FROM %s",
                ((Object) entity).getClass().getSimpleName()), entity.getClass());
        return query.stream().collect(Collectors.toSet());
    }

    @Override
    public void close() {
        _session.close();
    }


}
