package com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wallet.control.web.model.BankStatement;
import com.wallet.control.web.service.FinancialDashboardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("financial")
@Slf4j
public class FinancialDashboardController {
	
	@Autowired
	private FinancialDashboardService service;
	
	@GetMapping("{account}")
	public ResponseEntity<BankStatement> getStatementByAccount(@PathVariable("account") String account) {
		log.info("Searching for bank statement to account {}", account);
		BankStatement statement = service.findStatementByAccount(account);
		return ResponseEntity.ok(statement);
	}
}
