/*
 * Author: Arvind Nayak
 * Description: JAX-RS endpoint class to receive all REST requests
 */
package org.hackathon.cardless.orchestrator.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hackathon.cardless.orchestrator.model.Credentials;
import org.hackathon.cardless.orchestrator.model.CustomerAccounts;
import org.hackathon.cardless.orchestrator.model.Token;
import org.hackathon.cardless.orchestrator.model.Transaction;
import org.hackathon.cardless.orchestrator.model.TransactionReq;
import org.hackathon.cardless.orchestrator.model.WithdrawCCReq;
import org.hackathon.cardless.orchestrator.utils.Secured;
import org.hackathon.cardless.orchestrator.utils.UtilServices;

import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/")
public class OrchestratorEndpoints {
	
	private Response response;
	@Inject
	private UtilServices uSrvs;
	final static Logger logger = Logger.getLogger(OrchestratorEndpoints.class);

	/*
	 * This endpoint will be invoked by consumer in order to login to the system.
	 */
	@POST
	@Path("auth/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response checkUserCredentials(Credentials credentials) {
		logger.info("orc endpoint method - checkUserCredentials triggered");
		response = uSrvs.resteasyAuthenticateUser(credentials);
		return response;		
	}
	
	
	/*
	 * This endpoint will be invoked by consumer to fetch customer details
	 */
	@GET
	@Secured
	@Path("cust/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchDashboardDetails(@PathParam("id")String customerId) {
		logger.info("orc endpoint method - fetchDashboardDetails triggered");
		response = uSrvs.resteasyGetCustomerDetailsById(customerId);
		return response;
	}
	
