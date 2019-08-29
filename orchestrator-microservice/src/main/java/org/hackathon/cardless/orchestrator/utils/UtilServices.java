package org.hackathon.cardless.orchestrator.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hackathon.cardless.orchestrator.model.Accounts;
import org.hackathon.cardless.orchestrator.model.AuthenticationProxy;
import org.hackathon.cardless.orchestrator.model.Credentials;
import org.hackathon.cardless.orchestrator.model.CustomerAccounts;
import org.hackathon.cardless.orchestrator.model.CustomerAccountsProxy;
import org.hackathon.cardless.orchestrator.model.TokenProxy;
import org.hackathon.cardless.orchestrator.model.Transaction;
import org.hackathon.cardless.orchestrator.model.TransactionProxy;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class UtilServices {

	private Response response;
	private Client client;
	private ResteasyWebTarget target;
	final static Logger logger = Logger.getLogger(UtilServices.class);
	/*
	private String authBasePath = "http://localhost:8081/api";
	private String custAccsBasePath = "http://localhost:8082/api";
	private String txBasePath = "http://localhost:8083/api";
	private String tokenBasePath = "http://localhost:8084/api";;
	*/
	private String authBasePath = "http://authentication-srv:8081/api";
	private String custAccsBasePath = "http://custaccs-srv:8082/api";
	private String txBasePath = "http://tx-srv:8083/api";
	private String tokenBasePath = "http://token-srv:8084/api";
	
	public UtilServices() {
		
	}
	
	public Response resteasyAuthenticateUser(Credentials credentials) {
		logger.info("Inside resteasyAuthenticateUser method");
		client = ClientBuilder.newClient();
		target = (ResteasyWebTarget) client.target(authBasePath);
		AuthenticationProxy proxy = target.proxy(AuthenticationProxy.class);
		
		response = proxy.authenticateUser(credentials);
		logger.info("response from proxy authenticateUser: "+response);

		return response;		
	}
	
	public Response resteasyGetCustomerDetailsById(String customerId) {
		logger.info("Inside resteasyGetCustomerDetailsById method");
		client = ClientBuilder.newClient();
		logger.info("custAccsBasePath: "+custAccsBasePath);
		target = (ResteasyWebTarget) client.target(custAccsBasePath);
		CustomerAccountsProxy proxy = target.proxy(CustomerAccountsProxy.class);
		
		response = proxy.getCustomerDetailsById(customerId);
		logger.info("response from proxy: "+response);
		
		return response;
	}
	// get customer based on mobile number
	public Response resteasyGetCustomerDetailsByMobileNumber(String mobileNumber) {
		
		logger.info("Inside resteasyGetCustomerDetailsByMobileNumber method");
		logger.info("mobileNo: "+mobileNumber);
		client = ClientBuilder.newClient();
		target = (ResteasyWebTarget) client.target(custAccsBasePath);
		CustomerAccountsProxy proxy = target.proxy(CustomerAccountsProxy.class);
	
		response = proxy.getCustomerDetailsByMobileNumber(mobileNumber);
		logger.info("response from proxy to fetch customer details: "+response.getStatus());
		
		return response;
	}
	
	public Response resteasyGenerateBankToken() {
		logger.info("Inside resteasyGenerateBankToken method");
		
		client = ClientBuilder.newClient();
		target = (ResteasyWebTarget) client.target(tokenBasePath);
		TokenProxy tokenProxy = target.proxy(TokenProxy.class);		
		response = tokenProxy.generateBankToken();
		
		return response;
	}
	
	public Response resteasySaveTx(Transaction tx) {
		logger.info("Inside resteasySaveTx method");
		
		client = ClientBuilder.newClient();
		target = (ResteasyWebTarget) client.target(txBasePath);
		TransactionProxy txProxy = target.proxy(TransactionProxy.class);		
		response = txProxy.saveTx(tx);
		
		logger.info("Response from proxy: "+response);
		
		return response;
	}
	
	// fetch details of the transaction from transaction_db
		public Response resteasyGetTx(String bankToken) {
			
			logger.info("Inside resteasyGetTx method");
			client = ClientBuilder.newClient();
			target = (ResteasyWebTarget) client.target(txBasePath);
			
			TransactionProxy txProxy = target.proxy(TransactionProxy.class);		
			response = txProxy.getTx(bankToken);
			
			logger.info("response from proxy to fetch transaction details: "+response.getStatus());
			
			return response;
		}
		
		public Response resteasyValidateJWTToken(String jwt) {
			logger.info("Inside resteasyValidateJWTToken method");
			client = ClientBuilder.newClient();
			target = (ResteasyWebTarget) client.target(authBasePath);
			
			AuthenticationProxy proxy = target.proxy(AuthenticationProxy.class);			
			response = proxy.validateJWTToken(jwt);
			
			logger.info("response from proxy: "+response);
			
			return response;
		}
		
		public Response resteasyPutCustomerAccountsDetails(CustomerAccounts custAccs) {
			
			logger.info("Inside resteasyPutCustomerAccountsDetails method");
			client = ClientBuilder.newClient();
			target = (ResteasyWebTarget) client.target(custAccsBasePath);
			// need to think about how put works
			CustomerAccountsProxy custProxy = target.proxy(CustomerAccountsProxy.class);		
			response = custProxy.updateCustomerDetailsbyCustomerId(custAccs.getCustomerId(),custAccs);
			
			logger.info("response from proxy to put customeraccount details: "+response.getStatus());
			
			return response;
			
		}
		
		public Response resteasyPutCustomerTransaction(Transaction tx) {
			logger.info("Inside resteasyPutCustomerTransaction method");
			client = ClientBuilder.newClient();
			target = (ResteasyWebTarget) client.target(txBasePath);
			// need to think about how put works
			TransactionProxy txProxy = target.proxy(TransactionProxy.class);		
			response = txProxy.updateTx(tx);
			
			logger.info("response from proxy to put customeraccount details: "+response.getStatus());
			
			return response;	
			
		}
		
		/*
		 * This method locates a matching account and updates the account balance
		 */
		public boolean findAndUpdateAccBalance(CustomerAccounts custAccs, String sortCode, String accountNo,String withdrawalAmt) {
			logger.info("Inside findAndUpdateAccBalance method");
			boolean isSuccess=false;
			Accounts[] accs = custAccs.getAccounts();
			for(int i=0;i<accs.length;i++) {
				if(accs!=null && (accs[i].getSortCode()).equals(sortCode) && (accs[i].getAccountNumber()).equals(accountNo)) {
					logger.info("Matching account found");
					Double accBal = Double.parseDouble(accs[i].getBalance());
					Double withBal = Double.parseDouble(withdrawalAmt);
					if(accBal >= withBal) {
						logger.info("Withdrawal is within balance limits.");
						accs[i].setBalance(Double.toString(accBal-withBal));
						logger.info("Account balance updated");
						isSuccess=true;
						break;
					}
				}						
			}
			
			return isSuccess;
		}
}
