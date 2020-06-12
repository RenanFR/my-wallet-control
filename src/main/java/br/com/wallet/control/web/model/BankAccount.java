package br.com.wallet.control.web.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.wallet.control.web.dto.binders.BankDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {
		"accountNumber",
		"agency",
		"bank",
		"accountType",
		"owner"
})
public class BankAccount {
	
	private String accountNumber;
	
	private String agency;
	
	@JsonDeserialize(using = BankDeserializer.class)
	private Bank bank;
	
	private String accountType;
	
	private Login owner;
	
}
