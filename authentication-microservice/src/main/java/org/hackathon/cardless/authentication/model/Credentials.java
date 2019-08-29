/*
 * Author: Arvind Nayak
 * Description: This POJO class represents the Login request that is sent by clients in order to login to their account.
 *              The fields represent the elements that need to be sent in the request.
 */

package org.hackathon.cardless.authentication.model;

import java.io.Serializable;

public class Credentials implements Serializable{
	
	private String username;
    private String password;
    private static final long serialVersionUID = 41L;

	public Credentials() {
		
	}
	
	public Credentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
