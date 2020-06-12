package br.com.wallet.control.web.exception;

public class UserAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException() {
		super("USUÁRIO COM OS DADOS INFORMADOS JÁ EXISTE NA BASE DE DADOS");
	}

}
