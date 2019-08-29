package org.hackathon.cardless.orchestrator.model;

public class WithdrawCCReq {
	
	private String mobileNumber;
	private String bankToken;
	private String userToken;
	
	public WithdrawCCReq() {
		
	}
	
	public WithdrawCCReq(String mobileNumber, String bankToken, String userToken) {
		super();
		this.mobileNumber = mobileNumber;
		this.bankToken = bankToken;
		this.userToken = userToken;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getBankToken() {
		return bankToken;
	}
	public void setBankToken(String bankToken) {
		this.bankToken = bankToken;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	
}
