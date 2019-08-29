package org.hackathon.cardless.orchestrator.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customer")
public interface CustomerAccountsProxy {
	
	@GET
	@Path("/{custId}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerDetailsById(@PathParam("custId")String customerId);
	
	@GET
	@Path("/mobile/{mobileNo}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerDetailsByMobileNumber(@PathParam("mobileNo")String mobileNo);
	
	@PUT
	@Path("/{custId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerDetailsbyCustomerId(@PathParam(value="custId") String custId,CustomerAccounts custAccs);
}
