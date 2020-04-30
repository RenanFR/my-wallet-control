package br.com.wallet.control.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.service.BankStatementService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("statements")
@Slf4j
public class BankStatementController {
	
	@Autowired
	private BankStatementService service;
	
	@GetMapping("{account}")
	public ResponseEntity<List<BankStatement>> getStatementByAccount(@PathVariable("account") String account) {
		log.info("Searching for bank statement to account {}", account);
		List<BankStatement> statements = service.findStatementByAccount(account);
		return ResponseEntity.ok(statements);
	}
	
	@GetMapping("upload/{uploadId}")
	public ResponseEntity<BankStatement> getStatementByUploadId(@PathVariable("uploadId") String uploadId) {
		log.info("Searching for bank statement to upload id {}", uploadId);
		BankStatement statement = service.findStatementByUploadId(uploadId);
		return ResponseEntity.ok(statement);
	}
}
