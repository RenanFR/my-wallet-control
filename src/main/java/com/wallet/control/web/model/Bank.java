package com.wallet.control.web.model;

import lombok.Getter;
import lombok.Setter;

public enum Bank {
	
    INTER("Banco Inter"),

    ITAU("Banco Itaú");
	
	@Getter
	@Setter
	private String name;
    
    Bank(String name) {
    	this.name = name;
    }
    
}