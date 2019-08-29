package org.hackathon.cardless.orchestrator.service;

import org.apache.log4j.Logger;
import org.hackathon.cardless.orchestrator.model.Accounts;
import org.hackathon.cardless.orchestrator.model.CustomerAccounts;
import org.hackathon.cardless.orchestrator.model.TransactionReq;

public class OrchestratorService {
	final static Logger logger = Logger.getLogger(OrchestratorService.class);
	
	public boolean validateCCTxReq(TransactionReq txReq, CustomerAccounts custAccs) {
		boolean isValid = false;
		logger.info("Inside validateCCTxReq method");
		Accounts[] accs = custAccs.getAccounts();
		
		for(int i=0;i<accs.length;i++) {
			if(accs[i].getSortCode() == txReq.getSortCode() && accs[i].getAccountNumber() == txReq.getAccountNumber()) {
				logger.info("SortCode-AccountNumber match found");
				if(Double.parseDouble(accs[i].getBalance()) >= Double.parseDouble(txReq.getAmount())) {
					logger.info("Withdrawal amount is within account limits");				
					isValid = true;
					break;
				}
			}
		}
		
		return isValid;
	}
}
