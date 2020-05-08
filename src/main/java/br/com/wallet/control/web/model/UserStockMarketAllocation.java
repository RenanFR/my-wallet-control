package br.com.wallet.control.web.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStockMarketAllocation {
	
	private String _id;
	
	@Builder.Default
	private List<StocksAllocationBySector> currentStocksAllocation = new ArrayList<>();
	
	@Builder.Default
	private List<StocksAllocationBySector> stocksAllocationPlaned = new ArrayList<>();
	
	private int totalNumberStocks;
	
	private double totalValueInStocks;
	
	private String account;
	
}
