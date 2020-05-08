package br.com.wallet.control.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.repository.mongo.UserStockMarketAllocationRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StocksService {
	
	@Autowired
	private UserStockMarketAllocationRepository userStockRepository;

}
