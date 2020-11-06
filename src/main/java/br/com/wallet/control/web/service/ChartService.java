package br.com.wallet.control.web.service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.dto.ExpenseChart;
import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;

@Service
public class ChartService {
	
	@Autowired
	private BankStatementService service;
	
	public ExpenseChart getChartData(String userId) {
		ExpenseChart expenseChart = new ExpenseChart();
		List<BankStatement> statements = service.findStatementByUserId(userId);
		Map<String, Map<String, BigDecimal>> expensesByCategory = statements
				.stream()
				.flatMap(st -> st.getEntries().stream())
				.collect(Collectors.groupingBy((BankStatementEntry entry) -> entry.getCategory().getName(), 
						Collectors.toMap((BankStatementEntry entry) -> entry.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString(), 
						(BankStatementEntry entry) -> entry.getValue(), (e1, e2) -> e1.add(e2))));
		expenseChart.setExpensesByCategory(expensesByCategory);
		return expenseChart;
		
	}
	
}
