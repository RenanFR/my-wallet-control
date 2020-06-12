package br.com.wallet.control.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.model.StocksAllocationBySector;
import br.com.wallet.control.web.model.UserStockMarketAllocation;
import br.com.wallet.control.web.repository.mongo.UserStockMarketAllocationRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StocksService {
	
	@Autowired
	private UserStockMarketAllocationRepository userStockRepository;
	
	public UserStockMarketAllocation getStocksInfoByUser(String userId) {
		UserStockMarketAllocation userStocksInfo = userStockRepository
			.findByUserId(userId)
			.orElseGet(() -> {
				log.info("User does not have stock market applications, creating a new record");
				UserStockMarketAllocation newUserStocksInfo = new UserStockMarketAllocation();
				newUserStocksInfo.setUserId(userId);
				userStockRepository.save(newUserStocksInfo);
				return newUserStocksInfo;
			});
		return userStocksInfo;
	}
	
	public UserStockMarketAllocation addNewStocksAllocationBySector(String userId, StocksAllocationBySector allocationBySector) {
		Optional<UserStockMarketAllocation> userStocksInfo = userStockRepository.findByUserId(userId);
		UserStockMarketAllocation toReturn = null;
		if (userStocksInfo.isPresent()) {
			UserStockMarketAllocation info = userStocksInfo.get();
			info.getStocksAllocationPlanned().add(allocationBySector);
			toReturn = userStockRepository.save(info);
		}
		return toReturn;
	}
	
}
