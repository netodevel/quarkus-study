package br.com.netodevel.resource;

import br.com.netodevel.model.User;
import br.com.netodevel.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.ok;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response findAll() {
        return ok(userService.listAll()).build();
    }

    @POST
    public Response create(User user) {
        return ok(userService.create(user)).status(201).build();
    }

    @GET
    @Path(value = "/{id}")
    public Response findById(@PathParam("id") Long id) {
        return ok(userService.findById(id)).build();
    }

    @PUT
    @Path(value = "/{id}")
    public Response update(@PathParam("id") Long id, User user) {
        return ok(userService.update(id, user)).build();
    }

    @DELETE
    @Path(value = "/{id}")
    public Response delete(@PathParam("id") Long id) {
        return ok(userService.delete(id)).build();
    }
}
