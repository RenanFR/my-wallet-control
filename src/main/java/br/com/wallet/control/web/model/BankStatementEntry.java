package br.com.wallet.control.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {
		"date",
		"description",
		"value",
		"balanceAfter",
		"lineNumber",
		"category"
})
public class BankStatementEntry {
	
	private String hash;
	
	private LocalDate date;
	
	private String description;
	
	private BigDecimal value;
	
	private BigDecimal balanceAfter;
	
	private Long lineNumber;
	
	private Long idExpenseCategory;
	
	private ExpenseCategory category;
	
}
