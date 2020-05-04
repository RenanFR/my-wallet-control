package br.com.wallet.control.web.repository.neo4j;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.com.wallet.control.web.model.ExpenseCategory;

public interface ExpenseCategoryRepository extends Neo4jRepository<ExpenseCategory, Long> {
	
	List<ExpenseCategory> findByAccount(String account);
	
	@Query("MATCH (category:ExpenseCategory)<-[:PARENT_OF]-(parent:ExpenseCategory) WHERE category.name = $0 RETURN parent")
	Optional<ExpenseCategory> getParentCategory(String childrenNode);
	
	ExpenseCategory findByName(String name);
	
}
