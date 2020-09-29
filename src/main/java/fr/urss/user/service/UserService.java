package fr.urss.user.service;

import fr.urss.common.StringCase;
import fr.urss.common.api.filtering.Filter;
import fr.urss.common.service.Service;
import fr.urss.common.service.SkillService;
import fr.urss.security.domain.Authority;
import fr.urss.ticket.domain.Ticket;
import fr.urss.user.domain.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that provides operations for {@link User}s.
 *
 * @author lucas.david
 */
@ApplicationScoped
public class UserService extends Service {

    @Inject
    private SkillService skillService;

    /**
     * Finds a {@link User} by id.
     *
     * @param identifier
     * @return an optional {@link User} corresponding to the given identifier.
     */
    public Optional<User> findById(long identifier) {
        return Optional.ofNullable(manager.find(User.class, identifier));
    }

    /**
     * Finds a {@link User} by username.
     *
     * @param username
     * @return an optional {@link User} corresponding to the given username.
     */
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(
                    manager.createQuery("SELECT u FROM User u WHERE u.active = TRUE AND u.username = :username",
                                        User.class).setParameter("username", username).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all {@link User}s.
     *
     * @return
     */
    public List<User> findAll() {
        return manager.createQuery("SELECT u FROM User u WHERE u.active = TRUE ", User.class).getResultList();
    }

    /**
     * Creates a new {@link User}.
     *
     * @return
     */
    public long create(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return doTransaction(() -> {
            manager.persist(user);
            manager.flush();
            return user.getIdentifier();
        });
    }

    /**
     * Updates a {@link User}.
     *
     * @param user
     */
    public void update(User user) {
        doTransaction(() -> {
            manager.getTransaction().begin();
            manager.merge(user);
        });
    }

    /**
     * Deletes a {@link User}.
     *
     * @param user
     */
    public void delete(User user) {
        doTransaction(() -> {
            manager.getTransaction().begin();
            user.setActive(false);
        });
    }

    /**
     * Promote {@link User} to a specific role.
     *
     * @param username
     * @param authority
     */
    public void promote(String username, Authority authority) {
        doTransaction(() -> {
            var user = findByUsername(username).orElseThrow(EntityNotFoundException::new);
            manager.getTransaction().begin();

            /* early quit. */
            if (user.getAuthorities().contains(authority)) return;

            /* e.g. if promoting to TechnicianManager and user is not Technician, it will promote to Technician first. */
            var toPromoteBefore = authority.dependencies().stream().filter(a -> user.getAuthorities().contains(a))
                                           .collect(Collectors.toSet());
            for (var a : toPromoteBefore)
                promote(username, a);

            /* if Authority implies that user has additional data. */
            if (authority.hasTable()) manager.createNativeQuery("INSERT INTO :authority (user_id) VALUES (:identifier)")
                                             .setParameter("authority", StringCase.toSnakeCase(authority.toString()))
                                             .setParameter("identifier", user.getIdentifier()).executeUpdate();

            /* adds Authority */
            user.getAuthorities().add(authority);
        });
    }

    /**
     * Demote a {@link User} from a specific role.
     *
     * @param username
     * @param authority
     */
    public void demote(String username, Authority authority) {
        doTransaction(() -> {
            manager.getTransaction().begin();
            var user = findByUsername(username).orElseThrow(EntityNotFoundException::new);

            /* early quit. */
            if (!user.getAuthorities().contains(authority)) return;

            /* e.g. if demoting from Technician and user is also a TechnicianManager, it will demote from TechnicianManager first. */
            var toDemoteBefore = Arrays.stream(Authority.values()).filter(a -> user.getAuthorities().contains(a))
                                       .filter(a -> a.dependencies().contains(authority)).collect(Collectors.toSet());
            for (var a : toDemoteBefore)
                demote(username, a);

            user.getAuthorities().remove(authority); /* soft remove suffisant */
        });
    }

    /** Administrator. */

    /**
     * Finds a {@link Administrator} by id.
     *
     * @param identifier
     * @return an optional {@link Administrator} corresponding to the given identifier.
     */
    public Optional<Administrator> findAdministratorById(long identifier) {
        return Optional.ofNullable(manager.find(Administrator.class, identifier));
    }

    /**
     * Finds a {@link Administrator} by username.
     *
     * @param username
     * @return an optional {@link Administrator} corresponding to the given username.
     */
    public Optional<Administrator> findAdministratorByUsername(String username) {
        try {
            return Optional.ofNullable(manager.createQuery(
                    "SELECT u FROM Administrator u WHERE u.active = TRUE AND u.username = :username",
                    Administrator.class).setParameter("username", username).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /** Operator. */

    /**
     * Finds a {@link Operator} by id.
     *
     * @param identifier
     * @return an optional {@link Operator} corresponding to the given identifier.
     */
    public Optional<Operator> findOperatorById(long identifier) {
        return Optional.ofNullable(manager.find(Operator.class, identifier));
    }

    /**
     * Finds a {@link Operator} by username.
     *
     * @param username
     * @return an optional {@link Operator} corresponding to the given username.
     */
    public Optional<Operator> findOperatorByUsername(String username) {
        try {
            return Optional.ofNullable(
                    manager.createQuery("SELECT u FROM Operator u WHERE u.active = TRUE AND u.username = :username",
                                        Operator.class).setParameter("username", username).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /** Technician. */

    /**
     * Finds a {@link Technician} by id.
     *
     * @param identifier
     * @return an optional {@link Technician} corresponding to the given identifier.
     */
    public Optional<Technician> findTechnicianById(long identifier) {
        return Optional.ofNullable(manager.find(Technician.class, identifier));
    }

    /**
     * Finds a {@link Technician} by username.
     *
     * @param username
     * @return an optional {@link Technician} corresponding to the given username.
     */
    public Optional<Technician> findTechnicianByUsername(String username) {
        try {
            return Optional.ofNullable(
                    manager.createQuery("SELECT u FROM Technician u WHERE u.active = TRUE AND u.username = :username",
                                        Technician.class).setParameter("username", username).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Ticket> findTechnicianTickets(String username) {
        return manager.createQuery(
                "SELECT t FROM Ticket t JOIN Technician tech ON tech MEMBER OF t.assignedTechnicians WHERE tech.username = :username",
                Ticket.class).setParameter("username", username).getResultList();
    }

    public void addSkill(String username, String skill) {
        doTransaction(() -> {
            var user = findTechnicianByUsername(username).orElseThrow(EntityNotFoundException::new);
            user.getSkills().add(skillService.findOrCreate(skill));
        });
    }

    public void removeSkill(String username, String skill) {
        doTransaction(() -> {
            var user = findTechnicianByUsername(username).orElseThrow(EntityNotFoundException::new);
            user.getSkills().remove(skillService.findByName(skill).orElseThrow(EntityNotFoundException::new));
        });
    }

    /** Technician manager. */

    /**
     * Finds a {@link TechnicianManager} by id.
     *
     * @param identifier
     * @return an optional {@link TechnicianManager} corresponding to the given identifier.
     */
    public Optional<TechnicianManager> findTechnicianManagerById(long identifier) {
        return Optional.ofNullable(manager.find(TechnicianManager.class, identifier));
    }

    /**
     * Finds a {@link TechnicianManager} by username.
     *
     * @param username
     * @return an optional {@link TechnicianManager} corresponding to the given username.
     */
    public Optional<TechnicianManager> findTechnicianManagerByUsername(String username) {
        try {
            return Optional.ofNullable(manager.createQuery(
                    "SELECT u FROM TechnicianManager u WHERE u.active = TRUE AND u.username = :username",
                    TechnicianManager.class).setParameter("username", username).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void supervise(String username, String toSuperviseUsername) {
        doTransaction(() -> {
            var user = findTechnicianManagerByUsername(username).orElseThrow(EntityNotFoundException::new);
            user.setSupervisedTechnician(user);
        });
    }

    public void unsupervise(String username) {
        doTransaction(() -> {
            var user = findTechnicianManagerByUsername(username).orElseThrow(EntityNotFoundException::new);
            user.setSupervisedTechnician(null);
        });
    }
}
