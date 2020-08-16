package br.com.wallet.control.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import br.com.wallet.control.web.model.Bank;
import br.com.wallet.control.web.model.FileExtension;
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
@ToString(of = {
		"periodStart",
		"periodEnd",
		"uploadedAt",
		"columnDate",
		"columnDescription",
		"columnValue",
		"columnBalance",
		"file"
})
@Builder
public class StatementUploadDTO {
	
	private LocalDate periodStart;
	
	private LocalDate periodEnd;
	
	private FileExtension fileExtension;
	
	private Bank bank;
	
	private String bankAccount;
	
	private MultipartFile file;
	
	private LocalDateTime uploadedAt;
	
	private String columnDate;
	
	private String columnDescription;
	
	private String columnValue;
	
	private String columnBalance;
	
}
