package br.com.wallet.control.web.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.wallet.control.web.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	private String _id;
	
	private String userName;
	
	private String cpf;
	
	private String userEmail;
	
	@Builder.Default
	private List<BankAccount> bankAccounts = new ArrayList<>();	
	
}
