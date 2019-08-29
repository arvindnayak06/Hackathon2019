package org.hackathon.cardless.orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Accounts {
	private String sortCode;
	private String accountNumber;
	private String productType;
	private String balance;
	private String cardlessCash;
	
	public String getSortCode() {
		return sortCode;
	}
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCardlessCash() {
		return cardlessCash;
	}
	public void setCardlessCash(String cardlessCash) {
		this.cardlessCash = cardlessCash;
	}
	
}
