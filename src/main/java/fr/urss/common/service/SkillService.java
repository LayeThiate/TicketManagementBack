package fr.urss.common.service;

import fr.urss.common.domain.Skill;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

/**
 * Service that provides operations for {@link Skill}s.
 *
 * @author lucas.david
 */
@ApplicationScoped
public class SkillService extends Service {

    /**
     * Find a {@link Skill} by id.
     *
     * @param identifier
     * @return an optional {@link Skill} corresponding to the given identifier.
     */
    public Optional<Skill> findById(long identifier) {
        return Optional.ofNullable(manager.find(Skill.class, identifier));
    }

    /**
     * Finds a {@link Skill} by name.
     *
     * @param name
     * @return returns an optional {@link Skill} corresponding to the given username.
     */
    public Optional<Skill> findByName(String name) {
        return Optional.ofNullable(manager.createQuery("SELECT s FROM Skill s WHERE s.name = :name", Skill.class)
                                          .setParameter("name", name).getSingleResult());
    }

    /**
     * Finds or create a {@link Skill}.
     *
     * @param name
     * @return
     */
    public Skill findOrCreate(String name) {
        return findByName(name).orElseGet(() -> doTransaction(() -> {
            var skill = new Skill(name);
            create(skill);
            manager.flush();
            return skill;
        }));
    }

    /**
     * Creates a new {@link Skill}.
     *
     * @return
     * @throws PersistenceException
     */
    public void create(Skill skill) {
        doTransaction(() -> manager.persist(skill));
    }

    public void delete(Skill skill) {
        doTransaction(() -> manager.remove(skill));
    }

    /**
     * Find all {@link Skill}s.
     *
     * @return
     */
    public List<Skill> findAll() {
        return manager.createQuery("SELECT s FROM Skill s", Skill.class).getResultList();
    }

}