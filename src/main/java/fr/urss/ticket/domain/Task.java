package fr.urss.ticket.domain;

import fr.urss.annotations.Prohibited;

import javax.persistence.*;

// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Task /* extends Ticket */ {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Ticket task;
    @Column(name = "\"order\"")
    private int order; /* 0 ≤ order < n */
    private int weight;

    @Prohibited
    public Task() {
    }

    public Ticket get() {
        return task;
    }

    public void set(Ticket task) {
        this.task = task;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
