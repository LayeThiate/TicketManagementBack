package fr.urss.ticket.domain;

import fr.urss.user.domain.User;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Table
public class TicketHistory {
    @OneToOne
    private User author;
    @OneToMany
    private Ticket modified;
    @OneToOne
    private Ticket old;

    private Date when;
}
