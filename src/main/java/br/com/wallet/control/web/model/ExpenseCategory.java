package br.com.wallet.control.web.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {
		"name"
})
public class ExpenseCategory {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String account;
	
	private int level;
	
	@Relationship(type = "PARENT_OF", direction = Relationship.OUTGOING)
	@Builder.Default
	@JsonIgnore
	Set<ExpenseCategory> childrenCategories = new HashSet<>();
	
	@Relationship(type = "PARENT_OF", direction = Relationship.INCOMING)
	private ExpenseCategory parent;
	
	private Boolean root;
	
	private Boolean hasChildren;
	
	public void addInner(ExpenseCategory category) {
		this.childrenCategories.add(category);
	}

}
