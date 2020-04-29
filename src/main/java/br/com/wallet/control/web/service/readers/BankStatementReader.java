package br.com.wallet.control.web.service.readers;

import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvException;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;

public interface BankStatementReader {
	
	List<BankStatementEntry> readAndParse(BankStatement bankStatement) throws IOException, CsvException, InterruptedException;
	
}
