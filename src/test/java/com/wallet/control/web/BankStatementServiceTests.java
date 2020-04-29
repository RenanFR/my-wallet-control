package com.wallet.control.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.wallet.control.web.service.BankStatementService;

@SpringBootTest
public class BankStatementServiceTests {
	
	@Autowired
	private BankStatementService service;

	@Test
	void shouldNotBeNull() {
		assertNotNull(service);
	}
	
}
