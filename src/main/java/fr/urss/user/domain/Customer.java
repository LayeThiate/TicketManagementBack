package fr.urss.user.domain;

import fr.urss.company.domain.Company;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends User {

    @JoinColumn(name = "employer")
    @ManyToOne
    private Company employer;

    public Customer() {}
}
