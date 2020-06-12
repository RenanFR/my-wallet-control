package br.com.wallet.control.web.exception;

import lombok.Getter;

@Getter
public enum EntityType {
	
	USER("User credentials");
	
	private String domainDescription;
	
	private EntityType(String domainDescription) {
		this.domainDescription = domainDescription;
	}
	
}
