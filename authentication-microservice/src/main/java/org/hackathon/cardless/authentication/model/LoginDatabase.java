/*
 * Author: Arvind Nayak
 * Description: A class to represent a document from Cloudant logindb database.
 */

package org.hackathon.cardless.authentication.model;

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class LoginDatabase implements Serializable{

	private static final long serialVersionUID = 42L;
	private String _id;
	private String _rev;
	private String username;
	private String password;
	private String customerId;
	
	public LoginDatabase(String id) {
		this._id = id;
	}
	
	public LoginDatabase(String _id, String _rev, String username, String password, String customerId) {
		super();
		this._id = _id;
		this._rev = _rev;
		this.username = username;
		this.password = password;
		this.customerId = customerId;
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_rev() {
		return _rev;
	}
	public void set_rev(String _rev) {
		this._rev = _rev;
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
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String toJsonString() {

		JsonObjectBuilder jBuilder = Json.createObjectBuilder();
		
		if(get_id() != null)
			jBuilder.add("_id",get_id());
		if(get_rev() != null)
			jBuilder.add("_rev",get_rev());
		if(getUsername() != null)
			jBuilder.add("username", getUsername());
		if(getPassword() != null)
			jBuilder.add("password", getPassword());
		if(getCustomerId() != null)
			jBuilder.add("customerId", getCustomerId());
		
		return jBuilder.build().toString();
	  }
}

