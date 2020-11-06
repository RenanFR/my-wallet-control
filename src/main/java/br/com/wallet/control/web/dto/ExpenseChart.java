package br.com.wallet.control.web.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseChart {
	
	Map<String, Map<String, BigDecimal>> expensesByCategory = new HashMap<>();
	
}
