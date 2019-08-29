package org.hackathon.cardless.authentication.service;

import static com.cloudant.client.api.query.Expression.eq;

import java.net.MalformedURLException;
import java.net.URL;

import org.hackathon.cardless.authentication.model.LoginDatabase;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;

public class CloudantClientService {
	
	private CloudantClient client;
	private Database loginDb;
	private LoginDatabase login;

	public CloudantClientService() {
		
	}
	public void connectToCloudant() throws MalformedURLException {
		
		client = ClientBuilder				
	            .url(new URL("https://ae976554-9e77-4668-8b14-853b977e79a0-bluemix:d4eb5982839e19e9cec2ff9bf31851617d513360a823293f2986b8317e2a84fa@ae976554-9e77-4668-8b14-853b977e79a0-bluemix.cloudantnosqldb.appdomain.cloud"))
	            .username("ae976554-9e77-4668-8b14-853b977e79a0-bluemix")
	            .password("d4eb5982839e19e9cec2ff9bf31851617d513360a823293f2986b8317e2a84fa")
	            .build();
	}
	
	public Database connectToDb(CloudantClient client, String dbName, boolean createNew) {
		return client.database(dbName, createNew);
	}
	
	public LoginDatabase getLoginDetailsByUsername(String username) throws MalformedURLException {
		connectToCloudant();
		loginDb = connectToDb(client,"logindb", false);
		
		// create query to fetch document based on username
		QueryResult<LoginDatabase> result = loginDb.query(new QueryBuilder(eq("username", username))
										.fields("username", "password", "customerId")
										.build(), LoginDatabase.class);
		if(result!=null && result.getDocs()!=null && !result.getDocs().isEmpty()) {
			login = result.getDocs().get(0);
		}
		return login;		
	}
	
	public static void main(String args[]) {
		CloudantClientService app = new CloudantClientService();
		try {
			LoginDatabase login = app.getLoginDetailsByUsername("arvind");
			if(login!=null)
				System.out.println(login.toJsonString());
			else
				System.out.println("No records found!");
		}
		catch(Exception e){
			System.out.println("Error Details: "+e);
		}
	}
}
