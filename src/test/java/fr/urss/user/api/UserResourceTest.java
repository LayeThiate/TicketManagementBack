package fr.urss.user.api;


import fr.urss.ArquillianTest;
import fr.urss.security.domain.Authority;
import fr.urss.user.domain.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for the user resource class.
 *
 * @author lucas.david
 */
@RunWith(Arquillian.class)
public class UserResourceTest extends ArquillianTest {

    private static final String Path = "api/u";

    @Test
    public void getUsersAsAnonymous() {
        var response = client.target(uri).path(Path).request().get();

        // assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsOperator() {
        var authorizationHeader = composeAuthorizationHeader(getTokenForOperator());
        var response = client.target(uri).path(Path).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsAdministrator() {
        var authorizationHeader = composeAuthorizationHeader(getTokenForAdministrator());
        var response = client.target(uri).path(Path).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        var queryDetailsList = response.readEntity(new GenericType<List<User>>() {});

        assertNotNull(queryDetailsList);
        assertThat(queryDetailsList, hasSize(3));
    }

    /*
    @Test
    public void getUserAsAnonymous() {
        long userId = 1L;

        Response response = client.target(uri).path(Path).path(Long.toString(userId)).request().get();
        // assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    } */

    @Test
    public void getUserAsOperator() {
        long userId = 1L;

        String authorizationHeader = composeAuthorizationHeader(getTokenForOperator());

        Response response = client.target(uri).path(Path).path(Long.toString(userId)).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsAdministrator() {
        long userId = 1L;

        var authorizationHeader = composeAuthorizationHeader(getTokenForAdministrator());
        var response = client.target(uri).path(Path).path(Long.toString(userId)).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        var result = response.readEntity(User.class);

        assertNotNull(result);
        assertEquals(userId, result.getIdentifier());
    }

    /*
    @Test
    public void getAuthenticatedUserAsAnonymous() {
        var response = client.target(uri).path(Path).path("me").request().get();

        // assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        var user = response.readEntity(User.class);

        //assertEquals("anonymous", user.getUsername());
        //assertThat(user.getAuthorities(), is(empty()));
    } */

    @Test
    public void getAuthenticatedUserAsAdministrator() {
        var authorizationHeader = composeAuthorizationHeader(getTokenForAdministrator());
        var response = client.target(uri).path(Path).path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        var user = response.readEntity(User.class);
        System.out.println(user.getIdentifier());
        System.out.println(user.getUsername());

        assertThat(user.getAuthorities(), containsInAnyOrder(Authority.Administrator));
    }
}
