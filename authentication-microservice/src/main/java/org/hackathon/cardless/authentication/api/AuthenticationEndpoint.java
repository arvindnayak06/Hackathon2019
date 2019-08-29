/*
 * Author: Arvind Nayak
 * Description: JAX-RS endpoint class to receive all REST requests
 */
package org.hackathon.cardless.authentication.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hackathon.cardless.authentication.model.Credentials;
import org.hackathon.cardless.authentication.service.JWTService;

@Path("/authz")
public class AuthenticationEndpoint {
	
	@Inject
	private JWTService jwtService;
	private Response response;
	final static Logger logger = Logger.getLogger(AuthenticationEndpoint.class);
	
	/*
	 * This method is the endpoint for authenticating a user. If valid returns customer ID and a JWT token
	 */
	@POST
	@Path("/user/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Credentials credentials) {
		String username = credentials.getUsername();
	    String password = credentials.getPassword();
        
	    // check the user credentials
        logger.info("authentication.api Inside authenticateUser method. invoking validateCredentials method");
        response = jwtService.validateCredentials(username, password);

        // Return the token on the response
        return response;
	} 
	
	/*
	 * This method can be used to check if a token is valid. Exposed as a helper function.
	 */
	@POST
	@Path("/jwt/validate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
	public Response validateJWTToken(String jwt) {
		 logger.info("authentication.api Inside validateJWTToken. invoking validateJWTToken method");
		response = jwtService.validateJWTToken(jwt);
		
		return response;
	}
}
