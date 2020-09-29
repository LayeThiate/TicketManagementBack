package fr.urss.security.domain;

import fr.urss.user.domain.User;

import java.util.Set;

/**
 * Represents an authority granted to a {@link User}.
 *
 * @author lucas.david
 */
public enum Authority {
    Administrator(false), Customer(true), Operator(true), Technician(true), TechnicianManager(true,
                                                                                              Authority.Technician);

    private final Set<Authority> dependencies;
    private final boolean hasTable;

    Authority(boolean hasTable) {
        this.hasTable = hasTable;
        this.dependencies = Set.of();
    }

    Authority(boolean hasTable, Authority... dependencies) {
        this.hasTable = hasTable;
        this.dependencies = Set.of(dependencies);
    }

    public Set<Authority> dependencies() {
        return dependencies;
    }

    public boolean hasTable() {
        return hasTable;
    }


}
