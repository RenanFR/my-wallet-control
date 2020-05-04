package br.com.wallet.control.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.wallet.control.web.model.ExpenseCategory;
import br.com.wallet.control.web.service.ExpenseCategoryService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("expenses")
@Slf4j
public class ExpenseCategoryController {
	
	@Autowired
	private ExpenseCategoryService service;
	
	@GetMapping("{account}")
	public ResponseEntity<List<ExpenseCategory>> getFromAccount(@PathVariable("account") String account) {
		log.info("Searching for expenses categories to account {}", account);
		List<ExpenseCategory> accountCategories = service.getFromAccount(account);
		log.info("{} categories available to account {}", accountCategories.size(), account);
		return ResponseEntity.ok(accountCategories);
	}
	
	@GetMapping("parent/{children}")
	public ResponseEntity<ExpenseCategory> findParentCategoryOf(@PathVariable("children") String childrenCategory) {
		log.info("Searching parent category of node {}", childrenCategory);
		Optional<ExpenseCategory> parent = service.findParentCategoryOf(childrenCategory);
		parent.ifPresent(p -> log.info("Parent of category {} is category {}", childrenCategory, p.getName()));
		return ResponseEntity.ok(parent.orElse(null));
	}
	
	@PostMapping("{account}")
	public ResponseEntity<?> addNew(
			@RequestParam(value = "parentCategory", required = false) String parent, 
			@RequestParam("childrenCategory") String children, 
			@PathVariable("account") String account) {
		log.info("Saving new {} category {} for account {}", parent.equals("undefined")? "parent" : "children", children, account);
		ExpenseCategory saved = service.appendChildren(parent, children, account);
		return ResponseEntity.ok(saved);
	}
}

