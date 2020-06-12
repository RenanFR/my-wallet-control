package br.com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.model.InvestmentPosition;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.model.StocksAllocationBySector;
import br.com.wallet.control.web.model.UserInvestmentsProvider;
import br.com.wallet.control.web.model.UserStockMarketAllocation;
import br.com.wallet.control.web.service.StocksService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("investments")
@Slf4j
public class InvestmentsController {
	
	@Autowired
	private StocksService stocksService;
	
	@Autowired
	private UserInvestmentsProvider investmentsProvider;
	
	@GetMapping("stocks/info")
	public ResponseEntity<UserStockMarketAllocation> getStockMarketByUser() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();
		log.info("Getting stock market allocation plan for user {}", userId);
		UserStockMarketAllocation stocksInfoByUser = stocksService.getStocksInfoByUser(userId);
		return ResponseEntity.ok(stocksInfoByUser);
	}
	
	@PostMapping("stocks/allocation")
	public ResponseEntity<UserStockMarketAllocation> addNewStocksAllocationBySector(@RequestBody StocksAllocationBySector allocationBySector) {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();
		log.info("Adding user {} intention to apply {} percent of his stock wallet on sector {}", userId, allocationBySector.getAllocationPercentageGoal(), allocationBySector.getSectorName());
		UserStockMarketAllocation stocksInfoByUser = stocksService.addNewStocksAllocationBySector(userId, allocationBySector);
		return ResponseEntity.ok(stocksInfoByUser);
	}
	
	@GetMapping
	public ResponseEntity<InvestmentPosition> userInvestmentApplications() {
		return ResponseEntity.ok(investmentsProvider.getFinancialPositionFromUser());
		
	}

}
