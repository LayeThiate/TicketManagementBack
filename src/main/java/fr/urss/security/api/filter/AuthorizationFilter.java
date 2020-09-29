package fr.urss.security.api.filter;

import fr.urss.security.exception.AccessDeniedException;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

/**
 * Role authorisation filter.
 *
 * @author lucas.david
 */
@Dependent
@Priority(Priorities.AUTHORIZATION)
@Provider
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext context) {
        var method = resourceInfo.getResourceMethod();

        /* @DenyAll on the method takes precedence over @RolesAllowed and @PermitAll */
        if (method.isAnnotationPresent(DenyAll.class))
            throw new AccessDeniedException("You don't have permissions to perform this action.");

        /* @RolesAllowed on the method takes precedence over @PermitAll. */
        Optional.ofNullable(method.getAnnotation(RolesAllowed.class))
                .ifPresent(rolesAllowed -> performAuthorization(rolesAllowed.value(), context));

        /* @PermitAll on the method takes precedence over @RolesAllowed on the class. */
        if (method.isAnnotationPresent(PermitAll.class)) {
            if (!isAuthenticated(context))
                throw new AccessDeniedException("Authentication is required to perform this action.");
            return;
        }

        /* @DenyAll can't be attached to classes. */

        /* @RolesAllowed on the class takes precedence over @PermitAll on the class. */
        Optional.ofNullable(resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class))
                .ifPresent(rolesAllowed -> performAuthorization(rolesAllowed.value(), context));

        /* @PermitAll on the class. */
        if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            if (!isAuthenticated(context))
                throw new AccessDeniedException("Authentication is required to perform this action.");
        }

        /* Authentication is not required for non-annotated methods. */
        /* if (!isAuthenticated(context))
            throw new AccessDeniedException("Authentication is required to perform this action."); */
    }

    /**
     * Perform authorization based on roles.
     *
     * @param rolesAllowed
     * @param context
     */
    private void performAuthorization(final String[] rolesAllowed, final ContainerRequestContext context) {
        if (rolesAllowed.length > 0 && !isAuthenticated(context))
            throw new AccessDeniedException("Authentication is required to perform this action.");

        for (final String role : rolesAllowed)
            if (context.getSecurityContext().isUserInRole(role)) return;

        throw new AccessDeniedException("You don't have permissions to perform this action.");
    }

    /**
     * Check if the user is authenticated.
     *
     * @param context
     * @return true if user is authenticated, false otherwise.
     */
    private boolean isAuthenticated(final ContainerRequestContext context) {
        return context.getSecurityContext().getUserPrincipal() != null;
    }

}
