package fr.urss.common.service;

import fr.urss.common.Function;
import fr.urss.common.Procedure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@ApplicationScoped
public class Service {

    @Inject
    protected EntityManager manager;

    protected <T> T doTransaction(Function<T> transaction) {
        var activeTransaction = manager.getTransaction().isActive();
        try {
            if (!activeTransaction) manager.getTransaction().begin();
            return transaction.run();
        } catch (PersistenceException e) {
            if (!activeTransaction) manager.getTransaction().rollback();
            throw e;
        } finally {
            if (!activeTransaction) manager.getTransaction().commit();
        }
    }

    protected void doTransaction(Procedure transaction) {
        var activeTransaction = manager.getTransaction().isActive();
        try {
            if (!activeTransaction) manager.getTransaction().begin();
            transaction.run();
        } catch (PersistenceException e) {
            if (!activeTransaction) manager.getTransaction().rollback();
            throw e;
        } finally {
            if (!activeTransaction) manager.getTransaction().commit();
        }
    }

}
