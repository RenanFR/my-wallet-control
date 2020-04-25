package com.wallet.control.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.wallet.control.web.dto.StatementUploadDTO;
import com.wallet.control.web.model.BankStatement;
import com.wallet.control.web.model.BankStatementEntry;
import com.wallet.control.web.model.FileExtension;
import com.wallet.control.web.service.BankStatementService;
import com.wallet.control.web.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("upload")
@Slf4j
public class UploadController {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	
	@Autowired
	private UploadService service;
	
	private List<Character> alphabet = Arrays.asList(
			'A', 'B', 'C', 'D', 'E', 
            'F', 'G', 'H', 'I', 'K', 
            'L', 'M', 'N', 'O', 'P', 
            'Q', 'R', 'S', 'T', 'U', 
            'V', 'W', 'X', 'Y', 'Z'
    );
	
	@PostMapping(consumes = {
			"multipart/form-data"
	})
	public ResponseEntity<String> upload(@ModelAttribute StatementUploadDTO uploadDTO) {
		try {
			String fileName = uploadDTO.getAccount().replace("-", "") + "_" + (uploadDTO.getPeriodStart().toString().replace("-", "")) + "-" + (uploadDTO.getPeriodEnd().toString().replace("-", "")) + "." + uploadDTO.getFileExtension().toString().toLowerCase();
			service.uploadToBucket(fileName, uploadDTO.getFile());
			BankStatement bankStatement = BankStatement.builder()
					.account(uploadDTO.getAccount())
					.periodStart(uploadDTO.getPeriodStart())
					.periodEnd(uploadDTO.getPeriodEnd())
					.uploadedAt(uploadDTO.getUploadedAt())
					.fileName(fileName)
					.fileExtension(uploadDTO.getFileExtension())
					.bank(uploadDTO.getBank())
					.columnDate(uploadDTO.getColumnDate())
					.columnDescription(uploadDTO.getColumnDescription())
					.columnValue(uploadDTO.getColumnValue())
					.columnBalance(uploadDTO.getColumnBalance())
					.build();
			try (Reader reader = new BufferedReader(new InputStreamReader(uploadDTO.getFile().getInputStream()))) {
				if (uploadDTO.getFileExtension() == FileExtension.CSV) {
					CSVParser parser = new CSVParserBuilder() 
							.withSeparator(';') 
							.withIgnoreQuotations(true) 
							.build();
					CSVReader csvReader = new CSVReaderBuilder(reader)
							.withCSVParser(parser)
							.build();
					List<String[]> lines = csvReader.readAll();
					for (int index = 0; index < lines.size(); index++) {
						String[] line = lines.get(index);
						if (line.length == 4) {
							try {
								String columnValue = line[alphabet.indexOf(uploadDTO.getColumnValue().toCharArray()[0])].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim();
								String columnBalance = line[alphabet.indexOf(uploadDTO.getColumnBalance().toCharArray()[0])].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim();
								String columnDescription = line[alphabet.indexOf(uploadDTO.getColumnDescription().toCharArray()[0])].trim();
								String columnDate = line[alphabet.indexOf(uploadDTO.getColumnDate().toCharArray()[0])].trim();
								log.info("Reading line {}, date {}, description {}, value {}, balance after {}", index, columnDate, columnDescription, columnValue, columnBalance);
								BankStatementEntry entry = BankStatementEntry.builder()
										.date(LocalDate.parse(columnDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
										.description(columnDescription)
										.value(new BigDecimal(columnValue))
										.balanceAfter(new BigDecimal(columnBalance))
										.lineNumber(Integer.valueOf(index).longValue())
										.build();
								bankStatement.getEntries().add(entry);								
							} catch (DateTimeParseException parseException) {
								log.error("Error parsing date from text {}", parseException.getParsedString());
							}
						}
					}
					csvReader.close();
				}
			}
			bankStatementService.save(bankStatement);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(uploadDTO.getAccount());
	}
	
}
