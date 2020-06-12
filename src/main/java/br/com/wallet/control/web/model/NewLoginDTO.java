package br.com.wallet.control.web.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {
		"userEmail",
		"userName",
		"cpf",
		"password",
		"easynvestPassword",
		"bitcoinBalance",
		"bankAccounts"
})
public class NewLoginDTO {
	
	private String userEmail;
	
	private String userName;
	
	private String password;
	
	private List<BankAccount> bankAccounts;
	
	private BigDecimal bitcoinBalance;
	
	private String cpf;
	
	private String easynvestPassword;
	
}
