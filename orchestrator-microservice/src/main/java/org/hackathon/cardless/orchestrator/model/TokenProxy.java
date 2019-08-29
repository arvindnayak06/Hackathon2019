package org.hackathon.cardless.orchestrator.model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tokens")
public interface TokenProxy {
	
	@GET
	@Path("/bank")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateBankToken() ;
}
