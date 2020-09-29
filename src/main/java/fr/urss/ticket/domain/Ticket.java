package fr.urss.ticket.domain;

import fr.urss.common.domain.Skill;
import fr.urss.company.domain.Company;
import fr.urss.user.domain.Customer;
import fr.urss.user.domain.Operator;
import fr.urss.user.domain.Technician;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private long identifier;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.Opened;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @JoinColumn(name = "category")
    @ManyToOne
    private Category category;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @Temporal(TemporalType.DATE)
    @Column(name = "opened_on", nullable = false)
    private Date openedOn = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "last_modified_on", nullable = false)
    private Date lastModifiedOn = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "closed_on")
    private Date closedOn;

    private String description;

    private boolean archived = false;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"ticket_id", "skill_id"}))
    private Set<Skill> requiredSkills;

    /* Associed fields. */
    @JoinColumn(name = "claimant")
    @ManyToOne
    private Customer claimant;

    @JoinColumn(name = "company")
    @ManyToOne
    private Company company;

    @JoinColumn(name = "editor")
    @ManyToOne
    private Operator editor;

    @JoinTable(joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ticket_id"}))
    @ManyToMany
    private Set<Technician> assignedTechnicians;

    public Ticket() {}

    public long getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getOpenedOn() {
        return openedOn;
    }

    public void setOpenedOn(Date openedOn) {
        this.openedOn = openedOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Customer getClaimant() {
        return claimant;
    }

    public void setClaimant(Customer claimer) {
        this.claimant = claimer;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Operator getEditor() {
        return editor;
    }

    public void setEditor(Operator editor) {
        this.editor = editor;
    }

    public Set<Technician> getAssignedTechnicians() {
        return assignedTechnicians;
    }

    public void setAssignedTechnicians(Set<Technician> assignedTechnicians) {
        this.assignedTechnicians = assignedTechnicians;
    }

    @Override /* accurate, but lazy */ public boolean equals(Object object) {
        if (Objects.equals(this, object)) return true;
        if (object instanceof Ticket) {
            Ticket ticket = (Ticket) object;
            return Objects.equals(identifier, ticket.identifier);
            /* return Objects.equals(title, fr.urss.ticket.title) && Objects.equals(type, fr.urss.ticket.type)
                    && Objects.equals(openedOn, fr.urss.ticket.openedOn); // ... */
        }
        return false;
    }

    @Override /* accurate, but lazy */ public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "Ticket [identifier=" + identifier + ", title=" + title + ", type=" + type + ", priority=" + priority +
                ", openedOn=" + openedOn + ", lastModifiedOn=" + lastModifiedOn + ", closedOn=" + closedOn +
                ", description=" + description + "]";
    }

}

