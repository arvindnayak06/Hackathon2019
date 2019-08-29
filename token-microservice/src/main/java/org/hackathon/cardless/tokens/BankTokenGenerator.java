/*
 * Author: Arvind Nayak
 * Description: JAX-RS endpoint class to receive all REST requests
 */
package org.hackathon.cardless.tokens;

import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


@Path("/tokens")
public class BankTokenGenerator {
	
	private String bankToken;
	final static Logger logger = Logger.getLogger(BankTokenGenerator.class);
	
	public String getToken() {
		return bankToken;
	}
	
	public void setToken(String token) {
		bankToken = token;
	}
	
	@GET
	@Path("/bank")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateBankToken() 
    { 
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
        logger.info("Inside generateBankToken method.");
        int tokenSize = Integer.parseInt(System.getenv("CC_TOKEN_SIZE"));
        logger.info("Token Size: "+tokenSize);
        char[] otp = new char[tokenSize];   
        for (int i = 0; i < tokenSize; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        setToken(String.copyValueOf(otp));
        logger.info("Generated Bank Token: "+getToken());
        JsonObject responseJson = Json.createObjectBuilder()
        							  .add("bankToken",getToken())
        							  .build();
         
        return Response.status(200).entity(responseJson).build();
    } 
	
	public static void main(String args[]) {
		BankTokenGenerator btg = new BankTokenGenerator();
		Response resp = btg.generateBankToken();
		System.out.println(resp);
	}
}