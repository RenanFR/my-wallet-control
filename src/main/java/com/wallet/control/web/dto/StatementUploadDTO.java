package com.wallet.control.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.wallet.control.web.model.FileExtension;

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
		"account",
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
	
	private String account;
	
	private LocalDate periodStart;
	
	private LocalDate periodEnd;
	
	private FileExtension fileExtension;
	
	private MultipartFile file;
	
	private LocalDateTime uploadedAt;
	
	private String columnDate;
	
	private String columnDescription;
	
	private String columnValue;
	
	private String columnBalance;
	
}
