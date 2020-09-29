package fr.urss.company.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.urss.common.domain.Address;
import fr.urss.user.domain.Customer;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private long identifier;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "ceo_name", nullable = false)
    private String CEOName;

    @Column(name = "ceo_email", nullable = false)
    private String CEOEmail;

    @JoinColumn(name = "headquarter")
    @ManyToOne(cascade = CascadeType.ALL)
    private Address headquarter;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private Set<Customer> employees;

    @Column(nullable = false)
    private boolean archived = false;

    public Company() {
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCEOName() {
        return CEOName;
    }

    public void setCEOName(String CEOName) {
        this.CEOName = CEOName;
    }

    public String getCEOEmail() {
        return CEOEmail;
    }

    public void setCEOEmail(String CEOEmail) {
        this.CEOEmail = CEOEmail;
    }

    public Address getHeadquarter() {
        return headquarter;
    }

    public void setHeadquarter(Address headquarter) {
        this.headquarter = headquarter;
    }

    public Set<Customer> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Customer> employees) {
        this.employees = employees;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public String toString() {
        return name + " based at " + headquarter;
    }

    @Override
    public boolean equals(Object object) {
        if (Objects.equals(this, object)) return true;
        if (object instanceof Company) {
            final Company company = (Company) object;
            return Objects.equals(name, company.name);
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
