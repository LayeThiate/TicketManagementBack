package fr.urss.security.domain;

import fr.urss.common.domain.Skill;

import javax.persistence.*;
import java.util.Set;

//@Entity(name = "technician_details")
public class TechnicianDetails extends AuthorityDetails {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "technician_details_skill", joinColumns = @JoinColumn(name = "details_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "details_id", "skill_id"}))
    private Set<Skill> skills;

    public TechnicianDetails() {
    }

    public Set<Skill> getSkills() {
        return skills;
    }
}
