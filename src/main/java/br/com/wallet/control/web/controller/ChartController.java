package br.com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.dto.ExpenseChart;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.service.ChartService;

@Controller
@RequestMapping("chart")
public class ChartController {
	
	@Autowired
	private ChartService service;
	
	@GetMapping
	public ResponseEntity<ExpenseChart> getUserMainChart() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();			
		return ResponseEntity.ok(service.getChartData(userId));
		
	}

}
