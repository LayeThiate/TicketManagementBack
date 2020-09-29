package fr.urss.ws.test;

import fr.urss.common.domain.Skill;
import fr.urss.company.domain.Company;
import fr.urss.ticket.domain.Category;
import fr.urss.ticket.domain.Priority;
import fr.urss.ticket.domain.Ticket;
import fr.urss.ticket.domain.Type;
import fr.urss.user.domain.User;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@ApplicationScoped
public class ClientRest {

    static String url = "http://localhost:8080/api";
    static Client client = ClientBuilder.newClient();

    public static void main(String[] args) {

       // createTicket();
        // createUser();
        // createCompany();
    }



    public static void getTicket(long id) {
        WebTarget target = client.target(url + "/ticket/" + id);

        Response resp = target.request(MediaType.APPLICATION_JSON).get();

        System.out.println("Create User status: " + resp.getStatus());

        if (resp == null || resp.getStatus() != 200) System.out.println("Impossible");
        else {
            JSONObject parse = new JSONObject(resp.readEntity(Ticket.class));
            System.out.println(parse);
        }
    }

    public static void getTickets() {
        WebTarget target = client.target(url + "/ticket/list-ticket");

        Response resp = target.request(MediaType.APPLICATION_JSON).get();

        System.out.println("Create User status: " + resp.getStatus());

        if (resp == null || resp.getStatus() != 200) System.out.println("Impossible");
        else {
            JSONObject parse = new JSONObject(resp.readEntity(User.class));
            System.out.println(parse);
        }
    }

    /********************* TEST DES RESSOURCES DE USER ***************************/
    public static void createUser() {
        WebTarget target = client.target(url + "/u");

        // Address ad = new Address("Broglio", "Orsay", "91400");
        //User user = new User();
        // user = new User("layed", "1234", true, "abdou", "diongue", "abdou@urss.fr",
        // "003");
/*
		Response resp = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));*/
        // Response resp = target.request(MediaType.APPLICATION_JSON).post(null);
        // Response resp = target.request(MediaType.APPLICATION_JSON).get();
/*
		System.out.println("Create User status: " + resp.getStatus());

		if (resp == null || resp.getStatus() != 200)
			System.out.println("Impossible");
		else {
			JSONObject parse = new JSONObject(resp.readEntity(User.class));
			System.out.println(parse);
		}*/
    }

    public static void createCompany() {
        WebTarget target = client.target(url + "/c/create-company");

        // Company company = new Company("urss", "chef", "chef@urss.fr", Priority.High,
        // null);

        Company company = new Company();
        Response resp = target.request(MediaType.APPLICATION_JSON)
                              .post(Entity.entity(company, MediaType.APPLICATION_JSON));

        System.out.println("Create Company status: " + resp.getStatus());

        if (resp == null || resp.getStatus() != 200) System.out.println("Impossible");
        else {
            JSONObject parse = new JSONObject(resp.readEntity(Company.class));
            System.out.println(parse);
        }
    }

}
