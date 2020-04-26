package com.wallet.control.web.service.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.wallet.control.web.dto.BankStatementAssembler;
import com.wallet.control.web.dto.StatementUploadDTO;
import com.wallet.control.web.model.BankStatement;
import com.wallet.control.web.model.BankStatementEntry;
import com.wallet.control.web.model.FileExtension;

import lombok.extern.slf4j.Slf4j;

@Service("inter-csv-reader")
@Slf4j
public class InterBankStatementCSVReader implements BankStatementCSVReader {
	
	private List<Character> alphabet = Arrays.asList(
			'A', 'B', 'C', 'D', 'E', 
            'F', 'G', 'H', 'I', 'K', 
            'L', 'M', 'N', 'O', 'P', 
            'Q', 'R', 'S', 'T', 'U', 
            'V', 'W', 'X', 'Y', 'Z'
    );	

	@Override
	public BankStatement readAndParse(String fileName, StatementUploadDTO uploadDTO) throws IOException, CsvException {
		BankStatement bankStatement = BankStatementAssembler.fromDTO(fileName, uploadDTO);
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
		return bankStatement;
	}

}
