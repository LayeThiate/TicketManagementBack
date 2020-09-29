package fr.urss.company.service;

import fr.urss.company.domain.Company;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

/**
 * Service that provides operations for {@link Company}(ies).
 *
 * @author lucas.david
 */
@ApplicationScoped
public class CompanyService {

    @Inject
    private EntityManager manager;

    /**
     * Find a {@link Company} by id.
     *
     * @param identifier
     * @return an optional {@link Company} corresponding to the given identifier.
     */
    public Optional<Company> findById(long identifier) {
        return Optional.ofNullable(manager.find(Company.class, identifier));
    }

    /**
     * Find a {@link Company} by name.
     *
     * @param name
     * @return an optional {@link Company} corresponding to the given identifier.
     */
    public Optional<Company> findByName(String name) {
        return Optional.ofNullable(manager.createQuery("SELECT c FROM Company c WHERE c.name = :name", Company.class)
                                          .setParameter("name", name).getSingleResult());
    }

    /**
     * Find all {@link Company}(ies).
     *
     * @return
     */
    public List<Company> findAll() {
        return manager.createQuery("SELECT c FROM Company c WHERE c.archived = FALSE", Company.class).getResultList();
    }

    /**
     * Find all {@link Company}(ies) deleted.
     *
     * @return
     */
    public List<Company> findArchived() {
        return manager.createQuery("SELECT c FROM Company c WHERE c.archived = TRUE", Company.class).getResultList();
    }

    /**
     * Creates a new {@link Company}.
     *
     * @return
     */
    public long create(Company company) {
        try {
            manager.getTransaction().begin();
            manager.persist(company);
            manager.flush();
            return company.getIdentifier();
        } catch (PersistenceException e) {
            manager.getTransaction().rollback();
            throw e; /* or return 0; */
        } finally {
            manager.getTransaction().commit();
        }
    }

    /**
     * Updates a {@link Company}.
     *
     * @param company
     */
    public void update(Company company) {
        try {
            manager.getTransaction().begin();
            manager.merge(company);
        } catch (PersistenceException e) {
            manager.getTransaction().rollback();
            throw e; /* or return 0; */
        } finally {
            manager.getTransaction().commit();
        }
    }

    /**
     * Deletes a {@link Company}.
     *
     * @param company
     */
    public void delete(Company company) {
        try {
            manager.getTransaction().begin();
            company.setArchived(true);
        } catch (PersistenceException e) {
            manager.getTransaction().rollback();
            throw e; /* or return 0; */
        } finally {
            manager.getTransaction().commit();
        }
    }

}
