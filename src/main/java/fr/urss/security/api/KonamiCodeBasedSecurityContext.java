package fr.urss.security.api;

import fr.urss.security.domain.Authority;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * {@link SecurityContext} implementation for Konami Code authentication.
 *
 * @author lucas.david
 */
public class KonamiCodeBasedSecurityContext implements SecurityContext {

    private AuthenticatedUserDetails authenticatedUserDetails;
    private AuthenticationTokenDetails authenticationTokenDetails;
    private final boolean secure;

    public KonamiCodeBasedSecurityContext(AuthenticatedUserDetails authenticatedUserDetails, boolean secure) {
        this.authenticatedUserDetails = authenticatedUserDetails;
        this.authenticationTokenDetails = new AuthenticationTokenDetails.Builder().withId("l33t")
                                                                                  .withUsername("↑↑↓↓←→←→□△")
                                                                                  .withIssuedDate(ZonedDateTime
                                                                                                          .ofInstant(
                                                                                                                  Instant.EPOCH,
                                                                                                                  ZoneOffset.UTC))
                                                                                  .withExpirationDate(ZonedDateTime
                                                                                                              .ofInstant(
                                                                                                                      Instant.ofEpochMilli(
                                                                                                                              Long.MAX_VALUE),
                                                                                                                      ZoneOffset.UTC))
                                                                                  .build();
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return authenticatedUserDetails;
    }

    @Override
    public boolean isUserInRole(String s) {
        return authenticatedUserDetails.getAuthorities().contains(Authority.valueOf(s));
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Konami Code ";
    }

    public AuthenticationTokenDetails getAuthenticationTokenDetails() {
        return authenticationTokenDetails;
    }
}