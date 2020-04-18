package com.wallet.control.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankStatement {
	
	private String account;
	
	private LocalDate periodStart;
	
	private LocalDate periodEnd;
	
	private BigDecimal currentBalance;

}
