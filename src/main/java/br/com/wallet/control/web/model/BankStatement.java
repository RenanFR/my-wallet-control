package br.com.wallet.control.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

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
		"account",
		"periodStart",
		"periodEnd",
		"uploadedAt",
		"fileName",
		"fileExtension",
		"bank"
})
public class BankStatement {
	
	private String _id;
	
	private String account;
	
	private LocalDate periodStart;
	
	private LocalDate periodEnd;
	
	private BigDecimal currentBalance;
	
	private LocalDateTime uploadedAt;
	
	@Builder.Default
	private List<BankStatementEntry> entries = new ArrayList<>();
	
	@Transient
	private MultipartFile file;	
	
	private String fileName;
	
	@Transient
	private String originalFileName;
	
	private FileExtension fileExtension;
	
	private Bank bank;
	
	private String columnDate;
	
	private String columnDescription;
	
	private String columnValue;
	
	private String columnBalance;	
	
	@Transient
	private String preSignedURL;
	
	private JobStatus status;

}
