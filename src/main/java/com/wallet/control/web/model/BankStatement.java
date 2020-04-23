package com.wallet.control.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

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
public class BankStatement {
	
	private String account;
	
	private LocalDate periodStart;
	
	private LocalDate periodEnd;
	
	private BigDecimal currentBalance;
	
	private LocalDateTime uploadedAt;
	
	@Builder.Default
	private List<BankStatementEntry> entries = new ArrayList<>();
	
	private String fileName;
	
	private FileExtension fileExtension;
	
	private String columnDate;
	
	private String columnDescription;
	
	private String columnValue;
	
	private String columnBalance;	
	
	@Transient
	private String preSignedURL;

}
