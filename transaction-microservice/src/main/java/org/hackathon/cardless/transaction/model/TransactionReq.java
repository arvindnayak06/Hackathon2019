/*
 * Author: Arvind Nayak
 * Description: This POJO class represents the Transaction request that is sent by clients in order to register a 
 *              Cardless cash request. The fields represent the elements that need to be sent in the request.
 */

package org.hackathon.cardless.transaction.model;

import java.io.Serializable;

public class TransactionReq implements Serializable{

	private String sortCode;
	private String accountNumber;
	private String customerId;
	private String amount;
	private String transactionType;
    private static final long serialVersionUID = 43L;
    
    public TransactionReq() {
    	
    }

	public TransactionReq(String sortCode, String accountNumber, String customerId, String amount,
			String transactionType) {
		super();
		this.sortCode = sortCode;
		this.accountNumber = accountNumber;
		this.customerId = customerId;
		this.amount = amount;
		this.transactionType = transactionType;
	}
	
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
