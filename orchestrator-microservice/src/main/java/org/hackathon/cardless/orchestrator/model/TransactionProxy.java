package org.hackathon.cardless.orchestrator.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transaction")
public interface TransactionProxy {

	@POST
	@Path("/cardlesscash")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveTx(Transaction tx);
	
	@GET
	@Path("/cardlesscash/query")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTx(@QueryParam("token") String bankToken);
	
	@PUT
	@Path("/cardlesscash/update")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTx(Transaction tx);
}
