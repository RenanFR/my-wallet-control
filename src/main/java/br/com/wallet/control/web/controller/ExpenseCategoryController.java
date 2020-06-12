package br.com.wallet.control.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.wallet.control.web.model.ExpenseCategory;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.service.ExpenseCategoryService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("expenses")
@Slf4j
public class ExpenseCategoryController {
	
	@Autowired
	private ExpenseCategoryService service;
	
	@GetMapping
	public ResponseEntity<List<ExpenseCategory>> getFromAccount() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();		
		log.info("SEARCHING FOR EXPENSES CATEGORIES TO USER {}", userId);
		List<ExpenseCategory> accountCategories = service.getFromAccount(userId);
		log.info("{} CATEGORIES AVAILABLE TO USER {}", accountCategories.size(), userId);
		return ResponseEntity.ok(accountCategories);
	}
	
	@GetMapping("parent/{children}")
	public ResponseEntity<ExpenseCategory> findParentCategoryOf(
			@PathVariable("children") String childrenCategory
			) {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();		
		log.info("SEARCHING PARENT CATEGORY OF NODE {}", childrenCategory);
		Optional<ExpenseCategory> parent = service.findParentCategoryOf(childrenCategory, userId);
		parent.ifPresent(p -> log.info("PARENT OF CATEGORY {} IS CATEGORY {}", childrenCategory, p.getName()));
		return ResponseEntity.ok(parent.orElse(null));
	}
	
	@PostMapping
	public ResponseEntity<?> addNew(
			@RequestParam(value = "parentCategory", required = false) String parent, 
			@RequestParam("childrenCategory") String children) {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = login.get_id();		
		log.info("SAVING NEW {} CATEGORY {} FOR USER {}", parent.equals("undefined")? "PARENT" : "CHILDREN", children, userId);
		ExpenseCategory saved = service.appendChildren(parent, children, userId);
		return ResponseEntity.ok(saved);
	}
}

