package org.hackathon.cardless.orchestrator.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authz")
public interface AuthenticationProxy {
	
	@POST
	@Path("/user/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Credentials credentials);
	
	@POST
	@Path("/jwt/validate")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response validateJWTToken(String jwt);
	
	/*
	@GET
	@Path("/jwt/generate")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateJWTToken(String username);
	*/
	
	/*
	@GET
	@Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
	public Response test();
	*/
}
