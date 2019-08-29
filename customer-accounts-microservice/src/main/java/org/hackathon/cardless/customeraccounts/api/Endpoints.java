/*
 * Author: Arvind Nayak
 * Description: JAX-RS endpoint class to receive all REST requests
 */
package org.hackathon.cardless.customeraccounts.api;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hackathon.cardless.customeraccounts.model.CustomerAccounts;
import org.hackathon.cardless.customeraccounts.service.CloudantQueryService;

import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/customer")
public class Endpoints {	
	@Inject
	private CloudantQueryService db;	
	private CustomerAccounts custAccs;	
	private Response response;	
	final static Logger logger = Logger.getLogger(Endpoints.class);
	
	@GET
	@Path("/{custId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerDetailsById(@PathParam(value="custId") String custId) {
		logger.info("inside getCustomerDetailsById. custId: "+custId);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {	
			logger.info("get record from db");
			custAccs = db.getDetailsByCustomerId(custId,false);
			
			if(custAccs != null) {
				logger.info("record found in db");
				response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(mapper.writeValueAsString(custAccs)).build();
			}
			else {
				logger.info("record not found in db");
				response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"No Customer found for the id "+custId+"\"}").build();
			}			
		}
		catch(JsonProcessingException e) {
			// can be thrown by fasterxml.jackson
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Internal Server Error encountered.\"}").build();
		}
		catch(MalformedURLException e) {
			// can be thrown by cloudant client
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"malformed URL exception while connecting to cloudant.\"}").build();
		}
		
		return response;
	}
	
	@PUT
	@Path("/{custId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerDetailsbyCustomerId(@PathParam(value="custId") String custId,CustomerAccounts custAccs) {
		logger.info("inside updateCustomerDetailsbyCustomerId. custId: "+custId);
		try {
			boolean isSuccess = db.updateCustomerDetails(custAccs);
			logger.info("is success: "+isSuccess);
			if(isSuccess) {
				logger.info("update a success");
				response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).build();
			}else {
				logger.info("update not a success");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Unable to save updates to DB.\"}").build();
			}
		}catch(MalformedURLException e) {
			logger.info("MalformedURLException");
			// can be thrown by cloudant client
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"malformed URL exception while connecting to cloudant.\"}").build();
		}
		catch(DocumentConflictException e) {
			logger.info("DocumentConflictException");
			// can be thrown by cloudant client
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"DocumentConflictException while updating customer details\"}").build();
		}
		
		return response;
	}
	
	@GET
	@Path("/mobile/{mobileNo}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerDetailsByMobileNumber(@PathParam("mobileNo")String mobileNo) {
		logger.info("inside getCustomerDetailsByMobileNumber. mobileNo: "+mobileNo);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {	
			logger.info("get record from db");
			custAccs = db.getDetailsByMobileNumber(mobileNo,true);
			
			if(custAccs != null) {
				logger.info("record found in db");
				response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(mapper.writeValueAsString(custAccs)).build();
			}
			else {
				logger.info("record not found in db");
				response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"No Customer found with mobile number "+mobileNo+"\"}").build();
			}			
		}
		catch(JsonProcessingException e) {
			// can be thrown by fasterxml.jackson
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Internal Server Error encountered.\"}").build();
		}
		catch(MalformedURLException e) {
			// can be thrown by cloudant client
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"malformed URL exception while connecting to cloudant.\"}").build();
		}
		
		return response;
	}	
}