	/*
	 * This endpoint will be invoked by the consumer to generate a cardless cash request
	 */
	@POST
	@Secured
	@Path("tx/cardlesscash")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response requestCCTx(TransactionReq txReq) {
		logger.info("orc endpoint method - requestCCTx triggered");
		Token obj = null;
		// generate bank token
		response = uSrvs.resteasyGenerateBankToken();
		
		if(response !=null && response.getStatus() == 200) {
			String str = response.readEntity(String.class);
			
			try {
				ObjectMapper mapper = new ObjectMapper();
				obj = mapper.readValue(str, Token.class);
				logger.info("Token: "+obj.getBankToken());
				
				Transaction tx = new Transaction();
				tx.setCustomerId(txReq.getCustomerId());
				tx.setSortCode(txReq.getSortCode());
				tx.setAccountNumber(txReq.getAccountNumber());
//				tx.setMobileNumber(custAccs.getMobileNumber());
				tx.setTransactionType("CardlessCash");
				tx.setAmount(txReq.getAmount());
				logger.info("set tx field active as yes and status as pending");
				tx.setActive("yes");
				tx.setStatus("pending");
				tx.setBankToken(obj.getBankToken());
//				tx.setUserToken(custAccs.getUserToken());
				// token valid for 1 hour
				tx.setValidTill(Long.toString(System.currentTimeMillis() + 60000l*60));
		
				response = uSrvs.resteasySaveTx(tx);	
				if(response !=null && response.getStatus()/100 == 2) {
					response = Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"bankToken\":\""+obj.getBankToken()+"\"}").build();
				}
			}catch(Exception e) {
				logger.info("General catch all exception: "+e);
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"General exception\"}").build();
				
			}
		}	
		return response;
	}
	
	
	/*
	 * This endpoint will be invoked by the consumer to withdraw money based on an existing cardless cash request.
	 */
	@PUT
	@Secured
	@Path("tx/cardlesscash/withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response withdrawCash(WithdrawCCReq req) {
		
		CustomerAccounts custAccs = null;
		Transaction trans = null;
		logger.info("orc endpoint method - withdrawCash triggered");
		
		try {
			// get customer based on mobile number
			logger.info("Inovking resteasyGetCustByMobile");
			logger.info("mobileNo: "+req.getMobileNumber());
			response = uSrvs.resteasyGetCustomerDetailsByMobileNumber(req.getMobileNumber());
			if(response!=null)
				logger.info("Response from resteasyGetCustByMobile: "+response.getStatus());
			if(response!=null && response.getStatus()==200) {
				
				// read the response json entity as a string 
				String str = response.readEntity(String.class);
			
				// convert the json string into a CustomerAccounts POJO class object
				ObjectMapper mapper = new ObjectMapper();
				custAccs = mapper.readValue(str, CustomerAccounts.class);
				
				// fetch details of the transaction from transaction_db
				logger.info("Inovking resteasyGetTx");
				logger.info("Mobile: "+req.getMobileNumber());
				logger.info("Bank: "+req.getBankToken());
				response = uSrvs.resteasyGetTx(req.getBankToken());

				if(response!=null && response.getStatus()==200) {
					// read the response json entity as a string 
					str = response.readEntity(String.class);
					
					// convert the json string into a Transaction POJO class object
					mapper = new ObjectMapper();
					trans = mapper.readValue(str, Transaction.class);
					
					if(custAccs!=null && trans!=null) {
						// check that the details sent in withdrawal request match against customer record
						if((req.getMobileNumber()).equals(custAccs.getMobileNumber())){
							logger.info("mobile numbers match.");
							if((req.getBankToken()).equals(trans.getBankToken())) {
								logger.info("bank tokens match.");
								if((req.getUserToken()).equals(custAccs.getUserToken())) {
									logger.info("user tokens match.");
									// check status is incomplete and is active
									logger.info("Status: "+trans.getStatus() +" Active: "+trans.getActive());
									if((trans.getStatus()).equals("pending") && (trans.getActive()).equals("yes")) {										
										Boolean isSuccess = uSrvs.findAndUpdateAccBalance(custAccs,trans.getSortCode(),trans.getAccountNumber(),trans.getAmount());
										if(isSuccess) {
											logger.info("Reached acc balance");
											for(int i=0;i<custAccs.getAccounts().length;i++) {
												System.out.println("SortCode: "+custAccs.getAccounts()[i].getSortCode());
												System.out.println(" AcctNum: "+custAccs.getAccounts()[i].getAccountNumber());
												System.out.println(" Balance: "+custAccs.getAccounts()[i].getBalance());
											}
											// update customer record
											logger.info("attempting to update customer document...");
											response = uSrvs.resteasyPutCustomerAccountsDetails(custAccs);
											
											if(response!=null && response.getStatus()/100 == 2) {
												logger.info("customer document successfully updated!!!");
											// update transaction record
												isSuccess = null;
												trans.setActive("no");
												trans.setStatus("completed");
												logger.info("attempting to update transaction document...");
												response = uSrvs.resteasyPutCustomerTransaction(trans);
												if(response!=null && response.getStatus()/100 == 2)
													logger.info("transaction document successfully updated!!!");
											}
											
										}else {
											logger.info("Insufficient balance");
											response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Insufficient balance.\"}").build();
										}
									}else {
										logger.info("transaction status is not incomplete or tx is not active");
										response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Exception while processing withdrawal request.\"}").build();
									}									
								}else {
									logger.info("User Tokens do not match");
									response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"User Tokens do not match\"}").build();
								}
							}else {
								logger.info("Bank Tokens do not match");
								response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Bank Tokens do not match\"}").build();
							}							
						}else {
							logger.info("Mobile numbers do not match");
							response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Mobile numbers do not match\"}").build();
						}									
					}
				}else {
					logger.info("Transaction not found");
					response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Transaction not found\"}").build();
				}
			}
		}catch(Exception e) {
			logger.info("Exception in service: "+e);
			response = Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Error in withdrawCash method - Customer details not found.\"}").build();
			
		}
		
		return response;
	}
	
	
	
	
	// this is not needed at orchestration level but is retained as a helper
	@POST
	@Path("auth/validatejwt")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response validateJWTToken(String jwt) {
		logger.info("Inside method-validateJWTToken");
		response = uSrvs.resteasyValidateJWTToken(jwt);
		
		return response;
		
	}
	
}
