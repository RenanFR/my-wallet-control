package com.wallet.control.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class BankStatementEntry {
	
	private LocalDate date;
	
	private String description;
	
	private BigDecimal value;
	
	private BigDecimal balanceAfter;
	
	private Long lineNumber;
	
}
