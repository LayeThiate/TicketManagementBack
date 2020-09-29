package fr.urss.security.domain;

import javax.persistence.*;

//@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class AuthorityDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "details_id")
    private long identifier;

    public AuthorityDetails() {
    }
}
