package com.wallet.control.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.wallet.control.web.MyWalletControlApplication;
import br.com.wallet.control.web.dto.ExpenseChart;
import br.com.wallet.control.web.model.ExpenseCategory;
import br.com.wallet.control.web.service.ChartService;

@SpringBootTest(classes = MyWalletControlApplication.class)
public class ChartServiceTest {
	
	@Autowired
	private ChartService service;	
	
	@Test
	void shouldNotBeNull() {
//		ExpenseChart chartData = service.getChartData("5f9871d77408fe665d1c1620");
//		ExpenseCategory category = new ExpenseCategory();
//		category.setName("Planejado");
//		assertNotNull(chartData);
//		assertEquals(new BigDecimal(-43.85).setScale(2, RoundingMode.HALF_UP), 
//				chartData.getExpensesByCategory().get(category).get("27/01/2020").setScale(2, RoundingMode.HALF_UP));
//		category.setName("Supérfluo");
//		assertEquals(new BigDecimal(-124.3).setScale(1, RoundingMode.HALF_UP), 
//				chartData.getExpensesByCategory().get(category).get("31/01/2020").setScale(1, RoundingMode.HALF_UP));
	}	

}
