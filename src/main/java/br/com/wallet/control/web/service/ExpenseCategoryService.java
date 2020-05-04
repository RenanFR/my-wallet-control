package br.com.wallet.control.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.model.ExpenseCategory;
import br.com.wallet.control.web.repository.neo4j.ExpenseCategoryRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseCategoryService {
	
	@Autowired
	private ExpenseCategoryRepository repository;
	
	public List<ExpenseCategory> getFromAccount(String account) {
		List<ExpenseCategory> categoriesToReturn = new ArrayList<>();
		List<ExpenseCategory> accountCategories = repository.findByAccount(account);
		if (accountCategories == null || accountCategories.isEmpty()) {
			log.info("Account {} has no expense category saved, creating default list", account);
			createDefaultCategoriesForAccount(account)
				.forEach(defaultCategory -> categoriesToReturn.add(defaultCategory));
			return categoriesToReturn;
		}
		return accountCategories;
	}
	
	public Optional<ExpenseCategory> findParentCategoryOf(String childrenCategory) {
		return repository.getParentCategory(childrenCategory);
	}
	
	public ExpenseCategory findByIdentifier(Long id) {
		ExpenseCategory founded = repository.findById(id).get();
		return founded;
	}
	
	public ExpenseCategory appendChildren(String parent, String children, String account) {
		ExpenseCategory childrenCategory = ExpenseCategory.builder().name(children).account(account).build();
		if (!parent.equals("undefined")) {
			ExpenseCategory foundedParent = repository.findByName(parent);
			foundedParent.addInner(childrenCategory);
			childrenCategory.setLevel(foundedParent.getLevel() + 1);
			ExpenseCategory saved = repository.save(foundedParent);
			return saved;
		}
		childrenCategory.setLevel(1);
		ExpenseCategory saved = repository.save(childrenCategory);
		return saved;
	}
	
	private Iterable<ExpenseCategory> createDefaultCategoriesForAccount(String account) {
		List<ExpenseCategory> defaultCategories = new ArrayList<>();
		ExpenseCategory level2 = addChildren(account, "Alimentação");
		level2.addInner(ExpenseCategory.builder().name("Essencial").account(account).level(3).build());
		level2.addInner(ExpenseCategory.builder().name("Luxo").account(account).level(3).build());
		defaultCategories.add(ExpenseCategory
				.builder()
				.name("Planejado")
				.account(account)
				.level(1)
				.childrenCategories(Set.of(level2, addChildren(account, "Aluguel"), addChildren(account, "Transporte")))
				.build());
		defaultCategories.add(ExpenseCategory
				.builder()
				.name("Supérfluo")
				.account(account)
				.level(1)
				.childrenCategories(Set.of(addChildren(account, "Cinema"), addChildren(account, "Suplementos"), addChildren(account, "Cadeira")))
				.build());		
		defaultCategories.add(ExpenseCategory
				.builder()
				.name("Investimento")
				.account(account)
				.level(1)
				.childrenCategories(Set.of(addChildren(account, "Ações"), addChildren(account, "Caixa"), addChildren(account, "Tesouro")))
				.build());		
		Iterable<ExpenseCategory> defaultSaved = repository.saveAll(defaultCategories);
		return defaultSaved;
	}
	
	private ExpenseCategory addChildren(String account, String name) {
		return ExpenseCategory
				.builder()
				.name(name)
				.account(account)
				.level(2)
				.build();
	}
	
}
