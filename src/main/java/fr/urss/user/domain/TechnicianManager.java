package fr.urss.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "technician_manager")
public class TechnicianManager extends Technician {

    @JoinColumn(name = "supervised_technician")
    @ManyToOne
    private Technician supervisedTechnician;

    public TechnicianManager() {}

    @JsonIgnoreProperties({"identifier", "authorities", "active", "email", "bornOn", "hiredOn", "phone", "address",
                           "skills",
                           "supervisedTechnician"}) /* to avoid infinite loops. */ public Technician getSupervisedTechnician() {
        return supervisedTechnician;
    }

    @JsonIgnore
    public void setSupervisedTechnician(Technician toSupervise) {
        supervisedTechnician = toSupervise;
    }
}
