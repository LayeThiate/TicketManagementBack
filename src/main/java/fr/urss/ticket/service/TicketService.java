package fr.urss.ticket.service;

import fr.urss.common.service.Service;
import fr.urss.ticket.domain.Category;
import fr.urss.ticket.domain.Ticket;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

/**
 * Service that provides operations for {@link Ticket}s.
 *
 * @author lucas.david
 */
@ApplicationScoped
public class TicketService extends Service {

    /**
     * Finds a {@link Ticket} by id.
     *
     * @param identifier
     * @return an optional {@link Ticket} corresponding to the given identifier.
     */
    public Optional<Ticket> findById(long identifier) {
        return Optional.ofNullable(manager.find(Ticket.class, identifier));
    }

    /**
     * Find all {@link Ticket}s.
     *
     * @return
     */
    public List<Ticket> findAll(MultivaluedMap<String, String> filter) {

        JSONObject json = null;
        for (String s : filter.keySet()) {
            json = new JSONObject(s);
            break;
        }
        System.out.println(json);
        if (json.isEmpty()) return findAll();

        String query = "SELECT t FROM Ticket t WHERE ";
        if (!json.isNull("compagnyId")) query += "t.company = '" + json.get("compagnyId") + "' AND ";
        // TODO
        if (!json.isNull("technicianId")) query += "";
        if (!json.isNull("start")) query += " t.opened_on = '" + json.get("technicianId") + "' AND ";
        if (!json.isNull("end")) query += " t.closed_on = '" + json.get("end") + "' AND ";
        if (!json.isNull("categoryId")) query += " t.category = '" + json.get("categoryId").toString() + "' AND ";
        if (!json.isNull("statut")) query += " t.status = '" + json.get("statut") + "' AND ";
        if (!json.isNull("type")) query += " t.type = '" + json.get("type").toString() + "' AND ";
        if (!json.isNull("affected"))
            // TODO
            query += "";
        if (!json.isNull("priority")) query += " t.priority = '" + json.get("priority") + "' AND";

        int l = query.length();
        query = query.substring(0, l - 4);
        System.out.println(query);

        return manager.createQuery(query, Ticket.class).getResultList();
    }

    /**
     * @return
     */
    public List<Ticket> findAll() {
        return doTransaction(() -> manager.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList());
    }

    /**
     * Creates a new {@link Ticket}.
     *
     * @return
     */
    public long create(Ticket ticket) {
        ticket.setCategory(createCategory(ticket.getCategory().getName()));
        return doTransaction(() -> {
            manager.persist(ticket);
            manager.flush();
            return ticket.getIdentifier();
        });
    }

    /**
     * Updates a {@link Ticket}.
     *
     * @param ticket
     * @throws PersistenceException
     */
    public void update(Ticket ticket) {
        doTransaction(() -> manager.merge(ticket));
    }

    /**
     * Deletes a {@link Ticket}.
     *
     * @param ticket
     */
    public void delete(Ticket ticket) {
        doTransaction(() -> {
            ticket.setArchived(true);
            return null;
        });
    }

    public Category createCategory(String name) {
        return doTransaction(() -> {
            var category = manager.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                                  .setParameter("name", name).getSingleResult();
            manager.persist(category);
            manager.flush();
            return category;
        });
    }

    /**
     * @return
     */
    public List<Category> findAllCategories() {
        return doTransaction(() -> manager.createQuery("SELECT c FROM Category c", Category.class).getResultList());
    }

}
