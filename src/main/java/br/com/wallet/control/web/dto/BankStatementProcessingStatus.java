package br.com.wallet.control.web.dto;

import br.com.wallet.control.web.model.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BankStatementProcessingStatus {
	
	private String statementId;
	
	private JobStatus status;

}
