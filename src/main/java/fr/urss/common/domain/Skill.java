package fr.urss.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents a {@link Skill}.
 *
 * @author lucas.david
 */
@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private long identifier;
    @Column(unique = true, nullable = false)
    private String name;

    public Skill() {}

    @JsonCreator
    public Skill(String name) {
        this.name = name;
    }

    @JsonValue
    public String get() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
