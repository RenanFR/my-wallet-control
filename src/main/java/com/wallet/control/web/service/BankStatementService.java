package com.wallet.control.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.control.web.repository.BankStatementEntryRepository;
import com.wallet.control.web.repository.BankStatementRepository;

@Service
public class BankStatementService {
	
	@Autowired
	private BankStatementRepository repository;
	
	@Autowired
	private BankStatementEntryRepository entryRepository;

}
