package com.wallet.control.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.control.web.model.BankStatement;

@Service
public class FinancialDashboardService {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private UploadService uploadService;
	
	public BankStatement findStatementByAccount(String account) {
		BankStatement bankStatement = bankStatementService.findStatementByAccount(account);
		String preSignedURL = uploadService.createPreSignedURLtoView(bankStatement.getFileName());
		bankStatement.setPreSignedURL(preSignedURL);
		return bankStatement;
	}
	

}
