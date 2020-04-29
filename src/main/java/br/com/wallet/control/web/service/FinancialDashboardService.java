package br.com.wallet.control.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.model.BankStatement;

@Service
public class FinancialDashboardService {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private UploadService uploadService;
	
	public BankStatement findStatementByUploadId(String uploadId) {
		BankStatement bankStatement = bankStatementService.findStatementByUploadId(uploadId);
		String preSignedURL = uploadService.createPreSignedURLtoView(bankStatement.getFileName());
		bankStatement.setPreSignedURL(preSignedURL);
		return bankStatement;
	}
	
	public List<BankStatement> findStatementByAccount(String account) {
		List<BankStatement> statements = bankStatementService.findStatementByAccount(account);
		return statements;
	}
	

}
