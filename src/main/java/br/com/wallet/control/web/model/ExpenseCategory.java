package br.com.wallet.control.web.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	@JsonBackReference
	Set<ExpenseCategory> childrenCategories = new HashSet<>();
	
	@Relationship(type = "PARENT_OF", direction = Relationship.INCOMING)
	@JsonManagedReference
	private ExpenseCategory parent;
	
	public void addInner(ExpenseCategory category) {
		this.childrenCategories.add(category);
	}
	
}
