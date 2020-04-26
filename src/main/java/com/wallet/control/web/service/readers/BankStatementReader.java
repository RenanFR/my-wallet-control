package com.wallet.control.web.service.readers;

import java.io.IOException;

import com.opencsv.exceptions.CsvException;
import com.wallet.control.web.dto.StatementUploadDTO;
import com.wallet.control.web.model.BankStatement;

public interface BankStatementReader {
	
	BankStatement readAndParse(String fileName, StatementUploadDTO uploadDTO) throws IOException, CsvException, InterruptedException;
	
}
