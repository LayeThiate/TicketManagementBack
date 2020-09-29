package fr.urss.security.domain;

import fr.urss.user.domain.User;

import javax.persistence.ManyToOne;

//@Entity(name = "technician_details")
public class TechnicianManagerDetails extends AuthorityDetails {

    @ManyToOne
    private User assignedTechnician;

    public TechnicianManagerDetails() {
    }

}
