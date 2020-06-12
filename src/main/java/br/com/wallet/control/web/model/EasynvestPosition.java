package br.com.wallet.control.web.model;

import java.time.LocalDateTime;
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
public class EasynvestPosition extends InvestmentPosition {
	
	private double easyBalance;
	
	private double totalBalance;
	
	private boolean hasIpo;
	
	private boolean hasEquity;
	
	private int investmentsQuantity;
	
	private boolean isProjectedBalance;
	
	private boolean isCached;
	
	private LocalDateTime cacheUpdatedData;
	
	private List<EasynvestInvestment> investments;
	
}
