package br.com.wallet.control.web.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
public class EasynvestInvestment {
	
	private double ir;
	
	private LocalDateTime maturity;
	
	private double yield;
	
	private boolean hasSchedulings;
	
	private boolean isCloseToMaturityDate;
	
	private List<String> schedulings;
	
	private String symbolId;
	
	private double investedCapital;
	
	private String securityNameType;
	
	private double minimumWithdrawal;
	
	private boolean incomeTaxFree;
	
	private String securityType;
	
	private String nickName;
	
	private boolean settlement;
	
	private Integer settlementType;
	
	private double grossValue;
	
	private double netValue;
	
	private String rentability;
	
	private LocalDate updateDate;
	
	private boolean cache;
	
	private double unitPrice;
	
	private double quantity;
	
	private String hash;
	
	private EasynvestInvestmentType investmentType;
	
	private boolean isSecondaryMarket;
	
	private List<Map<String, String>> details;

}
