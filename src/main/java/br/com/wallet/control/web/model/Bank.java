package br.com.wallet.control.web.model;

import lombok.Getter;
import lombok.Setter;

public enum Bank {
	
    INTER("Banco Inter"),
    
    CAIXA("Caixa Econômica Federal"),

    ITAU("Banco Itaú");
	
	@Getter
	@Setter
	private String name;
	
	public static Bank parseFromName(String name) {
		switch (name) {
		case "Banco Inter": {
			return Bank.INTER;
		}
		case "Caixa Econômica Federal": {
			return Bank.CAIXA;
		}
		case "Banco Itaú": {
			return Bank.ITAU;
		}
		default:
			throw new IllegalArgumentException("CANNOT FIND BANK OPTION WITH NAME: " + name);
		}
	}
    
    Bank(String name) {
    	this.name = name;
    }
    
}