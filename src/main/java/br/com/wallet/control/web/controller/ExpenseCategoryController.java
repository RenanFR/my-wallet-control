package br.com.wallet.control.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;
import br.com.wallet.control.web.model.ExpenseCategory;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.repository.neo4j.ExpenseCategoryRepository;
import br.com.wallet.control.web.service.BankStatementService;
import br.com.wallet.control.web.service.ExpenseCategoryService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("expenses")
@Slf4j
public class ExpenseCategoryController {
	
	@Autowired
	private ExpenseCategoryService service;
	
	@Autowired
	private ExpenseCategoryRepository categoriesRepository;	
	
	@Autowired
	private BankStatementService statementService;

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
	
	@PutMapping("{entryId}")
	public ResponseEntity<Void> updateCategoryEntry(
			@PathVariable("entryId") String entryId,
			@RequestParam("expenseCategoryId") Long expenseCategoryId) {
		log.info("SETTING CATEGORY {} TO EXPENSE ENTRY {}", expenseCategoryId, entryId);
		Optional<BankStatement> statement = statementService.findStatementByEntryId(entryId);
		statement.ifPresent((BankStatement bs) -> {
			bs.getEntries()
				.stream()
				.filter((BankStatementEntry entry) -> entry.getHash().equals(entryId))
				.findFirst()
				.ifPresent((BankStatementEntry e) -> {
					e.setIdExpenseCategory(expenseCategoryId);
					log.info("EXPENSE ENTRY {} IS OF NOW OF TYPE {}", e.getDescription(), categoriesRepository.findById(expenseCategoryId).get().getName());
				});
			statementService.save(bs);
		});
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PatchMapping("entries")
	public ResponseEntity<Void> updateAllEntriesCategory(
			@RequestBody BankStatement bankStatement) {
		log.info("UPDATING CATEGORY OF {} ENTRIES OF BANK STATEMENT {}", 
				bankStatement.getEntries().size(), bankStatement.get_id());
		bankStatement.getEntries()
			.stream()
			.forEach((BankStatementEntry e) -> {
				log.info("EXPENSE ENTRY {} IS OF NOW OF TYPE {}", e.getDescription(), e.getCategory().getName());
			});
		statementService.update(bankStatement);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}

