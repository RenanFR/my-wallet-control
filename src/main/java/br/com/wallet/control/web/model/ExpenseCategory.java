package br.com.wallet.control.web.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategory {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String account;
	
	private int level;
	
	@Relationship(type = "PARENT_OF", direction = Relationship.OUTGOING)
	@Builder.Default
	Set<ExpenseCategory> childrenCategories = new HashSet<>();
	
	public void addInner(ExpenseCategory category) {
		this.childrenCategories.add(category);
	}
	
}
