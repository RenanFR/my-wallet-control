package br.com.wallet.control.web.model;

import org.springframework.security.core.context.SecurityContextHolder;

public class DadosLogin {
	
	public static Login get() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return login;
	}
	

}
