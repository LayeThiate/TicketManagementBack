package fr.urss.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.urss.common.domain.Address;
import fr.urss.security.domain.Authority;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long identifier;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @CollectionTable(name = "user_authority", joinColumns = {@JoinColumn(name = "user_id")})
    @Column(name = "authority")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

    @Column(nullable = false)
    private boolean active = true;

    /* Personal contact. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    /* Personal details. */
    @Column(name = "born_on")
    @Temporal(TemporalType.DATE)
    private Date bornOn;

    @Column(name = "hired_on")
    @Temporal(TemporalType.DATE)
    private Date hiredOn;

    private String phone;

    @JoinColumn(name = "address_id") /* can be changed to Embeddable. */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    public User() {}

    public long getIdentifier() {
        return identifier;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /* Personal contact. */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* Personal details. */
    public Date getBornOn() {
        return bornOn;
    }

    public void setBornOn(Date bornOn) {
        this.bornOn = bornOn;
    }

    public Date getHiredOn() {
        return hiredOn;
    }

    public void setHiredOn(Date hiredOn) {
        this.hiredOn = hiredOn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /* Overrides */
    @Override /* accurate, but lazy */ public boolean equals(Object object) {
        if (Objects.equals(this, object)) return true;
        if (object instanceof User) {
            final User user = (User) object;
            return Objects.equals(identifier, user.identifier);
			/* return Objects.equals(username, user.username) && Objects.equals(password, user.password)
				&& Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname)
				&& Objects.equals(email, user.email); */
        }
        return false;
    }

    @Override /* accurate, but lazy */ public int hashCode() {
        return Objects.hash(identifier);
        /* return Objects.hash(username, password, firstname, lastname, email); */
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName + " alias \"" + username + "\" <" + email + '>';
    }

}
