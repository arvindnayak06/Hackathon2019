package org.hackathon.cardless.orchestrator.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;
//import org.hackathon.cardless.customeraccounts.api.Endpoints;
import org.hackathon.cardless.orchestrator.model.AuthenticationProxy;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
//@Path("/validate")
public class AuthenticationFilter implements ContainerRequestFilter {
	
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    final static Logger logger = Logger.getLogger(AuthenticationFilter.class);
    @Override
    public void filter(ContainerRequestContext reqCtxt) throws IOException {
    	logger.info("Authentication filter triggered. Inside filter method.");
		// Read the Authorization header from the request
        String authHeader = reqCtxt.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header content
        if (!isJWTBasedAuthentication(authHeader)) {
        	logger.info("Authentication scheme does not start with Bearer<space>");
        	reqCtxt.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                            .build());
            return;
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        // Make resteasy call to authentication microservice to validate the token
       	Client client = ClientBuilder.newClient();
   		ResteasyWebTarget target = (ResteasyWebTarget) client.target("http://authentication-srv:8081/api");//"http://localhost:8081/api");
   		AuthenticationProxy proxy = target.proxy(AuthenticationProxy.class);
   		Response response = proxy.validateJWTToken(token);
   		if(response !=null && response.getStatus()!=200) {
   			 System.out.println("Issue with validating token: " + response.getEntity());
           	 reqCtxt.abortWith(
                     Response.status(Response.Status.UNAUTHORIZED)
                             .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                             .build());
    	}
	}
      
    /*
     * This method checks if an Authorization header field is present in request and 
     * if so then the value should start with 'Bearer ' to identify that this is a JWT token 
     */
	
    private boolean isJWTBasedAuthentication(String authHeader) {
    	return authHeader != null && authHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
    
    public static Key getKeyFromFile(String fileName) throws Exception{
	    Key pk = null;
	    File f = new File(fileName);
	    FileInputStream fis = new FileInputStream(f);
	    DataInputStream dis = new DataInputStream(fis);
	    byte[] keyBytes = new byte[(int)f.length()];
	    dis.readFully(keyBytes);
	    dis.close();

	    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    pk = kf.generatePublic(spec);
	    return pk;
	}
	
}
