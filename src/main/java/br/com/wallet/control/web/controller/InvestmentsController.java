package br.com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.service.StocksService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("statements")
@Slf4j
public class InvestmentsController {
	
	@Autowired
	private StocksService stocksService;

}
