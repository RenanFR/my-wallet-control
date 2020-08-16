package br.com.wallet.control.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.service.BankStatementService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("statements")
@Slf4j
public class BankStatementController {
	
	@Autowired
	private BankStatementService service;
	
	@GetMapping
	public ResponseEntity<List<BankStatement>> getStatementByUser() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();		
		log.info("SEARCHING FOR BANK STATEMENT TO USER {}", userId);
		List<BankStatement> statements = service.findStatementByUserId(userId);
		return ResponseEntity.ok(statements);
	}
	
	@GetMapping("upload/{uploadId}")
	public ResponseEntity<BankStatement> getStatementByUploadId(@PathVariable("uploadId") String uploadId) {
		log.info("SEARCHING FOR BANK STATEMENT TO UPLOAD ID {}", uploadId);
		BankStatement statement = service.findStatementByUploadId(uploadId);
		return ResponseEntity.ok(statement);
	}
	
	
}
