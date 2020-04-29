package br.com.wallet.control.web.service.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;
import br.com.wallet.control.web.model.FileExtension;
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
	public List<BankStatementEntry> readAndParse(BankStatement bankStatement) throws IOException, CsvException {
		//TODO Create aspect to log readers
		log.info("Starting bank statement entries processing for bank {} and extension {}", bankStatement.getBank().getName(), bankStatement.getFileExtension());
		List<BankStatementEntry> entries = new ArrayList<>();
		try (Reader reader = new BufferedReader(new InputStreamReader(bankStatement.getFile().getInputStream()))) {
			if (bankStatement.getFileExtension() == FileExtension.CSV) {
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
							String columnValue = line[alphabet.indexOf(bankStatement.getColumnValue().toCharArray()[0])].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim();
							String columnBalance = line[alphabet.indexOf(bankStatement.getColumnBalance().toCharArray()[0])].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim();
							String columnDescription = line[alphabet.indexOf(bankStatement.getColumnDescription().toCharArray()[0])].trim();
							String columnDate = line[alphabet.indexOf(bankStatement.getColumnDate().toCharArray()[0])].trim();
							log.info("Reading line {}, date {}, description {}, value {}, balance after {}", index, columnDate, columnDescription, columnValue, columnBalance);
							BankStatementEntry entry = BankStatementEntry.builder()
									.date(LocalDate.parse(columnDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
									.description(columnDescription)
									.value(new BigDecimal(columnValue))
									.balanceAfter(new BigDecimal(columnBalance))
									.lineNumber(Integer.valueOf(index).longValue())
									.build();
							entries.add(entry);								
						} catch (DateTimeParseException parseException) {
							log.error("Error parsing date from text {}", parseException.getParsedString());
						}
					}
				}
				csvReader.close();
			}
		}
		return entries;
	}

}
