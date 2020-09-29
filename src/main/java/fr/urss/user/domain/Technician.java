package fr.urss.user.domain;

import fr.urss.common.domain.Skill;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Technician extends Operator {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "user_id", "skill_id"}))
    private Set<Skill> skills;

    public Technician() {
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
}
