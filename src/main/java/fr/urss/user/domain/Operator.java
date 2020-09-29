package fr.urss.user.domain;

import fr.urss.ticket.domain.Ticket;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Operator extends User {
    @OneToMany(mappedBy = "editor")
    private Set<Ticket> editedTickets;

    public Operator() {}
}
